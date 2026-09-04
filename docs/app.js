(function () {
    var BUCKETS = ["1-2", "3", "4", "5", "6", "7", "8+"];
    var CHART_MIN_SAMPLES = 50;
    var CHART_HIT_RADIUS = 14;
    var CHART_SPR_DOMAIN = [-0.25, 1.75];
    var CHART_VAR_DOMAIN = [0.25, 1.25];
    var BASKET_SPR_DOMAIN = [-0.5, 2.0];
    var SVG_NS = "http://www.w3.org/2000/svg";
    var manifest = null;
    var selectedCourseSnapshot = null;
    var selectedCourseRequest = 0;
    var chartPoints = [];
    var selectedBasketStatsSnapshot = null;
    var selectedBasketStatsRequest = 0;
    var basketChartPoints = [];

    var courseStatsTab = document.getElementById("course-stats-tab");
    var basketStatsTab = document.getElementById("basket-stats-tab");
    var courseStatsView = document.getElementById("course-stats-view");
    var basketStatsView = document.getElementById("basket-stats-view");
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
    var basketFilters = document.getElementById("basket-filters");
    var basketCourseSelect = document.getElementById("basket-course");
    var basketVariationSelect = document.getElementById("basket-variation");
    var basketMessage = document.getElementById("basket-message");
    var basketChartSection = document.getElementById("basket-chart-section");
    var basketChartStatus = document.getElementById("basket-chart-status");
    var basketChartCanvas = document.getElementById("basket-chart-canvas");
    var basketChartSvg = document.getElementById("basket-stats-chart");
    var basketChartTooltip = document.getElementById("basket-chart-tooltip");

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
            showBasketMessage("No exported statistics manifest found. Run the local basket statistics export first.", true);
        });

    courseStatsTab.addEventListener("click", function () {
        setActiveView("course");
    });

    basketStatsTab.addEventListener("click", function () {
        setActiveView("basket");
    });

    filters.addEventListener("submit", function (event) {
        event.preventDefault();
        renderStatistics();
    });

    courseSelect.addEventListener("change", function () {
        loadSelectedCourse();
    });

    basketCourseSelect.addEventListener("change", function () {
        loadSelectedBasketStatsCourse();
    });

    basketVariationSelect.addEventListener("change", function () {
        renderSelectedBasketVariation();
    });

    chartSvg.addEventListener("pointermove", function (event) {
        handleChartPointer(event);
    });

    chartSvg.addEventListener("pointerleave", function () {
        hideChartTooltip();
    });

    basketChartSvg.addEventListener("pointermove", function (event) {
        handleBasketChartPointer(event);
    });

    basketChartSvg.addEventListener("pointerleave", function () {
        hideBasketChartTooltip();
    });

    function initializePage() {
        renderMetadata();
        populateCourses();
        populateBasketCourses();
        if (courseSelect.value) {
            loadSelectedCourse();
        }
        if (basketCourseSelect.value) {
            loadSelectedBasketStatsCourse();
        }
    }

    function setActiveView(view) {
        var basketActive = view === "basket";
        courseStatsView.hidden = basketActive;
        basketStatsView.hidden = !basketActive;
        courseStatsTab.classList.toggle("active", !basketActive);
        basketStatsTab.classList.toggle("active", basketActive);
        if (basketActive) {
            renderSelectedBasketVariation();
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

    function populateBasketCourses() {
        var courses = basketStatsCourses();
        basketCourseSelect.innerHTML = "";
        courses.forEach(function (course) {
            var option = document.createElement("option");
            option.value = String(course.id);
            option.textContent = course.name;
            basketCourseSelect.appendChild(option);
        });
        basketFilters.hidden = courses.length === 0;
        basketVariationSelect.innerHTML = "";
        resetBasketChart();
        if (courses.length === 0) {
            showBasketMessage("No basket courses with eligible sliding-window statistics are available.", false);
        } else {
            hideBasketMessage();
        }
    }

    function basketStatsCourses() {
        var courses = Array.isArray(manifest.courses) ? manifest.courses : [];
        return courses.filter(function (course) {
            return !!course.basketStatsPath;
        });
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

    function loadSelectedBasketStatsCourse() {
        var course = selectedBasketCourseOption();
        selectedBasketStatsSnapshot = null;
        basketVariationSelect.innerHTML = "";
        resetBasketChart();

        if (!course) {
            showBasketMessage("No basket course with eligible sliding-window statistics is selected.", false);
            return;
        }

        var requestId = ++selectedBasketStatsRequest;
        showBasketMessage("Loading selected basket course sliding-window statistics...", false);
        showBasketChartStatus("Loading selected basket course sliding-window statistics...");
        fetch(basketStatsDataUrl(course))
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Basket stats data not found");
                }
                return response.json();
            })
            .then(function (data) {
                if (requestId !== selectedBasketStatsRequest) {
                    return;
                }
                selectedBasketStatsSnapshot = data;
                populateBasketVariations();
            })
            .catch(function () {
                if (requestId !== selectedBasketStatsRequest) {
                    return;
                }
                resetBasketChart();
                showBasketMessage("Sliding-window statistics for the selected basket course could not be loaded.", true);
            });
    }

    function selectedCourseOption() {
        return selectedCourseFromSelect(courseSelect);
    }

    function selectedBasketCourseOption() {
        return selectedCourseFromSelect(basketCourseSelect);
    }

    function selectedCourseFromSelect(select) {
        if (!manifest || !select.value) {
            return null;
        }
        var selectedId = Number(select.value);
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

    function basketStatsDataUrl(course) {
        return "data/" + String(course.basketStatsPath).replace(/^\/+/, "");
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

    function populateBasketVariations() {
        var variations = basketVariations();
        basketVariationSelect.innerHTML = "";
        variations.forEach(function (variation) {
            var option = document.createElement("option");
            option.value = variationKey(variation);
            option.textContent = variation.basketLabel + " - " + variation.variationLabel;
            basketVariationSelect.appendChild(option);
        });

        if (variations.length === 0) {
            resetBasketChart();
            showBasketMessage("No basket variations with eligible sliding-window statistics are available for this course.", false);
            return;
        }

        hideBasketMessage();
        renderSelectedBasketVariation();
    }

    function renderSelectedBasketVariation() {
        var variation = selectedBasketVariation();
        if (!variation) {
            resetBasketChart();
            showBasketMessage("No basket variation selected.", false);
            return;
        }

        var windows = Array.isArray(variation.windows) ? variation.windows : [];
        if (windows.length === 0) {
            resetBasketChart();
            showBasketMessage("The selected basket variation has no sliding-window results.", false);
            return;
        }

        hideBasketMessage();
        drawBasketLineChart(variation, windows);
    }

    function basketVariations() {
        if (!selectedBasketStatsSnapshot || !Array.isArray(selectedBasketStatsSnapshot.variations)) {
            return [];
        }
        return selectedBasketStatsSnapshot.variations;
    }

    function selectedBasketVariation() {
        var key = basketVariationSelect.value;
        var variations = basketVariations();
        for (var index = 0; index < variations.length; index++) {
            if (variationKey(variations[index]) === key) {
                return variations[index];
            }
        }
        return null;
    }

    function variationKey(variation) {
        return variation.basketId + ":" + variation.variationId;
    }

    function parseOptionalNumber(value) {
        if (value === null || value.trim() === "") {
            return null;
        }
        var parsed = Number(value);
        return Number.isFinite(parsed) ? parsed : null;
    }

    function isNumeric(value) {
        return typeof value === "number" && Number.isFinite(value);
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
        var metrics = chartMetrics(chartCanvas, {top: 22, right: 24, bottom: 54, left: 58}, 320, 300);
        chartSvg.setAttribute("viewBox", "0 0 " + metrics.width + " " + metrics.height);
        chartSvg.setAttribute("width", String(metrics.width));
        chartSvg.setAttribute("height", String(metrics.height));
        drawChartGrid(chartSvg, metrics, CHART_SPR_DOMAIN, CHART_VAR_DOMAIN, "SPR", "VAR", false);

        points.forEach(function (point) {
            prepareChartMarker(point, CHART_SPR_DOMAIN, CHART_VAR_DOMAIN, metrics);
            appendChartMarker(point);
        });
    }

    function prepareChartMarker(point, sprDomain, varDomain, metrics) {
        var clampedSpr = clamp(point.spr, sprDomain[0], sprDomain[1]);
        var clampedVar = clamp(point.variation, varDomain[0], varDomain[1]);

        point.directionX = direction(point.spr, sprDomain);
        point.directionY = direction(point.variation, varDomain);
        point.marker = point.directionX === 0 && point.directionY === 0 ? "point" : "arrow";
        point.x = scale(clampedSpr, sprDomain, metrics.left, metrics.right);
        point.y = scale(clampedVar, varDomain, metrics.bottom, metrics.top);
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
            appendSvg(chartSvg, "path", attributes);
            return;
        }

        attributes.cx = point.x;
        attributes.cy = point.y;
        attributes.r = 5;
        appendSvg(chartSvg, "circle", attributes);
    }

    function drawBasketLineChart(variation, windows) {
        var metrics = chartMetrics(basketChartCanvas, {top: 24, right: 24, bottom: 58, left: 58}, 360, 300);
        var xDomain = paddedRatingDomain(windows);
        basketChartPoints = [];
        basketChartSvg.innerHTML = "";
        hideBasketChartTooltip();
        basketChartSection.hidden = false;
        basketChartStatus.textContent = variation.basketLabel + " - " + variation.variationLabel;
        basketChartSvg.setAttribute("viewBox", "0 0 " + metrics.width + " " + metrics.height);
        basketChartSvg.setAttribute("width", String(metrics.width));
        basketChartSvg.setAttribute("height", String(metrics.height));

        drawBasketGrid(metrics, xDomain);
        drawBasketSeriesSegments(windows, "sprw", "sprwCountBucket", xDomain, BASKET_SPR_DOMAIN, metrics, "basket-sprw-line");
        drawBasketPoints(windows, xDomain, metrics);
        drawBasketLegend(metrics);
    }

    function drawBasketGrid(metrics, xDomain) {
        drawChartGridWithTicks(
            basketChartSvg,
            metrics,
            xDomain,
            BASKET_SPR_DOMAIN,
            ratingTicks(xDomain),
            [0, 0.5, 1, 1.5],
            "Rating",
            "SPRW",
            true
        );
    }

    function drawBasketSeriesSegments(windows, metric, countBucketField, xDomain, yDomain, metrics, baseClass) {
        for (var index = 0; index < windows.length - 1; index++) {
            var current = windows[index];
            var next = windows[index + 1];
            if (!isNumeric(current[metric]) || !isNumeric(next[metric])) {
                continue;
            }
            var x1 = scale(current.ratingMidpoint, xDomain, metrics.left, metrics.right);
            var y1 = scale(current[metric], yDomain, metrics.bottom, metrics.top);
            var x2 = scale(next.ratingMidpoint, xDomain, metrics.left, metrics.right);
            var y2 = scale(next[metric], yDomain, metrics.bottom, metrics.top);
            var currentBucket = current[countBucketField];
            var nextBucket = next[countBucketField];

            if (currentBucket === nextBucket) {
                appendBasketLine(x1, y1, x2, y2, baseClass, currentBucket);
            } else {
                appendBasketLine(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2, baseClass, currentBucket);
                appendBasketLine((x1 + x2) / 2, (y1 + y2) / 2, x2, y2, baseClass, nextBucket);
            }
        }
    }

    function appendBasketLine(x1, y1, x2, y2, baseClass, countBucket) {
        if (countBucket === "50-99") {
            return;
        }
        appendSvg(basketChartSvg, "line", {
            class: baseClass + " " + bucketClass(countBucket),
            x1: x1,
            y1: y1,
            x2: x2,
            y2: y2
        });
    }

    function drawBasketPoints(windows, xDomain, metrics) {
        windows.forEach(function (window) {
            addBasketPoint(window, "SPRW", "sprw", BASKET_SPR_DOMAIN, xDomain, metrics);
        });
    }

    function addBasketPoint(window, label, metric, yDomain, xDomain, metrics) {
        if (!isNumeric(window[metric])) {
            return;
        }
        var point = {
            window: window,
            metric: label,
            valueField: metric,
            x: scale(window.ratingMidpoint, xDomain, metrics.left, metrics.right),
            y: scale(window[metric], yDomain, metrics.bottom, metrics.top)
        };
        basketChartPoints.push(point);
        appendSvg(basketChartSvg, "circle", {
            class: "basket-chart-point " + metric,
            cx: point.x,
            cy: point.y,
            r: 4,
            "data-metric": label,
            "data-midpoint": window.ratingMidpoint
        });
    }

    function drawBasketLegend(metrics) {
        var y = metrics.top + 14;
        var x = metrics.left + 8;
        appendSvg(basketChartSvg, "line", {class: "basket-sprw-line basket-line-200-plus", x1: x, y1: y, x2: x + 28, y2: y});
        appendSvg(basketChartSvg, "text", {class: "chart-tick-label", x: x + 36, y: y + 4}, "SPRW");
    }

    function bucketClass(countBucket) {
        if (countBucket === "50-99") {
            return "basket-line-50-99";
        }
        if (countBucket === "200+") {
            return "basket-line-200-plus";
        }
        return "basket-line-100-199";
    }

    function paddedRatingDomain(windows) {
        var values = windows.map(function (window) {
            return window.ratingMidpoint;
        });
        var min = Math.min.apply(null, values);
        var max = Math.max.apply(null, values);
        if (min === max) {
            return [min - 25, max + 25];
        }
        return [min - 10, max + 10];
    }

    function drawChartGrid(svg, metrics, xDomain, yDomain, xLabel, yLabel, wholeXLabels) {
        drawChartGridWithTicks(svg, metrics, xDomain, yDomain, ticks(xDomain), ticks(yDomain), xLabel, yLabel, wholeXLabels);
    }

    function drawChartGridWithTicks(svg, metrics, xDomain, yDomain, xTicks, yTicks, xLabel, yLabel, wholeXLabels) {
        yTicks.forEach(function (tick) {
            var y = scale(tick, yDomain, metrics.bottom, metrics.top);
            appendSvg(svg, "line", {class: "chart-grid", x1: metrics.left, y1: y, x2: metrics.right, y2: y});
            appendSvg(svg, "text", {class: "chart-tick-label", x: metrics.left - 8, y: y + 4, "text-anchor": "end"}, formatAxisValue(tick));
        });

        xTicks.forEach(function (tick) {
            var x = scale(tick, xDomain, metrics.left, metrics.right);
            appendSvg(svg, "line", {class: "chart-grid", x1: x, y1: metrics.top, x2: x, y2: metrics.bottom});
            appendSvg(svg, "text", {class: "chart-tick-label", x: x, y: metrics.bottom + 20, "text-anchor": "middle"}, wholeXLabels ? String(Math.round(tick)) : formatAxisValue(tick));
        });

        appendSvg(svg, "line", {class: "chart-axis", x1: metrics.left, y1: metrics.bottom, x2: metrics.right, y2: metrics.bottom});
        appendSvg(svg, "line", {class: "chart-axis", x1: metrics.left, y1: metrics.top, x2: metrics.left, y2: metrics.bottom});
        appendSvg(svg, "text", {class: "chart-axis-label", x: metrics.left + metrics.plotWidth / 2, y: metrics.height - 16, "text-anchor": "middle"}, xLabel);
        appendSvg(svg, "text", {
            class: "chart-axis-label",
            x: 18,
            y: metrics.top + metrics.plotHeight / 2,
            "text-anchor": "middle",
            transform: "rotate(-90 18 " + (metrics.top + metrics.plotHeight / 2) + ")"
        }, yLabel);
    }

    function ratingTicks(domain) {
        var result = [];
        var start = Math.ceil(domain[0] / 20) * 20;
        var end = Math.floor(domain[1] / 20) * 20;
        for (var tick = start; tick <= end; tick += 20) {
            result.push(tick);
        }
        return result;
    }

    function chartMetrics(canvas, margin, minWidth, minHeight) {
        var bounds = canvas.getBoundingClientRect();
        var width = Math.max(Math.round(bounds.width), minWidth);
        var height = Math.max(Math.round(bounds.height), minHeight);
        return {
            width: width,
            height: height,
            top: margin.top,
            right: width - margin.right,
            bottom: height - margin.bottom,
            left: margin.left,
            plotWidth: width - margin.left - margin.right,
            plotHeight: height - margin.top - margin.bottom
        };
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

    function appendSvg(svg, tagName, attributes, text) {
        var element = document.createElementNS(SVG_NS, tagName);
        Object.keys(attributes).forEach(function (name) {
            element.setAttribute(name, String(attributes[name]));
        });
        if (text !== undefined) {
            element.textContent = text;
        }
        svg.appendChild(element);
        return element;
    }

    function handleChartPointer(event) {
        if (chartPoints.length === 0) {
            hideChartTooltip();
            return;
        }

        var svgPoint = pointerToSvgPoint(chartSvg, event);
        var matches = chartPoints.filter(function (point) {
            return pointDistance(point, svgPoint) <= CHART_HIT_RADIUS;
        });

        if (matches.length === 0) {
            hideChartTooltip();
            return;
        }

        showChartTooltip(matches, event);
    }

    function handleBasketChartPointer(event) {
        if (basketChartPoints.length === 0) {
            hideBasketChartTooltip();
            return;
        }

        var svgPoint = pointerToSvgPoint(basketChartSvg, event);
        var matches = basketChartPoints.filter(function (point) {
            return pointDistance(point, svgPoint) <= CHART_HIT_RADIUS;
        });

        if (matches.length === 0) {
            hideBasketChartTooltip();
            return;
        }

        showBasketChartTooltip(matches, event);
    }

    function pointDistance(point, svgPoint) {
        var dx = point.x - svgPoint.x;
        var dy = point.y - svgPoint.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    function pointerToSvgPoint(svg, event) {
        var point = svg.createSVGPoint();
        point.x = event.clientX;
        point.y = event.clientY;
        return point.matrixTransform(svg.getScreenCTM().inverse());
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

        positionTooltip(chartTooltip, chartCanvas, event);
        chartTooltip.hidden = false;
        markHoveredPoints(points);
    }

    function showBasketChartTooltip(points, event) {
        basketChartTooltip.innerHTML = "";

        points.forEach(function (point) {
            var row = document.createElement("div");
            var window = point.window;
            row.className = "chart-tooltip-row";
            row.textContent = point.metric
                + " | Rating " + window.ratingMidpoint
                + " | " + formatMetric(window[point.valueField]);
            basketChartTooltip.appendChild(row);
        });

        positionTooltip(basketChartTooltip, basketChartCanvas, event);
        basketChartTooltip.hidden = false;
        markBasketHoveredPoints(points);
    }

    function positionTooltip(tooltip, canvas, event) {
        var canvasBounds = canvas.getBoundingClientRect();
        var left = event.clientX - canvasBounds.left + 12;
        var top = event.clientY - canvasBounds.top + 12;
        tooltip.style.left = left + "px";
        tooltip.style.top = top + "px";

        var tooltipBounds = tooltip.getBoundingClientRect();
        if (left + tooltipBounds.width > canvasBounds.width - 8) {
            tooltip.style.left = Math.max(8, canvasBounds.width - tooltipBounds.width - 8) + "px";
        }
        if (top + tooltipBounds.height > canvasBounds.height - 8) {
            tooltip.style.top = Math.max(8, canvasBounds.height - tooltipBounds.height - 8) + "px";
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

    function markBasketHoveredPoints(points) {
        var hoveredKeys = new Set(points.map(function (point) {
            return point.metric + ":" + point.window.ratingMidpoint;
        }));
        Array.from(basketChartSvg.querySelectorAll(".basket-chart-point")).forEach(function (element) {
            var key = element.getAttribute("data-metric") + ":" + element.getAttribute("data-midpoint");
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

    function hideBasketChartTooltip() {
        basketChartTooltip.hidden = true;
        basketChartTooltip.innerHTML = "";
        Array.from(basketChartSvg.querySelectorAll(".basket-chart-point")).forEach(function (element) {
            element.classList.remove("hovered");
        });
    }

    function showChartStatus(text) {
        chartSection.hidden = false;
        chartStatus.textContent = text;
        chartSvg.innerHTML = "";
        hideChartTooltip();
    }

    function showBasketChartStatus(text) {
        basketChartSection.hidden = false;
        basketChartStatus.textContent = text;
        basketChartSvg.innerHTML = "";
        hideBasketChartTooltip();
    }

    function resetChart() {
        chartPoints = [];
        chartSection.hidden = true;
        chartStatus.textContent = "";
        chartSvg.innerHTML = "";
        hideChartTooltip();
    }

    function resetBasketChart() {
        basketChartPoints = [];
        basketChartSection.hidden = true;
        basketChartStatus.textContent = "";
        basketChartSvg.innerHTML = "";
        hideBasketChartTooltip();
    }

    function formatAxisValue(value) {
        return value.toFixed(2);
    }

    function formatMetric(value) {
        return Number(value).toFixed(3);
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

    function showBasketMessage(text, isError) {
        basketMessage.textContent = text;
        basketMessage.classList.toggle("error", isError);
        basketMessage.hidden = false;
    }

    function hideBasketMessage() {
        basketMessage.textContent = "";
        basketMessage.classList.remove("error");
        basketMessage.hidden = true;
    }
})();
