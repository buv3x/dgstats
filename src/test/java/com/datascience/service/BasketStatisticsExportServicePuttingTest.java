package com.datascience.service;

import com.datascience.repository.RoundResultThrowsRepository.PuttingThrowRow;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketStatisticsExportServicePuttingTest {

    @Test
    void unsuccessfulZoneThreeAttemptsCountWithoutQualifyingDistance() throws Exception {
        Object builder = newBuilder();
        Method add = builder.getClass().getDeclaredMethod("add", PuttingThrowRow.class, boolean.class);
        add.setAccessible(true);

        add.invoke(builder, row(3L, null, null), false);
        add.invoke(builder, row(3L, 8, null), false);
        add.invoke(builder, row(3L, 40, null), false);

        Object c1x = metric(builder, "c1x");
        assertEquals(0, metricValue(c1x, "successful"));
        assertEquals(3, metricValue(c1x, "total"));
    }

    @Test
    void successfulZoneThreeStillRequiresC1xDistance() throws Exception {
        Object builder = newBuilder();
        Method add = builder.getClass().getDeclaredMethod("add", PuttingThrowRow.class, boolean.class);
        add.setAccessible(true);

        add.invoke(builder, row(3L, 12, null), true);
        add.invoke(builder, row(3L, 8, null), true);
        add.invoke(builder, row(3L, null, null), true);

        Object c1x = metric(builder, "c1x");
        assertEquals(1, metricValue(c1x, "successful"));
        assertEquals(1, metricValue(c1x, "total"));
    }

    @Test
    void zoneFourAndDistanceFallbackRemainUnchanged() throws Exception {
        Object builder = newBuilder();
        Method add = builder.getClass().getDeclaredMethod("add", PuttingThrowRow.class, boolean.class);
        add.setAccessible(true);

        add.invoke(builder, row(4L, null, null), true);
        add.invoke(builder, row(4L, null, null), false);
        add.invoke(builder, row(2L, 20, null), true);
        add.invoke(builder, row(2L, null, 40), false);

        Object c1x = metric(builder, "c1x");
        assertEquals(1, metricValue(c1x, "successful"));
        assertEquals(1, metricValue(c1x, "total"));

        Object c2 = metric(builder, "c2");
        assertEquals(1, metricValue(c2, "successful"));
        assertEquals(3, metricValue(c2, "total"));
    }

    @Test
    void dropZoneFourOverridesC1xDistance() throws Exception {
        Object builder = newBuilder();
        Method add = builder.getClass().getDeclaredMethod("add", PuttingThrowRow.class, boolean.class);
        add.setAccessible(true);

        add.invoke(builder, row(2L, 20, null, 4L), true);

        Object c1x = metric(builder, "c1x");
        Object c2 = metric(builder, "c2");
        assertEquals(0, metricValue(c1x, "total"));
        assertEquals(1, metricValue(c2, "successful"));
        assertEquals(1, metricValue(c2, "total"));
    }

    @Test
    void dropZoneThreeUsesZoneThreeBullseyeRules() throws Exception {
        Object builder = newBuilder();
        Method add = builder.getClass().getDeclaredMethod("add", PuttingThrowRow.class, boolean.class);
        add.setAccessible(true);

        add.invoke(builder, row(2L, 8, null, 3L), true);
        add.invoke(builder, row(2L, 8, null, 3L), false);

        Object c1x = metric(builder, "c1x");
        assertEquals(0, metricValue(c1x, "successful"));
        assertEquals(1, metricValue(c1x, "total"));
    }

    @Test
    void dropZoneRecordUsesDropDistanceInAnOrderedHole() throws Exception {
        Map<?, ?> players = buildPuttingPlayers(List.of(
                row(401L, 4L, 40L, 1, 1, 2L, null, 40, 4L, 1),
                row(401L, 4L, 40L, 1, 2, 5L, null, null, null, 1)
        ));

        Object summary = invoke(players.get(4L), "toSummary");
        Object c2 = invoke(summary, "c2");
        assertEquals(1, metricValue(c2, "successful"));
        assertEquals(1, metricValue(c2, "total"));
    }

    @Test
    void incompleteRoundRetainsRoundMetricsButExcludesThemFromPlayerTotals() throws Exception {
        Map<?, ?> players = buildPuttingPlayers(List.of(
                row(101L, 1L, 10L, 1, 1, 3L, 20, null, 2),
                row(101L, 1L, 10L, 1, 2, 5L, null, null, 2)
        ));

        Object builder = players.values().iterator().next();
        Object summary = invoke(builder, "toSummary");
        Object total = invoke(summary, "c1x");
        Object year = ((List<?>) invoke(summary, "years")).get(0);
        Object round = ((List<?>) invoke(year, "rounds")).get(0);

        assertEquals(0, metricValue(total, "total"));
        assertEquals(false, invoke(round, "complete"));
        assertEquals(1, metricValue(invoke(round, "c1x"), "total"));
    }

    @Test
    void completeAndEmptyRoundsAreDistinguishedPerPlayerRound() throws Exception {
        Map<?, ?> players = buildPuttingPlayers(List.of(
                row(201L, 1L, 20L, 1, 1, 3L, 20, null, 1),
                row(201L, 1L, 20L, 1, 2, 5L, null, null, 1),
                row(202L, 2L, 20L, null, null, null, null, null, 1)
        ));

        Object completePlayer = players.get(1L);
        Object incompletePlayer = players.get(2L);
        Object completeSummary = invoke(completePlayer, "toSummary");
        Object incompleteSummary = invoke(incompletePlayer, "toSummary");

        assertEquals(1, metricValue(invoke(completeSummary, "c1x"), "total"));
        Object completeYear = ((List<?>) invoke(completeSummary, "years")).get(0);
        assertEquals(true, invoke(((List<?>) invoke(completeYear, "rounds")).get(0), "complete"));
        assertEquals(0, metricValue(invoke(incompleteSummary, "c1x"), "total"));
        Object incompleteYear = ((List<?>) invoke(incompleteSummary, "years")).get(0);
        assertEquals(false, invoke(((List<?>) invoke(incompleteYear, "rounds")).get(0), "complete"));
    }

    @Test
    void invalidFinalZoneMarksRoundIncomplete() throws Exception {
        Map<?, ?> players = buildPuttingPlayers(List.of(
                row(301L, 3L, 30L, 1, 1, 3L, 20, null, 1),
                row(301L, 3L, 30L, 1, 2, 3L, 20, null, 1)
        ));

        Object summary = invoke(players.get(3L), "toSummary");
        Object year = ((List<?>) invoke(summary, "years")).get(0);
        Object round = ((List<?>) invoke(year, "rounds")).get(0);

        assertEquals(false, invoke(round, "complete"));
        assertEquals(0, metricValue(invoke(round, "c1x"), "total"));
        assertEquals(0, metricValue(invoke(summary, "c1x"), "total"));
    }

    @Test
    void yearlySummariesAreNewestFirstAndUndatedRoundsOnlyAffectGlobalTotals() throws Exception {
        Map<?, ?> players = buildPuttingPlayers(List.of(
                row(401L, 4L, 40L, 1, 1, 3L, 20, null, null, 1, LocalDate.of(2026, 6, 1)),
                row(401L, 4L, 40L, 1, 2, 5L, null, null, null, 1, LocalDate.of(2026, 6, 1)),
                row(402L, 4L, 41L, 1, 1, 3L, 20, null, null, 1, LocalDate.of(2025, 6, 1)),
                row(402L, 4L, 41L, 1, 2, 5L, null, null, null, 1, LocalDate.of(2025, 6, 1)),
                row(403L, 4L, 42L, 1, 1, 3L, 20, null, null, 1, null),
                row(403L, 4L, 42L, 1, 2, 5L, null, null, null, 1, null)
        ));

        Object summary = invoke(players.get(4L), "toSummary");
        List<?> years = (List<?>) invoke(summary, "years");

        assertEquals(3, metricValue(invoke(summary, "c1x"), "total"));
        assertEquals(2, years.size());
        assertEquals(2026, invoke(years.get(0), "year"));
        assertEquals(2025, invoke(years.get(1), "year"));
        assertEquals(1, metricValue(invoke(years.get(0), "c1x"), "total"));
        assertEquals(1, metricValue(invoke(years.get(1), "c1x"), "total"));
    }

    private static Object newBuilder() throws Exception {
        Class<?> type = Class.forName("com.datascience.service.BasketStatisticsExportService$PuttingMetricsBuilder");
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private static Object metric(Object builder, String name) throws Exception {
        Method method = builder.getClass().getDeclaredMethod(name.equals("c1x") ? "toC1xMetric" : "toC2Metric");
        method.setAccessible(true);
        return method.invoke(builder);
    }

    private static int metricValue(Object metric, String name) throws Exception {
        return (int) metric.getClass().getMethod(name).invoke(metric);
    }

    private static Map<?, ?> buildPuttingPlayers(List<PuttingThrowRow> rows) throws Exception {
        Method method = BasketStatisticsExportService.class.getDeclaredMethod("buildPuttingPlayers", List.class);
        method.setAccessible(true);
        return (Map<?, ?>) method.invoke(null, rows);
    }

    private static Object invoke(Object target, String methodName) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(target);
    }

    private static PuttingThrowRow row(
            Long roundResultId,
            Long playerId,
            Long roundId,
            Integer holeOrdinal,
            Integer scoreOrdinal,
            Long zoneId,
            Integer distance,
            Integer dropDistance,
            Integer expectedHoleCount
    ) {
        return row(
                roundResultId,
                playerId,
                roundId,
                holeOrdinal,
                scoreOrdinal,
                zoneId,
                distance,
                dropDistance,
                null,
                expectedHoleCount,
                LocalDate.of(2026, 1, 1)
        );
    }

    private static PuttingThrowRow row(
            Long roundResultId,
            Long playerId,
            Long roundId,
            Integer holeOrdinal,
            Integer scoreOrdinal,
            Long zoneId,
            Integer distance,
            Integer dropDistance,
            Long dropZoneId,
            Integer expectedHoleCount
    ) {
        return row(
                roundResultId,
                playerId,
                roundId,
                holeOrdinal,
                scoreOrdinal,
                zoneId,
                distance,
                dropDistance,
                dropZoneId,
                expectedHoleCount,
                LocalDate.of(2026, 1, 1)
        );
    }

    private static PuttingThrowRow row(
            Long roundResultId,
            Long playerId,
            Long roundId,
            Integer holeOrdinal,
            Integer scoreOrdinal,
            Long zoneId,
            Integer distance,
            Integer dropDistance,
            Long dropZoneId,
            Integer expectedHoleCount,
            LocalDate roundDate
    ) {
        return (PuttingThrowRow) Proxy.newProxyInstance(
                PuttingThrowRow.class.getClassLoader(),
                new Class<?>[]{PuttingThrowRow.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getRoundResultId" -> roundResultId;
                    case "getPlayerId" -> playerId;
                    case "getPlayerName" -> "Player " + playerId;
                    case "getPlayerPdgaNum" -> playerId;
                    case "getRoundId" -> roundId;
                    case "getRoundDate" -> roundDate;
                    case "getCompetitionSimpleName" -> "Competition";
                    case "getRoundAbbreviatedLabel" -> "Round";
                    case "getHoleOrdinal" -> holeOrdinal;
                    case "getScoreOrdinal" -> scoreOrdinal;
                    case "getZoneId" -> zoneId;
                    case "getDropZoneId" -> dropZoneId;
                    case "getDistanceToTarget" -> distance;
                    case "getDropDistanceToTarget" -> dropDistance;
                    case "getExpectedHoleCount" -> expectedHoleCount;
                    default -> null;
                }
        );
    }

    private static PuttingThrowRow row(Long zoneId, Integer distance, Integer dropDistance) {
        return row(zoneId, distance, dropDistance, null);
    }

    private static PuttingThrowRow row(Long zoneId, Integer distance, Integer dropDistance, Long dropZoneId) {
        return (PuttingThrowRow) Proxy.newProxyInstance(
                PuttingThrowRow.class.getClassLoader(),
                new Class<?>[]{PuttingThrowRow.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getZoneId" -> zoneId;
                    case "getDropZoneId" -> dropZoneId;
                    case "getDistanceToTarget" -> distance;
                    case "getDropDistanceToTarget" -> dropDistance;
                    default -> null;
                }
        );
    }
}
