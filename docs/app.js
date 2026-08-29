(function () {
    var BUCKETS = ["1-2", "3", "4", "5", "6", "7", "8+"];
    var CHART_MIN_SAMPLES = 10;
    var CHART_HIT_RADIUS = 14;
    var CHART_SPR_DOMAIN = [-0.25, 1.75];
    var CHART_VAR_DOMAIN = [0.25, 1.25];
    var SVG_NS = "http://www.w3.org/2000/svg";
    var manifest = null;
    var selectedCourseSnapshot = null;
    var selectedCourseRequest = 0;
    var chartPoints = [];

    var filters = document.getElementById("filters");
    var courseSelect = document.getElementById("course");
    var ratingFromInput = document.getElementById("rating-from");
    var ratingToInput = document.getElementById("rating-to");
    var snapshotMeta = document.getElementById("snapshot-meta");
    var message = document.getElementById("message");
    var tableWrap = document.getElementById("table-wrap");
    var tableBody = document.getElementById("statistics-body");
    var chartSection = document.getElementById("chart-section");
    var chartStatus = document.getElementById("chart-status");
    var chartCanvas = document.getElementById("chart-canvas");
    var chartSvg = document.getElementById("spr-var-chart");
    var chartTooltip = document.getElementById("chart-tooltip");

    fetch("data/statistics.json")
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Manifest not found");
            }
            return response.json();
        })
        .then(function (data) {
            manifest = data;
            initializePage();
        })
        .catch(function () {
            showMessage("No exported statistics manifest found. Run the local basket statistics export first.", true);
        });

    filters.addEventListener("submit", function (event) {
        event.preventDefault();
        renderStatistics();
    });

    courseSelect.addEventListener("change", function () {
        loadSelectedCourse();
    });

    chartSvg.addEventListener("pointermove", function (event) {
        handleChartPointer(event);
    });

    chartSvg.addEventListener("pointerleave", function () {
        hideChartTooltip();
    });

    function initializePage() {
        renderMetadata();
        populateCourses();
        if (courseSelect.value) {
            loadSelectedCourse();
        }
    }

    function renderMetadata() {
        if (!manifest || !manifest.metadata || !manifest.metadata.exportedAt) {
            snapshotMeta.textContent = "";
            return;
        }
        snapshotMeta.textContent = "Exported at " + manifest.metadata.exportedAt;
    }

    function populateCourses() {
        var courses = Array.isArray(manifest.courses) ? manifest.courses : [];
        courseSelect.innerHTML = "";
        courses.forEach(function (course) {
            var option = document.createElement("option");
            option.value = String(course.id);
            option.textContent = course.name;
            courseSelect.appendChild(option);
        });
        filters.hidden = courses.length === 0;
        if (courses.length === 0) {
            resetChart();
            showMessage("No basket courses with mapped rated results are available.", false);
        }
    }

    function loadSelectedCourse() {
        var course = selectedCourseOption();
        selectedCourseSnapshot = null;
        tableWrap.hidden = true;
        tableBody.innerHTML = "";
        showChartStatus("Loading selected basket course statistics...");

        if (!course) {
            resetChart();
            showMessage("No basket course selected.", false);
            return;
        }

        var requestId = ++selectedCourseRequest;
        showMessage("Loading selected basket course statistics...", false);
        fetch(courseDataUrl(course))
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Course data not found");
                }
                return response.json();
            })
            .then(function (data) {
                if (requestId !== selectedCourseRequest) {
                    return;
                }
                selectedCourseSnapshot = data;
                renderStatistics();
            })
            .catch(function () {
                if (requestId !== selectedCourseRequest) {
                    return;
                }
                resetChart();
                showMessage("Statistics data for the selected basket course could not be loaded.", true);
            });
    }

    function selectedCourseOption() {
        if (!manifest || !courseSelect.value) {
            return null;
        }
        var selectedId = Number(courseSelect.value);
        var courses = Array.isArray(manifest.courses) ? manifest.courses : [];
        for (var index = 0; index < courses.length; index++) {
            if (Number(courses[index].id) === selectedId) {
                return courses[index];
            }
        }
        return null;
    }

    function courseDataUrl(course) {
        var path = course.path || "courses/" + course.id + ".json";
        return "data/" + String(path).replace(/^\/+/, "");
    }

    function renderStatistics() {
        if (!manifest || !selectedCourseSnapshot) {
            return;
        }

        var from = parseOptionalNumber(ratingFromInput.value);
        var to = parseOptionalNumber(ratingToInput.value);
        var samples = Array.isArray(selectedCourseSnapshot.samples) ? selectedCourseSnapshot.samples : [];
        var filteredSamples = samples.filter(function (sample) {
            return ratingMatches(sample.rating, from, to);
        });
        var grouped = groupSamples(filteredSamples);

        showChartStatus("Calculating chart...");
        renderChart(filteredSamples);

        tableBody.innerHTML = "";
        if (grouped.length === 0) {
            tableWrap.hidden = true;
            showMessage("No results for the selected basket course and rating range.", false);
            return;
        }

        grouped.forEach(function (basket) {
            appendBasketRow(basket);
            basket.variations.forEach(appendVariationRow);
        });
        hideMessage();
        tableWrap.hidden = false;
    }

    function parseOptionalNumber(value) {
        if (value === null || value.trim() === "") {
            return null;
        }
        var parsed = Number(value);
        return Number.isFinite(parsed) ? parsed : null;
    }

    function ratingMatches(rating, from, to) {
        if (typeof rating !== "number") {
            return false;
        }
        if (from !== null && rating < from) {
            return false;
        }
        return !(to !== null && rating > to);
    }

    function groupSamples(samples) {
        var basketsById = new Map();

        samples.forEach(function (sample) {
            if (!basketsById.has(sample.basketId)) {
                basketsById.set(sample.basketId, {
                    id: sample.basketId,
                    label: sample.basketLabel,
                    variationsById: new Map()
                });
            }
            var basket = basketsById.get(sample.basketId);
            if (!basket.variationsById.has(sample.variationId)) {
                basket.variationsById.set(sample.variationId, {
                    id: sample.variationId,
                    label: sample.variationLabel,
                    samples: [],
                    scores: []
                });
            }
            basket.variationsById.get(sample.variationId).samples.push(sample);
            basket.variationsById.get(sample.variationId).scores.push(sample.score);
        });

        return Array.from(basketsById.values())
            .sort(compareById)
            .map(function (basket) {
                basket.variations = Array.from(basket.variationsById.values())
                    .filter(function (variation) {
                        return variation.scores.length > 0;
                    })
                    .sort(compareById)
                    .map(buildVariationStats);
                return basket;
            })
            .filter(function (basket) {
                return basket.variations.length > 0;
            });
    }

    function buildVariationStats(variation) {
        var count = variation.scores.length;
        var sum = variation.scores.reduce(function (total, score) {
            return total + score;
        }, 0);
        var bucketCounts = {};
        BUCKETS.forEach(function (bucket) {
            bucketCounts[bucket] = 0;
        });
        variation.scores.forEach(function (score) {
            bucketCounts[bucketFor(score)]++;
        });
        return {
            id: variation.id,
            label: variation.label,
            count: count,
            average: sum / count,
            percentages: BUCKETS.map(function (bucket) {
                return bucketCounts[bucket] * 100 / count;
            })
        };
    }

    function bucketFor(score) {
        if (score <= 2) {
            return "1-2";
        }
        if (score >= 8) {
            return "8+";
        }
        return String(score);
    }

    function appendBasketRow(basket) {
        var row = document.createElement("tr");
        var cell = document.createElement("th");
        row.className = "basket-row";
        cell.colSpan = 10;
        cell.textContent = basket.label;
        row.appendChild(cell);
        tableBody.appendChild(row);
    }

    function appendVariationRow(variation) {
        var row = document.createElement("tr");
        row.className = "variation-row";
        appendCell(row, variation.label);
        appendCell(row, variation.average.toFixed(3));
        variation.percentages.forEach(function (percentage) {
            appendCell(row, percentage.toFixed(1));
        });
        appendCell(row, String(variation.count));
        tableBody.appendChild(row);
    }

    function appendCell(row, value) {
        var cell = document.createElement("td");
        cell.textContent = value;
        row.appendChild(cell);
    }

    function compareById(left, right) {
        return Number(left.id) - Number(right.id);
    }

    function renderChart(samples) {
        chartPoints = buildChartPoints(samples);
        chartSvg.innerHTML = "";
        hideChartTooltip();
        chartSection.hidden = false;

        if (chartPoints.length === 0) {
            showChartStatus("No chart points for the selected basket course and rating range.");
            return;
        }

        chartStatus.textContent = "";
        drawScatterPlot(chartPoints);
    }

    function buildChartPoints(samples) {
        return groupChartSamples(samples)
            .map(buildChartPoint)
            .filter(function (point) {
                return point !== null;
            })
            .sort(function (left, right) {
                var basketComparison = compareById(left.basket, right.basket);
                if (basketComparison !== 0) {
                    return basketComparison;
                }
                return compareById(left, right);
            });
    }

    function groupChartSamples(samples) {
        var groupsByKey = new Map();

        samples.forEach(function (sample) {
            var key = sample.basketId + ":" + sample.variationId;
            if (!groupsByKey.has(key)) {
                groupsByKey.set(key, {
                    id: sample.variationId,
                    label: sample.variationLabel,
                    basket: {
                        id: sample.basketId,
                        label: sample.basketLabel
                    },
                    samples: []
                });
            }
            groupsByKey.get(key).samples.push(sample);
        });

        return Array.from(groupsByKey.values());
    }

    function buildChartPoint(group) {
        if (group.samples.length < CHART_MIN_SAMPLES) {
            return null;
        }

        var regression = linearRegression(group.samples);
        if (regression === null) {
            return null;
        }

        var residualTotal = group.samples.reduce(function (total, sample) {
            var expected = regression.intercept + regression.slope * sample.rating;
            return total + Math.abs(sample.score - expected);
        }, 0);

        return {
            id: group.id,
            label: group.label,
            basket: group.basket,
            count: group.samples.length,
            spr: -100 * regression.slope,
            variation: residualTotal / group.samples.length,
            x: 0,
            y: 0,
            marker: "point",
            directionX: 0,
            directionY: 0
        };
    }

    function linearRegression(samples) {
        var count = samples.length;
        var sums = samples.reduce(function (total, sample) {
            total.rating += sample.rating;
            total.score += sample.score;
            return total;
        }, {rating: 0, score: 0});
        var meanRating = sums.rating / count;
        var meanScore = sums.score / count;
        var ratingVariance = 0;
        var covariance = 0;

        samples.forEach(function (sample) {
            var ratingDelta = sample.rating - meanRating;
            ratingVariance += ratingDelta * ratingDelta;
            covariance += ratingDelta * (sample.score - meanScore);
        });

        if (ratingVariance === 0) {
            return null;
        }

        var slope = covariance / ratingVariance;
        return {
            slope: slope,
            intercept: meanScore - slope * meanRating
        };
    }

    function drawScatterPlot(points) {
        var bounds = chartCanvas.getBoundingClientRect();
        var width = Math.max(Math.round(bounds.width), 320);
        var height = Math.max(Math.round(bounds.height), 300);
        var margin = {top: 22, right: 24, bottom: 54, left: 58};
        var plotWidth = width - margin.left - margin.right;
        var plotHeight = height - margin.top - margin.bottom;
        var sprDomain = CHART_SPR_DOMAIN;
        var varDomain = CHART_VAR_DOMAIN;

        chartSvg.setAttribute("viewBox", "0 0 " + width + " " + height);
        chartSvg.setAttribute("width", String(width));
        chartSvg.setAttribute("height", String(height));

        drawChartGrid(width, height, margin, plotWidth, plotHeight, sprDomain, varDomain);

        points.forEach(function (point) {
            prepareChartMarker(point, sprDomain, varDomain, margin, plotWidth, plotHeight);
            appendChartMarker(point);
        });
    }

    function prepareChartMarker(point, sprDomain, varDomain, margin, plotWidth, plotHeight) {
        var clampedSpr = clamp(point.spr, sprDomain[0], sprDomain[1]);
        var clampedVar = clamp(point.variation, varDomain[0], varDomain[1]);

        point.directionX = direction(point.spr, sprDomain);
        point.directionY = direction(point.variation, varDomain);
        point.marker = point.directionX === 0 && point.directionY === 0 ? "point" : "arrow";
        point.x = scale(clampedSpr, sprDomain, margin.left, margin.left + plotWidth);
        point.y = scale(clampedVar, varDomain, margin.top + plotHeight, margin.top);
    }

    function appendChartMarker(point) {
        var attributes = {
            class: point.marker === "arrow" ? "chart-arrow" : "chart-point",
            "data-basket-id": point.basket.id,
            "data-variation-id": point.id
        };

        if (point.marker === "arrow") {
            attributes.d = arrowPath(point.x, point.y);
            attributes.transform = "rotate(" + arrowAngle(point.directionX, point.directionY) + " " + point.x + " " + point.y + ")";
            appendSvg("path", attributes);
            return;
        }

        attributes.cx = point.x;
        attributes.cy = point.y;
        attributes.r = 5;
        appendSvg("circle", attributes);
    }

    function drawChartGrid(width, height, margin, plotWidth, plotHeight, sprDomain, varDomain) {
        var left = margin.left;
        var right = width - margin.right;
        var top = margin.top;
        var bottom = height - margin.bottom;
        var sprTicks = ticks(sprDomain);
        var varTicks = ticks(varDomain);

        varTicks.forEach(function (tick) {
            var y = scale(tick, varDomain, bottom, top);
            appendSvg("line", {class: "chart-grid", x1: left, y1: y, x2: right, y2: y});
            appendSvg("text", {class: "chart-tick-label", x: left - 8, y: y + 4, "text-anchor": "end"}, formatAxisValue(tick));
        });

        sprTicks.forEach(function (tick) {
            var x = scale(tick, sprDomain, left, right);
            appendSvg("line", {class: "chart-grid", x1: x, y1: top, x2: x, y2: bottom});
            appendSvg("text", {class: "chart-tick-label", x: x, y: bottom + 20, "text-anchor": "middle"}, formatAxisValue(tick));
        });

        appendSvg("line", {class: "chart-axis", x1: left, y1: bottom, x2: right, y2: bottom});
        appendSvg("line", {class: "chart-axis", x1: left, y1: top, x2: left, y2: bottom});
        appendSvg("text", {class: "chart-axis-label", x: left + plotWidth / 2, y: height - 16, "text-anchor": "middle"}, "SPR");
        appendSvg("text", {
            class: "chart-axis-label",
            x: 18,
            y: top + plotHeight / 2,
            "text-anchor": "middle",
            transform: "rotate(-90 18 " + (top + plotHeight / 2) + ")"
        }, "VAR");
    }

    function ticks(domain) {
        var result = [];
        var steps = 4;
        var step = (domain[1] - domain[0]) / steps;
        for (var index = 0; index <= steps; index++) {
            result.push(domain[0] + step * index);
        }
        return result;
    }

    function scale(value, domain, rangeStart, rangeEnd) {
        return rangeStart + (value - domain[0]) * (rangeEnd - rangeStart) / (domain[1] - domain[0]);
    }

    function clamp(value, min, max) {
        return Math.min(Math.max(value, min), max);
    }

    function direction(value, domain) {
        if (value < domain[0]) {
            return -1;
        }
        if (value > domain[1]) {
            return 1;
        }
        return 0;
    }

    function arrowPath(x, y) {
        return "M " + (x + 8) + " " + y
            + " L " + (x - 4) + " " + (y - 7)
            + " L " + (x - 1) + " " + y
            + " L " + (x - 4) + " " + (y + 7)
            + " Z";
    }

    function arrowAngle(directionX, directionY) {
        return Math.atan2(directionY * -1, directionX) * 180 / Math.PI;
    }

    function appendSvg(tagName, attributes, text) {
        var element = document.createElementNS(SVG_NS, tagName);
        Object.keys(attributes).forEach(function (name) {
            element.setAttribute(name, String(attributes[name]));
        });
        if (text !== undefined) {
            element.textContent = text;
        }
        chartSvg.appendChild(element);
        return element;
    }

    function handleChartPointer(event) {
        if (chartPoints.length === 0) {
            hideChartTooltip();
            return;
        }

        var svgPoint = pointerToSvgPoint(event);
        var matches = chartPoints.filter(function (point) {
            var dx = point.x - svgPoint.x;
            var dy = point.y - svgPoint.y;
            return Math.sqrt(dx * dx + dy * dy) <= CHART_HIT_RADIUS;
        });

        if (matches.length === 0) {
            hideChartTooltip();
            return;
        }

        showChartTooltip(matches, event);
    }

    function pointerToSvgPoint(event) {
        var point = chartSvg.createSVGPoint();
        point.x = event.clientX;
        point.y = event.clientY;
        return point.matrixTransform(chartSvg.getScreenCTM().inverse());
    }

    function showChartTooltip(points, event) {
        chartTooltip.innerHTML = "";
        var title = document.createElement("div");
        title.className = "chart-tooltip-title";
        title.textContent = points.length === 1 ? "Basket variation" : points.length + " basket variations";
        chartTooltip.appendChild(title);

        points.forEach(function (point) {
            var row = document.createElement("div");
            row.className = "chart-tooltip-row";
            row.textContent = "Basket " + point.basket.label + " - " + point.label
                + " | Count " + point.count
                + " | SPR " + formatMetric(point.spr)
                + " | VAR " + formatMetric(point.variation);
            chartTooltip.appendChild(row);
        });

        positionTooltip(event);
        chartTooltip.hidden = false;
        markHoveredPoints(points);
    }

    function positionTooltip(event) {
        var canvasBounds = chartCanvas.getBoundingClientRect();
        var left = event.clientX - canvasBounds.left + 12;
        var top = event.clientY - canvasBounds.top + 12;
        chartTooltip.style.left = left + "px";
        chartTooltip.style.top = top + "px";

        var tooltipBounds = chartTooltip.getBoundingClientRect();
        if (left + tooltipBounds.width > canvasBounds.width - 8) {
            chartTooltip.style.left = Math.max(8, canvasBounds.width - tooltipBounds.width - 8) + "px";
        }
        if (top + tooltipBounds.height > canvasBounds.height - 8) {
            chartTooltip.style.top = Math.max(8, canvasBounds.height - tooltipBounds.height - 8) + "px";
        }
    }

    function markHoveredPoints(points) {
        var hoveredKeys = new Set(points.map(function (point) {
            return point.basket.id + ":" + point.id;
        }));
        Array.from(chartSvg.querySelectorAll(".chart-point, .chart-arrow")).forEach(function (element) {
            var key = element.getAttribute("data-basket-id") + ":" + element.getAttribute("data-variation-id");
            element.classList.toggle("hovered", hoveredKeys.has(key));
        });
    }

    function hideChartTooltip() {
        chartTooltip.hidden = true;
        chartTooltip.innerHTML = "";
        Array.from(chartSvg.querySelectorAll(".chart-point, .chart-arrow")).forEach(function (element) {
            element.classList.remove("hovered");
        });
    }

    function showChartStatus(text) {
        chartSection.hidden = false;
        chartStatus.textContent = text;
        chartSvg.innerHTML = "";
        hideChartTooltip();
    }

    function resetChart() {
        chartPoints = [];
        chartSection.hidden = true;
        chartStatus.textContent = "";
        chartSvg.innerHTML = "";
        hideChartTooltip();
    }

    function formatAxisValue(value) {
        return value.toFixed(2);
    }

    function formatMetric(value) {
        return value.toFixed(3);
    }

    function showMessage(text, isError) {
        message.textContent = text;
        message.classList.toggle("error", isError);
        message.hidden = false;
        tableWrap.hidden = true;
    }

    function hideMessage() {
        message.textContent = "";
        message.classList.remove("error");
        message.hidden = true;
    }
})();
