(function () {
    var BUCKETS = ["1-2", "3", "4", "5", "6", "7", "8+"];
    var SCORE_DISTRIBUTION_COLORS = {
        "1-2": "#164e7a",
        "3": "#62a9d1",
        "4": "#2f855a",
        "5": "#d6a514",
        "6": "#e67e22",
        "7": "#c026d3",
        "8+": "#8b1e2d"
    };
    var CHART_MIN_SAMPLES = 50;
    var CHART_HIT_RADIUS = 14;
    var CHART_SPR_DOMAIN = [-0.25, 1.75];
    var CHART_VAR_DOMAIN = [0.25, 1.25];
    var BASKET_SPR_DOMAIN = [-0.5, 2.0];
    var SVG_NS = "http://www.w3.org/2000/svg";
    var PUTTING_METRIC_TOOLTIPS = {
        c1x: "C1x: Circle 1 excluding bullseye (3 - 10 meters)",
        c2: "C2: Circle 2 (10 - 20 meters)",
        c2Plus: "C2+: Circle 2 and beyond (10+ meters)",
        threePlusPutting: "3+putting: Taking more than 2 throws to complete the hole after landing inside Circle 2"
    };
    var manifest = null;
    var selectedCourseSnapshot = null;
    var selectedCourseRequest = 0;
    var chartPoints = [];
    var selectedBasketStatsSnapshot = null;
    var selectedBasketStatsRequest = 0;
    var basketChartPoints = [];
    var scoreDistributionPoints = [];
    var scoreDistributionVisibility = {};
    var playerLookup = [];
    var selectedPersonalSnapshot = null;
    var selectedPersonalRequest = 0;
    var personalView = "putting";
    var personalPlayerInputTimer = null;
    var leaderboardData = null;
    var leaderboardCategory = "c1x";
    var leaderboardYear = String(new Date().getFullYear());
    var descriptionTab = document.getElementById("description-tab");
    var courseStatsTab = document.getElementById("course-stats-tab");
    var basketStatsTab = document.getElementById("basket-stats-tab");
    var personalStatsTab = document.getElementById("personal-stats-tab");
    var leaderboardsTab = document.getElementById("leaderboards-tab");
    var descriptionView = document.getElementById("description-view");
    var descriptionCompetitionCount = document.getElementById("description-competition-count");
    var descriptionPlayersCount = document.getElementById("description-players-count");
    var descriptionLatestCompetition = document.getElementById("description-latest-competition");
    var courseStatsView = document.getElementById("course-stats-view");
    var basketStatsView = document.getElementById("basket-stats-view");
    var personalStatsView = document.getElementById("personal-stats-view");
    var leaderboardsView = document.getElementById("leaderboards-view");
    var leaderboardsYearSelect = document.getElementById("leaderboards-year");
    var leaderboardsMessage = document.getElementById("leaderboards-message");
    var leaderboardsTableWrap = document.getElementById("leaderboards-table-wrap");
    var leaderboardsBody = document.getElementById("leaderboards-body");
    var leaderboardsNote = document.getElementById("leaderboards-note");
    var leaderboardCategoryTabs = Array.from(document.querySelectorAll(".leaderboard-category-tab"));
    var filters = document.getElementById("filters");
    var courseSelect = document.getElementById("course");
    var ratingFromInput = document.getElementById("rating-from");
    var ratingToInput = document.getElementById("rating-to");
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
    var scoreDistributionSection = document.getElementById("score-distribution-section");
    var scoreDistributionCanvas = document.getElementById("score-distribution-canvas");
    var scoreDistributionSvg = document.getElementById("score-distribution-chart");
    var scoreDistributionTooltip = document.getElementById("score-distribution-tooltip");
    var scoreDistributionLegend = document.getElementById("score-distribution-legend");
    var personalPlayerFilter = document.getElementById("personal-player-filter");
    var personalPlayerInput = document.getElementById("personal-player");
    var personalPlayerOptions = document.getElementById("personal-player-options");
    var personalContent = document.getElementById("personal-content");
    var personalHoleTab = document.getElementById("personal-hole-tab");
    var personalPuttingTab = document.getElementById("personal-putting-tab");
    var personalHoleView = document.getElementById("personal-hole-view");
    var personalPuttingView = document.getElementById("personal-putting-view");
    var personalHoleFilters = document.getElementById("personal-hole-filters");
    var personalCourseSelect = document.getElementById("personal-course");
    var personalMinCountInput = document.getElementById("personal-min-count");
    var personalMessage = document.getElementById("personal-message");
    var personalTableWrap = document.getElementById("personal-table-wrap");
    var personalTableBody = document.getElementById("personal-statistics-body");
    var personalPuttingSummary = document.getElementById("personal-putting-summary");
    var uiTooltip = document.getElementById("ui-tooltip");
    var activeUiTooltipTrigger = null;

    updateDescriptionPlaceholders(null);

    document.addEventListener("pointerover", handleUiTooltipPointerOver);
    document.addEventListener("pointerout", handleUiTooltipPointerOut);
    document.addEventListener("focusin", handleUiTooltipFocusIn);
    document.addEventListener("focusout", handleUiTooltipFocusOut);
    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            hideUiTooltip();
        }
    });

    fetch("data/statistics.json")
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Manifest not found");
            }
            return response.json();
        })
        .then(function (data) {
            manifest = data;
            updateDescriptionPlaceholders(manifest);
            initializePage();
        })
        .catch(function () {
            showMessage("No exported statistics manifest found. Run the local basket statistics export first.", true);
            showBasketMessage("No exported statistics manifest found. Run the local basket statistics export first.", true);
            showPersonalMessage("No exported statistics manifest found. Run the local basket statistics export first.", true);
            updateDescriptionPlaceholders(null);
        });

    descriptionTab.addEventListener("click", function () {
        setActiveView("description");
    });

    courseStatsTab.addEventListener("click", function () {
        setActiveView("course");
    });

    basketStatsTab.addEventListener("click", function () {
        setActiveView("basket");
    });

    personalStatsTab.addEventListener("click", function () {
        setActiveView("personal");
    });

    leaderboardsTab.addEventListener("click", function () {
        setActiveView("leaderboards");
    });

    leaderboardCategoryTabs.forEach(function (tab) {
        tab.addEventListener("click", function () {
            leaderboardCategory = tab.getAttribute("data-category");
            leaderboardCategoryTabs.forEach(function (categoryTab) {
                categoryTab.classList.toggle("active", categoryTab === tab);
            });
            populateLeaderboardYears();
            renderLeaderboards();
        });
    });

    leaderboardsYearSelect.addEventListener("change", function () {
        leaderboardYear = leaderboardsYearSelect.value;
        renderLeaderboards();
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

    personalPlayerInput.addEventListener("input", function () {
        schedulePersonalPlayerInput();
    });

    personalPlayerInput.addEventListener("change", function () {
        schedulePersonalPlayerInput();
    });

    personalCourseSelect.addEventListener("change", function () {
        renderPersonalStatistics();
    });

    personalMinCountInput.addEventListener("input", function () {
        renderPersonalStatistics();
    });

    personalMinCountInput.addEventListener("change", function () {
        normalizePersonalMinCount();
        renderPersonalStatistics();
    });

    personalHoleTab.addEventListener("click", function () {
        setPersonalView("hole");
    });

    personalPuttingTab.addEventListener("click", function () {
        setPersonalView("putting");
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

    scoreDistributionSvg.addEventListener("pointermove", function (event) {
        handleScoreDistributionPointer(event);
    });

    scoreDistributionSvg.addEventListener("pointerleave", function () {
        hideScoreDistributionTooltip();
    });

    function initializePage() {
        setActiveView("leaderboards");
        populateCourses();
        populateBasketCourses();
        loadPlayerLookup();
        populateLeaderboardYears();
        loadLeaderboards();
        if (courseSelect.value) {
            loadSelectedCourse();
        }
        if (basketCourseSelect.value) {
            loadSelectedBasketStatsCourse();
        }
    }

    function setActiveView(view) {
        var descriptionActive = view === "description";
        var courseActive = view === "course";
        var basketActive = view === "basket";
        var personalActive = view === "personal";
        var leaderboardsActive = view === "leaderboards";
        descriptionView.hidden = !descriptionActive;
        courseStatsView.hidden = descriptionActive || basketActive || personalActive || leaderboardsActive;
        basketStatsView.hidden = !basketActive;
        personalStatsView.hidden = !personalActive;
        leaderboardsView.hidden = !leaderboardsActive;
        descriptionTab.classList.toggle("active", descriptionActive);
        courseStatsTab.classList.toggle("active", courseActive);
        basketStatsTab.classList.toggle("active", basketActive);
        personalStatsTab.classList.toggle("active", personalActive);
        leaderboardsTab.classList.toggle("active", leaderboardsActive);
        if (courseActive && selectedCourseSnapshot) {
            renderStatistics();
        }
        if (basketActive) {
            renderSelectedBasketVariation();
        }
        if (personalActive && playerLookup.length === 0 && personalPlayerFilter.hidden) {
            showPersonalMessage("No eligible players are available in this statistics export.", false);
        }
        if (leaderboardsActive) {
            renderLeaderboards();
        }
    }

    function populateLeaderboardYears() {
        leaderboardsYearSelect.innerHTML = "";
        var years = [];
        if (leaderboardCategory === "aces" && leaderboardData) {
            years = (leaderboardData.scopes || [])
                .filter(function (scope) {
                    return scope.scope !== "overall"
                        && scope.categories
                        && Array.isArray(scope.categories.aces)
                        && scope.categories.aces.length > 0;
                })
                .map(function (scope) {
                    return scope.scope;
                })
                .sort(function (left, right) {
                    return Number(right) - Number(left);
                });
        } else {
            for (var year = new Date().getFullYear(); year >= 2025; year--) {
                years.push(String(year));
            }
        }
        years.forEach(function (year) {
            var option = document.createElement("option");
            option.value = year;
            option.textContent = year;
            leaderboardsYearSelect.appendChild(option);
        });
        var overallOption = document.createElement("option");
        overallOption.value = "overall";
        overallOption.textContent = "Overall";
        leaderboardsYearSelect.appendChild(overallOption);

        var availableValues = years.concat(["overall"]);
        if (availableValues.indexOf(leaderboardYear) === -1) {
            leaderboardYear = years.length > 0 ? years[0] : "overall";
        }
        leaderboardsYearSelect.value = leaderboardYear;
    }

    function loadLeaderboards() {
        if (!manifest || !manifest.leaderboardsPath) {
            showLeaderboardsMessage("Leaderboard data is not available in this export.", false);
            return;
        }
        fetch("data/" + manifest.leaderboardsPath)
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Leaderboard data not found");
                }
                return response.json();
            })
            .then(function (data) {
                leaderboardData = data;
                populateLeaderboardYears();
                renderLeaderboards();
            })
            .catch(function () {
                showLeaderboardsMessage("Leaderboard data could not be loaded.", true);
            });
    }

    function renderLeaderboards() {
        if (!leaderboardData) {
            return;
        }
        renderLeaderboardsNote();
        var scope = (leaderboardData.scopes || []).find(function (candidate) {
            return candidate.scope === leaderboardYear;
        });
        var entries = scope && scope.categories && Array.isArray(scope.categories[leaderboardCategory])
            ? scope.categories[leaderboardCategory]
            : [];
        leaderboardsBody.innerHTML = "";
        if (entries.length === 0) {
            showLeaderboardsMessage("No qualifying leaderboard entries are available for this selection.", false);
            return;
        }
        hideLeaderboardsMessage();
        entries.forEach(function (entry) {
            var row = document.createElement("tr");
            appendLeaderboardCell(row, entry.place);
            appendLeaderboardCell(row, entry.name);
            var valueCell = document.createElement("td");
            var tooltips = Array.isArray(entry.tooltips) ? entry.tooltips.filter(Boolean) : [];
            if (tooltips.length > 0) {
                var value = document.createElement("span");
                value.className = "tooltip-trigger";
                value.tabIndex = 0;
                value.textContent = entry.value;
                value.setAttribute("data-tooltip", tooltips.join("\n"));
                value.setAttribute("aria-label", entry.value + ": " + tooltips.join("; "));
                valueCell.appendChild(value);
            } else {
                valueCell.textContent = entry.value;
            }
            row.appendChild(valueCell);
            leaderboardsBody.appendChild(row);
        });
        leaderboardsTableWrap.hidden = false;
    }

    function renderLeaderboardsNote() {
        var overall = leaderboardYear === "overall";
        if (leaderboardCategory === "c1x") {
            leaderboardsNote.textContent = "At least " + (overall ? "100" : "50") + " C1x putts are required.";
            leaderboardsNote.hidden = false;
        } else if (leaderboardCategory === "c2PlusPerRound") {
            leaderboardsNote.textContent = "At least " + (overall ? "180" : "90") + " holes played are required.";
            leaderboardsNote.hidden = false;
        } else {
            leaderboardsNote.textContent = "";
            leaderboardsNote.hidden = true;
        }
    }

    function appendLeaderboardCell(row, value) {
        var cell = document.createElement("td");
        cell.textContent = value === undefined || value === null ? "" : String(value);
        row.appendChild(cell);
    }

    function showLeaderboardsMessage(text, isError) {
        leaderboardsMessage.textContent = text;
        leaderboardsMessage.classList.toggle("error", isError);
        leaderboardsMessage.hidden = false;
        leaderboardsTableWrap.hidden = true;
    }

    function hideLeaderboardsMessage() {
        leaderboardsMessage.textContent = "";
        leaderboardsMessage.classList.remove("error");
        leaderboardsMessage.hidden = true;
    }

    function updateDescriptionPlaceholders(source) {
        var values = source && source.metadata && source.metadata.description
            ? source.metadata.description
            : {};
        descriptionCompetitionCount.textContent = descriptionValue(values.competitionCount);
        descriptionPlayersCount.textContent = descriptionValue(values.playersCount);
        descriptionLatestCompetition.textContent = descriptionValue(values.latestCompetition);
    }

    function descriptionValue(value) {
        return value === undefined || value === null || value === "" ? "unavailable" : String(value);
    }

    function populateCourses() {
        var courses = sortedCourses(manifest.courses);
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
        var courses = sortedCourses(basketStatsCourses());
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
                if (!courseStatsView.hidden) {
                    renderStatistics();
                }
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

    function loadPlayerLookup() {
        playerLookup = [];
        personalPlayerOptions.innerHTML = "";
        resetPersonalResults();

        if (!manifest || !manifest.playersPath) {
            personalPlayerFilter.hidden = true;
            showPersonalMessage("No personal statistics are available in this export.", false);
            return;
        }

        showPersonalMessage("Loading eligible players...", false);
        fetch(dataUrl(manifest.playersPath))
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Player lookup data not found");
                }
                return response.json();
            })
            .then(function (data) {
                playerLookup = Array.isArray(data) ? data : [];
                populatePersonalPlayerOptions();
            })
            .catch(function () {
                personalPlayerFilter.hidden = true;
                resetPersonalResults();
                showPersonalMessage("Personal statistics player data could not be loaded.", true);
            });
    }

    function populatePersonalPlayerOptions() {
        personalPlayerOptions.innerHTML = "";
        resetPersonalCourseOptions();
        normalizePersonalMinCount();
        playerLookup.forEach(function (player) {
            var option = document.createElement("option");
            option.value = player.label;
            personalPlayerOptions.appendChild(option);
        });
        personalPlayerFilter.hidden = playerLookup.length === 0;
        if (playerLookup.length === 0) {
            showPersonalMessage("No eligible players are available in this statistics export.", false);
            return;
        }
        showPersonalMessage("Select a person to view statistics.", false);
    }

    function schedulePersonalPlayerInput() {
        if (personalPlayerInputTimer !== null) {
            clearTimeout(personalPlayerInputTimer);
        }
        personalPlayerInputTimer = setTimeout(function () {
            personalPlayerInputTimer = null;
            handlePersonalPlayerInput();
        }, 0);
    }

    function handlePersonalPlayerInput() {
        selectedPersonalRequest++;
        var player = selectedPersonalPlayer();
        selectedPersonalSnapshot = null;
        resetPersonalCourseOptions();
        personalTableBody.innerHTML = "";
        personalTableWrap.hidden = true;
        personalContent.hidden = true;
        personalPuttingSummary.innerHTML = "";

        if (personalPlayerInput.value.trim() === "") {
            showPersonalMessage("Select a person to view statistics.", false);
            return;
        }

        if (!player) {
            showPersonalMessage("Select a person from the autocomplete list.", false);
            return;
        }

        loadSelectedPersonalStats(player);
    }

    function selectedPersonalPlayer() {
        var value = personalPlayerInput.value.trim();
        for (var index = 0; index < playerLookup.length; index++) {
            if (playerLookup[index].label === value) {
                return playerLookup[index];
            }
        }
        return null;
    }

    function loadSelectedPersonalStats(player) {
        var requestId = ++selectedPersonalRequest;
        showPersonalMessage("Loading personal statistics...", false);
        fetch(dataUrl(player.path))
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Personal statistics data not found");
                }
                return response.json();
            })
            .then(function (data) {
                if (requestId !== selectedPersonalRequest) {
                    return;
                }
                selectedPersonalSnapshot = data;
                populatePersonalCourseOptions();
                personalContent.hidden = false;
                setPersonalView("putting");
                renderPersonalStatistics();
            })
            .catch(function () {
                if (requestId !== selectedPersonalRequest) {
                    return;
                }
                resetPersonalResults();
                showPersonalMessage("Personal statistics for the selected player could not be loaded.", true);
            });
    }

    function renderPersonalStatistics() {
        if (!selectedPersonalSnapshot) {
            personalTableBody.innerHTML = "";
            personalTableWrap.hidden = true;
            if (personalPlayerInput.value.trim() === "") {
                showPersonalMessage("Select a person to view statistics.", false);
            }
            return;
        }

        if (personalView === "putting") {
            renderPuttingStatistics();
            return;
        }

        var rows = selectedPersonalSnapshot && Array.isArray(selectedPersonalSnapshot.variations)
            ? selectedPersonalSnapshot.variations
            : [];
        var visibleRows = personalFilteredRows(rows);
        personalTableBody.innerHTML = "";

        if (rows.length === 0) {
            personalTableWrap.hidden = true;
            showPersonalMessage("No personal basket variation results are available for the selected player.", false);
            return;
        }

        if (visibleRows.length === 0) {
            personalTableWrap.hidden = true;
            showPersonalMessage("No personal basket variation results match the selected filters.", false);
            return;
        }

        visibleRows.forEach(appendPersonalVariationRow);
        hidePersonalMessage();
        personalTableWrap.hidden = false;
    }

    function setPersonalView(view) {
        personalView = view === "putting" ? "putting" : "hole";
        var holeActive = personalView === "hole";
        personalHoleTab.classList.toggle("active", holeActive);
        personalPuttingTab.classList.toggle("active", !holeActive);
        personalHoleView.hidden = !holeActive;
        personalPuttingView.hidden = holeActive;
        personalHoleFilters.hidden = !holeActive || !selectedPersonalSnapshot;
        if (selectedPersonalSnapshot) {
            renderPersonalStatistics();
        }
    }

    function renderPuttingStatistics() {
        personalPuttingSummary.innerHTML = "";
        var putting = selectedPersonalSnapshot && selectedPersonalSnapshot.putting
            ? selectedPersonalSnapshot.putting
            : {};

        var totals = document.createElement("div");
        totals.className = "putting-total";
        totals.appendChild(puttingLine("C1x", putting.c1x));
        totals.appendChild(puttingLine("C2", putting.c2));
        personalPuttingSummary.appendChild(totals);

        appendPuttingHighlights(personalPuttingSummary, putting);

        var years = Array.isArray(putting.years)
            ? putting.years.filter(function (year) {
                return year
                    && Number.isFinite(Number(year.year))
                    && yearHasCompleteRound(year);
            })
            : [];
        years.forEach(function (year, index) {
            appendPuttingYear(personalPuttingSummary, year, index);
        });
        hidePersonalMessage();
    }

    function yearHasCompleteRound(year) {
        return Array.isArray(year.rounds)
            && year.rounds.some(function (round) {
                return round && round.complete === true;
            });
    }

    function appendPuttingHighlights(container, putting) {
        var global = document.createElement("div");
        global.className = "putting-global";
        if (Number.isFinite(Number(putting.c2PlusPerRound))) {
            global.appendChild(puttingValueLine("C2+ per round", Number(putting.c2PlusPerRound).toFixed(2)));
        }
        if (putting.mostC2PlusInRound) {
            var mostC2Plus = putting.mostC2PlusInRound;
            var mostValue = Number.isFinite(Number(mostC2Plus.display))
                ? Number(mostC2Plus.display)
                : null;
            if (mostValue !== null) {
                var mostSuffix = Number(mostC2Plus.count) > 1
                    ? " (" + Number(mostC2Plus.count) + " times)"
                    : "";
                var mostDisplay = String(mostValue) + mostSuffix;
                global.appendChild(puttingValueLine(
                    "Most C2+ in a round",
                    mostDisplay,
                    puttingHighlightTooltip(mostC2Plus.rounds)
                ));
            }
        }
        if (putting.bestC1xRound) {
            var bestC1x = putting.bestC1xRound;
            var bestPercentage = bestC1x.percentage === null || bestC1x.percentage === undefined
                ? null
                : Number(bestC1x.percentage);
            if (Number.isFinite(bestPercentage)) {
                var bestSuffix = Number(bestC1x.count) > 1
                    ? " (" + Number(bestC1x.count) + " times)"
                    : "";
                var bestDisplay = bestPercentage.toFixed(2) + "% (" + Number(bestC1x.successful) + " / " + Number(bestC1x.total) + ")" + bestSuffix;
                global.appendChild(puttingValueLine(
                    "Best C1x round",
                    bestDisplay,
                    puttingHighlightTooltip(bestC1x.rounds)
                ));
            }
        }
        if (putting.longestThrowIn) {
            var longestThrowIn = putting.longestThrowIn;
            var longestDisplay = Number.isFinite(Number(longestThrowIn.display))
                ? Number(longestThrowIn.display)
                : null;
            if (longestDisplay !== null) {
                var longestSuffix = Number(longestThrowIn.count) > 1
                    ? " (" + Number(longestThrowIn.count) + " times)"
                    : "";
                var longestValue = String(longestDisplay) + " m" + longestSuffix;
                global.appendChild(puttingValueLine(
                    "Longest throw-in",
                    longestValue,
                    puttingLongestThrowInTooltip(longestThrowIn)
                ));
            }
        }
        if (Number.isFinite(Number(putting.threePlusPuttPerRound))) {
            global.appendChild(puttingValueLine(
                "Lost 3+putting per round",
                Number(putting.threePlusPuttPerRound).toFixed(2)
            ));
        }
        if (global.childNodes.length > 0) {
            container.appendChild(global);
        }
    }

    function appendPuttingYear(container, year, index) {
        var yearSection = document.createElement("section");
        yearSection.className = "putting-year";

        var yearId = "personal-putting-year-" + String(year.year) + "-" + String(index);
        var toggle = document.createElement("button");
        toggle.className = "putting-year-toggle";
        toggle.type = "button";
        toggle.textContent = String(year.year);
        toggle.setAttribute("aria-expanded", "false");
        toggle.setAttribute("aria-controls", yearId);

        var content = document.createElement("div");
        content.className = "putting-year-content";
        content.id = yearId;
        content.hidden = true;

        var totals = document.createElement("div");
        totals.className = "putting-total";
        totals.appendChild(puttingLine("C1x", year.c1x));
        totals.appendChild(puttingLine("C2", year.c2));
        content.appendChild(totals);
        appendPuttingHighlights(content, year);

        var rounds = Array.isArray(year.rounds)
            ? year.rounds.filter(displayablePuttingRound)
            : [];
        appendPuttingRoundDetails(content, rounds, "personal-putting-year-round-details-" + String(year.year) + "-" + String(index));

        toggle.addEventListener("click", function () {
            var expanded = toggle.getAttribute("aria-expanded") === "true";
            toggle.setAttribute("aria-expanded", String(!expanded));
            content.hidden = expanded;
        });

        yearSection.appendChild(toggle);
        yearSection.appendChild(content);
        container.appendChild(yearSection);
    }

    function appendPuttingRoundDetails(container, rounds, detailsId) {
        if (rounds.length === 0) {
            return;
        }

        var toggle = document.createElement("button");
        toggle.className = "putting-round-toggle";
        toggle.type = "button";
        toggle.textContent = "Show round details";
        toggle.setAttribute("aria-expanded", "false");
        toggle.setAttribute("aria-controls", detailsId);

        var details = document.createElement("div");
        details.className = "putting-round-details";
        details.id = detailsId;
        details.hidden = true;
        rounds.forEach(function (round) {
            details.appendChild(puttingRoundLine(round));
        });

        toggle.addEventListener("click", function () {
            var expanded = toggle.getAttribute("aria-expanded") === "true";
            toggle.setAttribute("aria-expanded", String(!expanded));
            toggle.textContent = expanded ? "Show round details" : "Hide round details";
            details.hidden = expanded;
        });

        container.appendChild(toggle);
        container.appendChild(details);
    }

    function puttingLine(label, metric) {
        var line = document.createElement("div");
        var successful = metric && Number.isFinite(Number(metric.successful)) ? Number(metric.successful) : 0;
        var total = metric && Number.isFinite(Number(metric.total)) ? Number(metric.total) : 0;
        var percentage = metric && metric.percentage !== null && metric.percentage !== undefined
            ? Number(metric.percentage).toFixed(2) + "%"
            : "—";
        var labelElement = document.createElement("strong");
        labelElement.className = "putting-metric-label";
        appendPuttingMetricLabel(labelElement, label);
        line.appendChild(labelElement);
        line.appendChild(document.createTextNode(": " + percentage + " (" + successful + " / " + total + ")"));
        return line;
    }

    function puttingValueLine(label, value, tooltipText) {
        var line = document.createElement("div");
        var labelElement = document.createElement("strong");
        labelElement.className = "putting-metric-label";
        appendPuttingMetricLabel(labelElement, label);
        line.appendChild(labelElement);
        line.appendChild(document.createTextNode(": "));
        if (tooltipText) {
            var valueElement = document.createElement("span");
            valueElement.className = "tooltip-trigger";
            valueElement.tabIndex = 0;
            valueElement.setAttribute("role", "button");
            valueElement.setAttribute("aria-label", label + ": " + value);
            valueElement.setAttribute("data-tooltip", tooltipText);
            valueElement.textContent = value;
            line.appendChild(valueElement);
        } else {
            line.appendChild(document.createTextNode(value));
        }
        return line;
    }

    function appendPuttingMetricLabel(container, label) {
        if (label === "C1x") {
            container.appendChild(puttingTooltipTrigger("C1x", PUTTING_METRIC_TOOLTIPS.c1x));
            return;
        }
        if (label === "C2") {
            container.appendChild(puttingTooltipTrigger("C2", PUTTING_METRIC_TOOLTIPS.c2));
            return;
        }
        if (label === "C2+ per round") {
            container.appendChild(puttingTooltipTrigger("C2+", PUTTING_METRIC_TOOLTIPS.c2Plus));
            container.appendChild(document.createTextNode(" per round"));
            return;
        }
        if (label === "Lost 3+putting per round") {
            container.appendChild(document.createTextNode("Lost "));
            container.appendChild(puttingTooltipTrigger("3+putting", PUTTING_METRIC_TOOLTIPS.threePlusPutting));
            container.appendChild(document.createTextNode(" per round"));
            return;
        }
        container.textContent = label;
    }

    function puttingTooltipTrigger(text, tooltipText) {
        var trigger = document.createElement("span");
        trigger.className = "tooltip-trigger";
        trigger.tabIndex = 0;
        trigger.setAttribute("role", "button");
        trigger.setAttribute("aria-label", text + ": " + tooltipText);
        trigger.setAttribute("data-tooltip", tooltipText);
        trigger.textContent = text;
        return trigger;
    }

    function puttingHighlightTooltip(rounds) {
        if (!Array.isArray(rounds) || rounds.length === 0) {
            return null;
        }
        var roundLines = rounds.map(function (round) {
            var competition = round && round.competitionSimpleName
                ? round.competitionSimpleName
                : "Competition";
            var roundLabel = round && round.roundAbbreviatedLabel
                ? round.roundAbbreviatedLabel
                : "Round";
            return competition + " " + roundLabel;
        });
        return roundLines.join("\n");
    }

    function puttingLongestThrowInTooltip(highlight) {
        if (!highlight || !Array.isArray(highlight.occurrences) || highlight.occurrences.length === 0) {
            return null;
        }
        var occurrenceLines = highlight.occurrences.map(function (occurrence) {
            var competition = occurrence && occurrence.competitionSimpleName
                ? occurrence.competitionSimpleName
                : "Competition";
            var roundLabel = occurrence && occurrence.roundAbbreviatedLabel
                ? occurrence.roundAbbreviatedLabel
                : "Round";
            var hole = occurrence && Number.isFinite(Number(occurrence.holeOrdinal))
                ? Number(occurrence.holeOrdinal)
                : "?";
            return competition + " " + roundLabel + " — Hole " + hole;
        });
        return occurrenceLines.join("\n");
    }

    function displayablePuttingRound(round) {
        if (!round || round.complete !== false) {
            return true;
        }
        return metricTotal(round.c1x) > 0
            || metricTotal(round.c2) > 0
            || threePlusPuttLostThrows(round) > 0;
    }

    function metricTotal(metric) {
        var total = metric ? Number(metric.total) : 0;
        return Number.isFinite(total) ? total : 0;
    }

    function threePlusPuttLostThrows(round) {
        var count = round ? Number(round.threePlusPuttLostThrows) : 0;
        return Number.isFinite(count) ? count : 0;
    }

    function puttingRoundLine(round) {
        var line = document.createElement("div");
        line.className = "putting-round-line";
        if (round.complete === false) {
            line.classList.add("incomplete");
        }

        var competition = document.createElement("strong");
        competition.textContent = round.competitionSimpleName || "Competition";
        line.appendChild(competition);
        line.appendChild(document.createTextNode(" "));

        var roundLabel = document.createElement("strong");
        roundLabel.textContent = round.roundAbbreviatedLabel || "Round";
        line.appendChild(roundLabel);
        line.appendChild(document.createTextNode(": "));
        line.appendChild(puttingLine("C1x", round.c1x));
        line.appendChild(document.createTextNode(", "));
        line.appendChild(puttingLine("C2", round.c2));
        var threePlusPuttCount = threePlusPuttLostThrows(round);
        if (threePlusPuttCount > 0) {
            line.appendChild(document.createTextNode(", "));
            line.appendChild(puttingValueLine("3+putts", String(threePlusPuttCount)));
        }
        return line;
    }

    function populatePersonalCourseOptions() {
        var rows = selectedPersonalSnapshot && Array.isArray(selectedPersonalSnapshot.variations)
            ? selectedPersonalSnapshot.variations
            : [];
        var coursesById = {};
        resetPersonalCourseOptions();

        rows.forEach(function (variation) {
            var courseId = variation.basketCourseId;
            if (courseId === null || courseId === undefined || coursesById[String(courseId)]) {
                return;
            }
            coursesById[String(courseId)] = selectedManifestCourse(courseId) || {
                id: courseId,
                name: variation.basketCourseName || String(courseId),
                sampleCount: 0
            };
        });

        Object.keys(coursesById)
            .map(function (courseId) {
                return coursesById[courseId];
            })
            .sort(compareCourses)
            .forEach(function (course) {
                var option = document.createElement("option");
                option.value = String(course.id);
                option.textContent = course.name;
                personalCourseSelect.appendChild(option);
            });
    }

    function resetPersonalCourseOptions() {
        personalCourseSelect.innerHTML = "";
        var option = document.createElement("option");
        option.value = "";
        option.textContent = "All courses";
        personalCourseSelect.appendChild(option);
    }

    function personalFilteredRows(rows) {
        var selectedCourseId = personalCourseSelect.value;
        var minCount = effectivePersonalMinCount();
        return rows.filter(function (variation) {
            var courseMatches = selectedCourseId === "" || String(variation.basketCourseId) === selectedCourseId;
            return courseMatches && Number(variation.count) >= minCount;
        }).sort(comparePersonalRows);
    }

    function effectivePersonalMinCount() {
        var minCount = Number(personalMinCountInput.value);
        if (!Number.isFinite(minCount) || minCount < 2) {
            return 2;
        }
        return Math.floor(minCount);
    }

    function normalizePersonalMinCount() {
        var minCount = effectivePersonalMinCount();
        personalMinCountInput.value = String(minCount);
        return minCount;
    }

    function appendPersonalVariationRow(variation) {
        var row = document.createElement("tr");
        row.className = "variation-row";
        appendCell(row, variation.basketCourseName);
        appendCell(row, variation.basketLabel);
        appendCell(row, variation.variationLabel);
        appendCell(row, String(variation.displayRating));
        appendCell(row, String(variation.count));
        appendCell(row, personalScoresText(variation.scores));
        personalTableBody.appendChild(row);
    }

    function personalScoresText(scores) {
        if (!Array.isArray(scores)) {
            return "";
        }
        return scores.join(", ");
    }

    function dataUrl(path) {
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
        return selectedBasketStatsSnapshot.variations.slice().sort(compareBasketStatsVariations);
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
                    sortOrder: sample.basketSortOrder,
                    variationsById: new Map()
                });
            }
            var basket = basketsById.get(sample.basketId);
            if (!basket.variationsById.has(sample.variationId)) {
                basket.variationsById.set(sample.variationId, {
                    id: sample.variationId,
                    label: sample.variationLabel,
                    sortOrder: sample.variationSortOrder,
                    samples: [],
                    scores: []
                });
            }
            basket.variationsById.get(sample.variationId).samples.push(sample);
            basket.variationsById.get(sample.variationId).scores.push(sample.score);
        });

        return Array.from(basketsById.values())
            .sort(compareBaskets)
            .map(function (basket) {
                basket.variations = Array.from(basket.variationsById.values())
                    .filter(function (variation) {
                        return variation.scores.length > 0;
                    })
                    .sort(compareVariations)
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

    function sortedCourses(courses) {
        return (Array.isArray(courses) ? courses : []).slice().sort(compareCourses);
    }

    function selectedManifestCourse(courseId) {
        var courses = Array.isArray(manifest && manifest.courses) ? manifest.courses : [];
        for (var index = 0; index < courses.length; index++) {
            if (String(courses[index].id) === String(courseId)) {
                return courses[index];
            }
        }
        return null;
    }

    function compareCourses(left, right) {
        var countComparison = Number(right.sampleCount || 0) - Number(left.sampleCount || 0);
        if (countComparison !== 0) {
            return countComparison;
        }
        var nameComparison = String(left.name || "").localeCompare(String(right.name || ""));
        if (nameComparison !== 0) {
            return nameComparison;
        }
        return compareIds(left.id, right.id);
    }

    function compareBaskets(left, right) {
        return compareOrderedEntities(left, right, "sortOrder", "id");
    }

    function compareVariations(left, right) {
        return compareOrderedEntities(left, right, "sortOrder", "id");
    }

    function compareBasketStatsVariations(left, right) {
        var basketComparison = compareOrderedEntities(
            {id: left.basketId, sortOrder: left.basketSortOrder},
            {id: right.basketId, sortOrder: right.basketSortOrder},
            "sortOrder",
            "id"
        );
        if (basketComparison !== 0) {
            return basketComparison;
        }
        return compareOrderedEntities(
            {id: left.variationId, sortOrder: left.variationSortOrder},
            {id: right.variationId, sortOrder: right.variationSortOrder},
            "sortOrder",
            "id"
        );
    }

    function comparePersonalRows(left, right) {
        var ratingComparison = Number(right.rating) - Number(left.rating);
        if (Number.isFinite(ratingComparison) && ratingComparison !== 0) {
            return ratingComparison;
        }
        var courseComparison = compareCourses(
            selectedManifestCourse(left.basketCourseId) || {
                id: left.basketCourseId,
                name: left.basketCourseName,
                sampleCount: 0
            },
            selectedManifestCourse(right.basketCourseId) || {
                id: right.basketCourseId,
                name: right.basketCourseName,
                sampleCount: 0
            }
        );
        if (courseComparison !== 0) {
            return courseComparison;
        }
        var basketComparison = compareOrderedEntities(left, right, "basketSortOrder", "basketId");
        if (basketComparison !== 0) {
            return basketComparison;
        }
        return compareOrderedEntities(left, right, "variationSortOrder", "variationId");
    }

    function compareOrderedEntities(left, right, orderField, idField) {
        var leftOrder = numericSortOrder(left[orderField]);
        var rightOrder = numericSortOrder(right[orderField]);
        if (leftOrder !== null && rightOrder === null) {
            return -1;
        }
        if (leftOrder === null && rightOrder !== null) {
            return 1;
        }
        if (leftOrder !== null && rightOrder !== null && leftOrder !== rightOrder) {
            return leftOrder - rightOrder;
        }
        return compareIds(left[idField], right[idField]);
    }

    function numericSortOrder(value) {
        if (value === null || value === undefined || value === "") {
            return null;
        }
        var parsed = Number(value);
        return Number.isFinite(parsed) ? parsed : null;
    }

    function compareIds(left, right) {
        return Number(left) - Number(right);
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
                var basketComparison = compareBaskets(left.basket, right.basket);
                if (basketComparison !== 0) {
                    return basketComparison;
                }
                return compareVariations(left, right);
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
                    sortOrder: sample.variationSortOrder,
                    basket: {
                        id: sample.basketId,
                        label: sample.basketLabel,
                        sortOrder: sample.basketSortOrder
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
            sortOrder: group.sortOrder,
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
        drawScoreDistributionChart(variation, windows, xDomain);
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

    function drawScoreDistributionChart(variation, windows, xDomain) {
        var buckets = Array.isArray(variation.scoreBuckets) ? variation.scoreBuckets.filter(function (bucket) {
            return BUCKETS.indexOf(bucket) >= 0;
        }) : [];
        if (buckets.length === 0 || !windows.some(function (window) {
            return window.scoreDistribution && typeof window.scoreDistribution === "object";
        })) {
            scoreDistributionSection.hidden = true;
            scoreDistributionSvg.innerHTML = "";
            scoreDistributionLegend.innerHTML = "";
            scoreDistributionPoints = [];
            hideScoreDistributionTooltip();
            return;
        }

        scoreDistributionSection.hidden = false;
        var metrics = chartMetrics(scoreDistributionCanvas, {top: 24, right: 24, bottom: 58, left: 58}, 360, 300);
        scoreDistributionPoints = [];
        scoreDistributionVisibility = {};
        buckets.forEach(function (bucket) {
            var overall = variation.overallScorePercentages && isNumeric(variation.overallScorePercentages[bucket])
                ? Number(variation.overallScorePercentages[bucket])
                : 0;
            scoreDistributionVisibility[bucket] = overall > 5;
        });
        scoreDistributionSvg.innerHTML = "";
        hideScoreDistributionTooltip();
        scoreDistributionSvg.setAttribute("viewBox", "0 0 " + metrics.width + " " + metrics.height);
        scoreDistributionSvg.setAttribute("width", String(metrics.width));
        scoreDistributionSvg.setAttribute("height", String(metrics.height));
        drawScoreDistributionGrid(metrics, xDomain);
        renderScoreDistributionSeries(windows, buckets, xDomain, metrics);
        renderScoreDistributionLegend(variation, windows, buckets, xDomain, metrics);
    }

    function drawScoreDistributionGrid(metrics, xDomain) {
        var yTicks = [0, 25, 50, 75, 100];
        yTicks.forEach(function (tick) {
            var y = scale(tick, [0, 100], metrics.bottom, metrics.top);
            appendSvg(scoreDistributionSvg, "line", {class: "chart-grid", x1: metrics.left, y1: y, x2: metrics.right, y2: y});
            appendSvg(scoreDistributionSvg, "text", {class: "chart-tick-label", x: metrics.left - 8, y: y + 4, "text-anchor": "end"}, tick + "%");
        });
        ratingTicks(xDomain).forEach(function (tick) {
            var x = scale(tick, xDomain, metrics.left, metrics.right);
            appendSvg(scoreDistributionSvg, "line", {class: "chart-grid", x1: x, y1: metrics.top, x2: x, y2: metrics.bottom});
            appendSvg(scoreDistributionSvg, "text", {class: "chart-tick-label", x: x, y: metrics.bottom + 20, "text-anchor": "middle"}, String(Math.round(tick)));
        });
        appendSvg(scoreDistributionSvg, "line", {class: "chart-axis", x1: metrics.left, y1: metrics.bottom, x2: metrics.right, y2: metrics.bottom});
        appendSvg(scoreDistributionSvg, "line", {class: "chart-axis", x1: metrics.left, y1: metrics.top, x2: metrics.left, y2: metrics.bottom});
        appendSvg(scoreDistributionSvg, "text", {class: "chart-axis-label", x: metrics.left + metrics.plotWidth / 2, y: metrics.height - 16, "text-anchor": "middle"}, "Rating");
        appendSvg(scoreDistributionSvg, "text", {
            class: "chart-axis-label",
            x: 18,
            y: metrics.top + metrics.plotHeight / 2,
            "text-anchor": "middle",
            transform: "rotate(-90 18 " + (metrics.top + metrics.plotHeight / 2) + ")"
        }, "Score percentage");
    }

    function renderScoreDistributionSeries(windows, buckets, xDomain, metrics) {
        buckets.forEach(function (bucket) {
            if (!scoreDistributionVisibility[bucket]) {
                return;
            }
            for (var index = 0; index < windows.length - 1; index++) {
                var current = windows[index];
                var next = windows[index + 1];
                var currentValue = current.scoreDistribution && current.scoreDistribution[bucket];
                var nextValue = next.scoreDistribution && next.scoreDistribution[bucket];
                if (!isNumeric(currentValue) || !isNumeric(nextValue)) {
                    continue;
                }
                var x1 = scale(current.ratingMidpoint, xDomain, metrics.left, metrics.right);
                var y1 = scale(currentValue, [0, 100], metrics.bottom, metrics.top);
                var x2 = scale(next.ratingMidpoint, xDomain, metrics.left, metrics.right);
                var y2 = scale(nextValue, [0, 100], metrics.bottom, metrics.top);
                if (current.sprwCountBucket === next.sprwCountBucket) {
                    appendScoreDistributionLine(x1, y1, x2, y2, bucket, current.sprwCountBucket);
                } else {
                    appendScoreDistributionLine(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2, bucket, current.sprwCountBucket);
                    appendScoreDistributionLine((x1 + x2) / 2, (y1 + y2) / 2, x2, y2, bucket, next.sprwCountBucket);
                }
            }
            windows.forEach(function (window) {
                var value = window.scoreDistribution && window.scoreDistribution[bucket];
                if (!isNumeric(value)) {
                    return;
                }
                var point = {
                    bucket: bucket,
                    window: window,
                    value: value,
                    x: scale(window.ratingMidpoint, xDomain, metrics.left, metrics.right),
                    y: scale(value, [0, 100], metrics.bottom, metrics.top)
                };
                scoreDistributionPoints.push(point);
                appendSvg(scoreDistributionSvg, "circle", {
                    class: "score-distribution-series score-distribution-point score-bucket-" + cssToken(bucket),
                    cx: point.x,
                    cy: point.y,
                    r: 4,
                    "data-bucket": bucket,
                    "data-midpoint": window.ratingMidpoint
                });
            });
        });
    }

    function appendScoreDistributionLine(x1, y1, x2, y2, bucket, countBucket) {
        if (countBucket === "50-99") {
            return;
        }
        appendSvg(scoreDistributionSvg, "line", {
            class: "score-distribution-series score-distribution-line score-bucket-" + cssToken(bucket) + " " + bucketClass(countBucket),
            x1: x1,
            y1: y1,
            x2: x2,
            y2: y2
        });
    }

    function renderScoreDistributionLegend(variation, windows, buckets, xDomain, metrics) {
        scoreDistributionLegend.innerHTML = "";
        buckets.forEach(function (bucket) {
            var toggle = document.createElement("button");
            toggle.type = "button";
            toggle.className = "legend-item score-distribution-toggle";
            toggle.setAttribute("aria-pressed", String(scoreDistributionVisibility[bucket]));
            toggle.setAttribute("data-bucket", bucket);
            var swatch = document.createElement("span");
            swatch.className = "legend-swatch score-bucket-" + cssToken(bucket);
            swatch.setAttribute("aria-hidden", "true");
            toggle.appendChild(swatch);
            toggle.appendChild(document.createTextNode(bucket));
            toggle.addEventListener("click", function () {
                scoreDistributionVisibility[bucket] = !scoreDistributionVisibility[bucket];
                toggle.setAttribute("aria-pressed", String(scoreDistributionVisibility[bucket]));
                scoreDistributionSvg.querySelectorAll(".score-distribution-series").forEach(function (element) {
                    element.remove();
                });
                scoreDistributionPoints = [];
                renderScoreDistributionSeries(windows, buckets, xDomain, metrics);
            });
            scoreDistributionLegend.appendChild(toggle);
        });
    }

    function cssToken(bucket) {
        return bucket.replace("+", "plus").replace("-", "to");
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

    function handleScoreDistributionPointer(event) {
        if (scoreDistributionPoints.length === 0) {
            hideScoreDistributionTooltip();
            return;
        }
        var svgPoint = pointerToSvgPoint(scoreDistributionSvg, event);
        var matches = scoreDistributionPoints.filter(function (point) {
            return pointDistance(point, svgPoint) <= CHART_HIT_RADIUS;
        });
        if (matches.length === 0) {
            hideScoreDistributionTooltip();
            return;
        }
        showScoreDistributionTooltip(matches, event);
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

    function handleUiTooltipPointerOver(event) {
        var trigger = event.target.closest ? event.target.closest(".tooltip-trigger") : null;
        if (trigger) {
            showUiTooltip(trigger);
        }
    }

    function handleUiTooltipPointerOut(event) {
        var trigger = event.target.closest ? event.target.closest(".tooltip-trigger") : null;
        if (trigger && (!event.relatedTarget || !trigger.contains(event.relatedTarget))) {
            if (document.activeElement !== trigger) {
                hideUiTooltip();
            }
        }
    }

    function handleUiTooltipFocusIn(event) {
        var trigger = event.target.closest ? event.target.closest(".tooltip-trigger") : null;
        if (trigger) {
            showUiTooltip(trigger);
        }
    }

    function handleUiTooltipFocusOut(event) {
        var trigger = event.target.closest ? event.target.closest(".tooltip-trigger") : null;
        if (trigger && (!event.relatedTarget || !trigger.contains(event.relatedTarget))) {
            hideUiTooltip();
        }
    }

    function showUiTooltip(trigger) {
        var text = trigger.getAttribute("data-tooltip");
        if (!text) {
            return;
        }
        activeUiTooltipTrigger = trigger;
        uiTooltip.textContent = text;
        uiTooltip.hidden = false;
        positionUiTooltip(trigger);
    }

    function positionUiTooltip(trigger) {
        var triggerBounds = trigger.getBoundingClientRect();
        var tooltipBounds = uiTooltip.getBoundingClientRect();
        var margin = 12;
        var gap = 8;
        var left = triggerBounds.left + (triggerBounds.width - tooltipBounds.width) / 2;
        var top = triggerBounds.bottom + gap;

        left = Math.max(margin, Math.min(left, window.innerWidth - tooltipBounds.width - margin));
        if (top + tooltipBounds.height > window.innerHeight - margin) {
            top = triggerBounds.top - tooltipBounds.height - gap;
        }
        top = Math.max(margin, top);
        uiTooltip.style.left = left + "px";
        uiTooltip.style.top = top + "px";
    }

    function hideUiTooltip() {
        activeUiTooltipTrigger = null;
        uiTooltip.hidden = true;
        uiTooltip.textContent = "";
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

    function showScoreDistributionTooltip(points, event) {
        scoreDistributionTooltip.innerHTML = "";
        points.forEach(function (point) {
            var row = document.createElement("div");
            row.className = "chart-tooltip-row";
            row.textContent = point.bucket
                + " | Rating " + point.window.ratingMidpoint
                + " | " + Number(point.value).toFixed(1) + "%";
            scoreDistributionTooltip.appendChild(row);
        });
        positionTooltip(scoreDistributionTooltip, scoreDistributionCanvas, event);
        scoreDistributionTooltip.hidden = false;
        markScoreDistributionHoveredPoints(points);
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

    function hideScoreDistributionTooltip() {
        scoreDistributionTooltip.hidden = true;
        scoreDistributionTooltip.innerHTML = "";
        Array.from(scoreDistributionSvg.querySelectorAll(".score-distribution-point")).forEach(function (element) {
            element.classList.remove("hovered");
        });
    }

    function markScoreDistributionHoveredPoints(points) {
        var hoveredKeys = new Set(points.map(function (point) {
            return point.bucket + ":" + point.window.ratingMidpoint;
        }));
        Array.from(scoreDistributionSvg.querySelectorAll(".score-distribution-point")).forEach(function (element) {
            var key = element.getAttribute("data-bucket") + ":" + element.getAttribute("data-midpoint");
            element.classList.toggle("hovered", hoveredKeys.has(key));
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
        scoreDistributionSection.hidden = true;
        basketChartStatus.textContent = "";
        basketChartSvg.innerHTML = "";
        scoreDistributionSvg.innerHTML = "";
        scoreDistributionLegend.innerHTML = "";
        scoreDistributionPoints = [];
        hideBasketChartTooltip();
        hideScoreDistributionTooltip();
    }

    function resetPersonalResults() {
        selectedPersonalSnapshot = null;
        personalView = "putting";
        resetPersonalCourseOptions();
        normalizePersonalMinCount();
        personalContent.hidden = true;
        personalHoleFilters.hidden = true;
        personalHoleView.hidden = true;
        personalPuttingView.hidden = false;
        personalHoleTab.classList.remove("active");
        personalPuttingTab.classList.add("active");
        personalTableWrap.hidden = true;
        personalTableBody.innerHTML = "";
        personalPuttingSummary.innerHTML = "";
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

    function showPersonalMessage(text, isError) {
        personalMessage.textContent = text;
        personalMessage.classList.toggle("error", isError);
        personalMessage.hidden = false;
    }

    function hidePersonalMessage() {
        personalMessage.textContent = "";
        personalMessage.classList.remove("error");
        personalMessage.hidden = true;
    }
})();
