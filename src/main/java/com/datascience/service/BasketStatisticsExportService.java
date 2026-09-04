package com.datascience.service;

import com.datascience.repository.HoleScoreRepository;
import com.datascience.repository.HoleScoreRepository.StatisticsExportRow;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasketStatisticsExportService {

    private static final Path MANIFEST_PATH = Path.of("docs", "data", "statistics.json");
    private static final Path COURSES_DIRECTORY = Path.of("docs", "data", "courses");
    private static final Path BASKET_STATS_DIRECTORY = Path.of("docs", "data", "basket-stats");
    private static final Path PLAYERS_PATH = Path.of("docs", "data", "players.json");
    private static final Path PERSONAL_STATS_DIRECTORY = Path.of("docs", "data", "personal-stats");
    private static final int BASKET_STATS_WINDOW_SIZE = 50;
    private static final int BASKET_STATS_WINDOW_STEP = 5;
    private static final int BASKET_STATS_MIN_SAMPLES = 50;
    private static final int BASKET_STATS_SPRW_RADIUS = 50;
    private static final int PERSONAL_STATS_MIN_GLOBAL_SAMPLES = 50;
    private static final int PERSONAL_STATS_MIN_PLAYER_SAMPLES = 2;

    private final HoleScoreRepository holeScoreRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public ExportResult exportStatistics() {
        List<StatisticsExportRow> rows = holeScoreRepository.findStatisticsExportRows();
        Map<Long, CourseExportBuilder> coursesById = new LinkedHashMap<>();
        long ignoredUnratedSamples = 0;
        long ignoredUnmappedSamples = 0;
        long exportedSamples = 0;
        List<PersonalScoreSample> personalSamples = new ArrayList<>();

        for (StatisticsExportRow row : rows) {
            if (row.getRating() == null) {
                ignoredUnratedSamples++;
                continue;
            }
            if (row.getVariationId() == null || row.getBasketCourseId() == null) {
                ignoredUnmappedSamples++;
                continue;
            }

            CourseExportBuilder course = coursesById.computeIfAbsent(
                    row.getBasketCourseId(),
                    id -> new CourseExportBuilder(id, courseName(row))
            );
            course.competitionsById.putIfAbsent(
                    row.getCompetitionId(),
                    new CompetitionDescriptor(row.getCompetitionId(), competitionName(row))
            );
            course.samples.add(new ScoreSample(
                    row.getBasketCourseId(),
                    row.getCompetitionId(),
                    row.getBasketId(),
                    basketLabel(row),
                    row.getVariationId(),
                    variationLabel(row),
                    row.getRating(),
                    row.getScore()
            ));
            personalSamples.add(new PersonalScoreSample(
                    row.getBasketCourseId(),
                    courseName(row),
                    row.getBasketId(),
                    basketLabel(row),
                    row.getVariationId(),
                    variationLabel(row),
                    row.getPlayerId(),
                    playerName(row),
                    row.getPlayerPdgaNum(),
                    row.getRoundId(),
                    row.getRoundDate(),
                    row.getRating(),
                    row.getScore()
            ));
            exportedSamples++;
        }

        List<CourseExportBuilder> courses = coursesById.values().stream()
                .sorted(Comparator.comparing(CourseExportBuilder::name, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(CourseExportBuilder::id))
                .toList();

        Path manifestPath = resolveManifestPath();
        Path coursesDirectory = resolveCoursesDirectory();
        Path basketStatsDirectory = resolveBasketStatsDirectory();
        Path playersPath = resolvePlayersPath();
        Path personalStatsDirectory = resolvePersonalStatsDirectory();
        List<CourseOption> courseOptions = new ArrayList<>();
        int generatedBasketStatsFiles = 0;

        for (CourseExportBuilder course : courses) {
            String relativePath = courseRelativePath(course.id());
            CourseSnapshot snapshot = course.toSnapshot();
            BasketStatsSnapshot basketStatsSnapshot = course.toBasketStatsSnapshot();
            String basketStatsPath = null;
            writeJson(snapshot, coursesDirectory.resolve(courseFileName(course.id())));
            if (!basketStatsSnapshot.variations().isEmpty()) {
                basketStatsPath = basketStatsRelativePath(course.id());
                writeJson(basketStatsSnapshot, basketStatsDirectory.resolve(courseFileName(course.id())));
                generatedBasketStatsFiles++;
            }
            courseOptions.add(new CourseOption(
                    course.id(),
                    course.name(),
                    course.samples.size(),
                    relativePath,
                    basketStatsPath
            ));
        }

        PersonalStatisticsExport personalStatisticsExport = buildPersonalStatisticsExport(personalSamples);
        writeJson(personalStatisticsExport.players(), playersPath);
        for (PersonalPlayerSnapshot snapshot : personalStatisticsExport.snapshots()) {
            writeJson(snapshot, personalStatsDirectory.resolve(personalStatsFileName(snapshot.player().id())));
        }

        ExportDiagnostics diagnostics = new ExportDiagnostics(
                exportedSamples,
                ignoredUnratedSamples,
                ignoredUnmappedSamples,
                courses.size(),
                courses.size(),
                generatedBasketStatsFiles,
                personalStatisticsExport.players().size(),
                personalStatisticsExport.snapshots().size()
        );
        StatisticsManifest manifest = new StatisticsManifest(
                new SnapshotMetadata(Instant.now().toString(), diagnostics),
                courseOptions,
                playersRelativePath(),
                personalStatsPathTemplate()
        );
        writeJson(manifest, manifestPath);
        return new ExportResult(
                manifestPath.toString(),
                courses.size(),
                generatedBasketStatsFiles,
                personalStatisticsExport.snapshots().size(),
                diagnostics
        );
    }

    private Path resolveManifestPath() {
        return resolveProjectRoot().resolve(MANIFEST_PATH).toAbsolutePath().normalize();
    }

    private Path resolveCoursesDirectory() {
        return resolveProjectRoot().resolve(COURSES_DIRECTORY).toAbsolutePath().normalize();
    }

    private Path resolveBasketStatsDirectory() {
        return resolveProjectRoot().resolve(BASKET_STATS_DIRECTORY).toAbsolutePath().normalize();
    }

    private Path resolvePlayersPath() {
        return resolveProjectRoot().resolve(PLAYERS_PATH).toAbsolutePath().normalize();
    }

    private Path resolvePersonalStatsDirectory() {
        return resolveProjectRoot().resolve(PERSONAL_STATS_DIRECTORY).toAbsolutePath().normalize();
    }

    private Path resolveProjectRoot() {
        try {
            Path classpathLocation = Path.of(BasketStatisticsExportService.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).toAbsolutePath().normalize();
            return classpathLocation.getParent().getParent();
        } catch (NullPointerException | URISyntaxException ex) {
            throw new IllegalStateException("Failed to resolve project root for basket statistics export", ex);
        }
    }

    private void writeJson(Object value, Path outputPath) {
        try {
            Path parent = outputPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            objectMapper.copy()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValue(outputPath.toFile(), value);
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to export basket statistics snapshot", ex);
        }
    }

    private String competitionName(StatisticsExportRow row) {
        return firstNonBlank(row.getCompetitionName(), "Competition " + row.getCompetitionId());
    }

    private String courseName(StatisticsExportRow row) {
        return firstNonBlank(row.getBasketCourseName(), "Basket course " + row.getBasketCourseId());
    }

    private String basketLabel(StatisticsExportRow row) {
        return firstNonBlank(row.getBasketName(), "Basket " + row.getBasketId());
    }

    private String variationLabel(StatisticsExportRow row) {
        String label = firstNonBlank(row.getVariationName(), "Variation " + row.getVariationId());
        if (row.getVariationDistance() == null) {
            return label;
        }
        return label + " [" + row.getVariationDistance() + "]";
    }

    private String playerName(StatisticsExportRow row) {
        return firstNonBlank(row.getPlayerName(), "Player " + row.getPlayerId());
    }

    private String firstNonBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String courseRelativePath(Long courseId) {
        return "courses/" + courseFileName(courseId);
    }

    private String basketStatsRelativePath(Long courseId) {
        return "basket-stats/" + courseFileName(courseId);
    }

    private String playersRelativePath() {
        return "players.json";
    }

    private String personalStatsRelativePath(Long playerId) {
        return "personal-stats/" + personalStatsFileName(playerId);
    }

    private String personalStatsPathTemplate() {
        return "personal-stats/{playerId}.json";
    }

    private String courseFileName(Long courseId) {
        return courseId + ".json";
    }

    private String personalStatsFileName(Long playerId) {
        return playerId + ".json";
    }

    private PersonalStatisticsExport buildPersonalStatisticsExport(List<PersonalScoreSample> samples) {
        Map<String, PersonalGlobalVariationBuilder> globalVariationBuilders = new LinkedHashMap<>();
        for (PersonalScoreSample sample : samples) {
            globalVariationBuilders.computeIfAbsent(
                    variationKey(sample.basketId(), sample.variationId()),
                    ignored -> new PersonalGlobalVariationBuilder(sample)
            ).samples.add(sample);
        }

        Map<String, PersonalGlobalVariation> eligibleVariations = new LinkedHashMap<>();
        for (PersonalGlobalVariationBuilder builder : globalVariationBuilders.values()) {
            PersonalGlobalVariation variation = builder.toEligibleVariation();
            if (variation != null) {
                eligibleVariations.put(variationKey(variation.basketId(), variation.variationId()), variation);
            }
        }

        Map<Long, PersonalPlayerBuilder> playersById = new LinkedHashMap<>();
        for (PersonalScoreSample sample : samples) {
            PersonalGlobalVariation variation = eligibleVariations.get(variationKey(sample.basketId(), sample.variationId()));
            if (variation == null) {
                continue;
            }
            playersById.computeIfAbsent(
                    sample.playerId(),
                    ignored -> new PersonalPlayerBuilder(sample.playerId(), sample.playerName(), sample.playerPdgaNum())
            ).add(sample, variation);
        }

        List<PersonalPlayerSnapshot> snapshots = playersById.values().stream()
                .map(PersonalPlayerBuilder::toSnapshot)
                .filter(snapshot -> !snapshot.variations().isEmpty())
                .sorted(Comparator.comparing(
                                (PersonalPlayerSnapshot snapshot) -> snapshot.player().label(),
                                String.CASE_INSENSITIVE_ORDER
                        )
                        .thenComparing(snapshot -> snapshot.player().id()))
                .toList();

        List<PlayerLookupEntry> players = snapshots.stream()
                .map(snapshot -> new PlayerLookupEntry(
                        snapshot.player().id(),
                        snapshot.player().name(),
                        snapshot.player().pdgaNum(),
                        snapshot.player().label(),
                        personalStatsRelativePath(snapshot.player().id())
                ))
                .toList();

        return new PersonalStatisticsExport(players, snapshots);
    }

    private static String variationKey(Long basketId, Long variationId) {
        return basketId + ":" + variationId;
    }

    private static class CourseExportBuilder {

        private final Long id;
        private final String name;
        private final Map<Long, CompetitionDescriptor> competitionsById = new LinkedHashMap<>();
        private final List<ScoreSample> samples = new ArrayList<>();

        private CourseExportBuilder(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        private Long id() {
            return id;
        }

        private String name() {
            return name;
        }

        private CourseSnapshot toSnapshot() {
            List<CompetitionDescriptor> competitions = competitionsById.values().stream()
                    .sorted(Comparator.comparing(CompetitionDescriptor::name, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(CompetitionDescriptor::id))
                    .toList();
            return new CourseSnapshot(new CourseDescriptor(id, name), competitions, samples);
        }

        private BasketStatsSnapshot toBasketStatsSnapshot() {
            Map<String, BasketStatsVariationBuilder> variationsByKey = new LinkedHashMap<>();
            for (ScoreSample sample : samples) {
                String key = sample.basketId() + ":" + sample.variationId();
                BasketStatsVariationBuilder variation = variationsByKey.computeIfAbsent(
                        key,
                        ignored -> new BasketStatsVariationBuilder(
                                sample.basketId(),
                                sample.basketLabel(),
                                sample.variationId(),
                                sample.variationLabel()
                        )
                );
                variation.samples.add(sample);
            }

            List<BasketStatsVariation> variations = variationsByKey.values().stream()
                    .sorted(Comparator.comparing(BasketStatsVariationBuilder::basketId)
                            .thenComparing(BasketStatsVariationBuilder::variationId))
                    .map(BasketStatsVariationBuilder::toVariation)
                    .filter(variation -> !variation.windows().isEmpty())
                    .toList();
            return new BasketStatsSnapshot(new CourseDescriptor(id, name), variations);
        }
    }

    private static class BasketStatsVariationBuilder {

        private final Long basketId;
        private final String basketLabel;
        private final Long variationId;
        private final String variationLabel;
        private final List<ScoreSample> samples = new ArrayList<>();

        private BasketStatsVariationBuilder(Long basketId, String basketLabel, Long variationId, String variationLabel) {
            this.basketId = basketId;
            this.basketLabel = basketLabel;
            this.variationId = variationId;
            this.variationLabel = variationLabel;
        }

        private Long basketId() {
            return basketId;
        }

        private Long variationId() {
            return variationId;
        }

        private BasketStatsVariation toVariation() {
            List<BasketStatsWindow> windows = buildWindows(samples);
            return new BasketStatsVariation(
                    basketId,
                    basketLabel,
                    variationId,
                    variationLabel,
                    samples.size(),
                    windows
            );
        }
    }

    private static List<BasketStatsWindow> buildWindows(List<ScoreSample> samples) {
        if (samples.isEmpty()) {
            return List.of();
        }

        int maxRating = samples.stream()
                .mapToInt(ScoreSample::rating)
                .max()
                .orElse(0);
        List<BasketStatsWindow> windows = new ArrayList<>();

        for (int start = 0; start <= maxRating; start += BASKET_STATS_WINDOW_STEP) {
            int end = start + BASKET_STATS_WINDOW_SIZE;
            int windowStart = start;
            int windowEnd = end;
            List<ScoreSample> windowSamples = samples.stream()
                    .filter(sample -> sample.rating() >= windowStart && sample.rating() <= windowEnd)
                    .toList();
            if (windowSamples.size() < BASKET_STATS_MIN_SAMPLES) {
                continue;
            }

            int midpoint = start + BASKET_STATS_WINDOW_SIZE / 2;
            WeightedRegression weightedRegression = weightedRegression(samples, midpoint);
            if (weightedRegression == null) {
                continue;
            }

            int count = windowSamples.size();
            windows.add(new BasketStatsWindow(
                    start,
                    end,
                    midpoint,
                    count,
                    -100 * weightedRegression.regression().slope(),
                    weightedRegression.weightedCount(),
                    countBucket(weightedRegression.weightedCount())
            ));
        }

        return windows;
    }

    private static Regression regression(List<ScoreSample> samples) {
        int count = samples.size();
        double meanRating = samples.stream()
                .mapToDouble(ScoreSample::rating)
                .average()
                .orElse(0);
        double meanScore = samples.stream()
                .mapToDouble(ScoreSample::score)
                .average()
                .orElse(0);
        double ratingVariance = 0;
        double covariance = 0;

        for (ScoreSample sample : samples) {
            double ratingDelta = sample.rating() - meanRating;
            ratingVariance += ratingDelta * ratingDelta;
            covariance += ratingDelta * (sample.score() - meanScore);
        }

        if (ratingVariance == 0 || count == 0) {
            return null;
        }

        double slope = covariance / ratingVariance;
        return new Regression(slope, meanScore - slope * meanRating);
    }

    private static WeightedRegression weightedRegression(List<ScoreSample> samples, int midpoint) {
        List<WeightedScoreSample> weightedSamples = samples.stream()
                .map(sample -> new WeightedScoreSample(sample, sprwWeight(sample.rating(), midpoint)))
                .filter(sample -> sample.weight() > 0)
                .toList();
        double weightedCount = weightedSamples.stream()
                .mapToDouble(WeightedScoreSample::weight)
                .sum();
        if (weightedCount < BASKET_STATS_MIN_SAMPLES) {
            return null;
        }

        double meanRating = weightedSamples.stream()
                .mapToDouble(sample -> sample.weight() * sample.sample().rating())
                .sum() / weightedCount;
        double meanScore = weightedSamples.stream()
                .mapToDouble(sample -> sample.weight() * sample.sample().score())
                .sum() / weightedCount;
        double ratingVariance = 0;
        double covariance = 0;

        for (WeightedScoreSample sample : weightedSamples) {
            double ratingDelta = sample.sample().rating() - meanRating;
            ratingVariance += sample.weight() * ratingDelta * ratingDelta;
            covariance += sample.weight() * ratingDelta * (sample.sample().score() - meanScore);
        }

        if (ratingVariance == 0) {
            return null;
        }

        double slope = covariance / ratingVariance;
        return new WeightedRegression(new Regression(slope, meanScore - slope * meanRating), weightedCount);
    }

    private static double sprwWeight(int rating, int midpoint) {
        int distance = Math.abs(rating - midpoint);
        if (distance > BASKET_STATS_SPRW_RADIUS) {
            return 0;
        }
        return Math.max(0, 1 - (double) distance / BASKET_STATS_SPRW_RADIUS);
    }

    private static String countBucket(double count) {
        if (count >= 200) {
            return "200+";
        }
        if (count >= 100) {
            return "100-199";
        }
        return "50-99";
    }

    public record StatisticsManifest(
            SnapshotMetadata metadata,
            List<CourseOption> courses,
            String playersPath,
            String personalStatsPathTemplate
    ) {
    }

    public record SnapshotMetadata(
            String exportedAt,
            ExportDiagnostics diagnostics
    ) {
    }

    public record ExportDiagnostics(
            long exportedSamples,
            long ignoredUnratedSamples,
            long ignoredUnmappedSamples,
            long includedBasketCourses,
            long generatedCourseFiles,
            long generatedBasketStatsFiles,
            long eligiblePersonalPlayers,
            long generatedPersonalStatsFiles
    ) {
    }

    public record CourseOption(
            Long id,
            String name,
            int sampleCount,
            String path,
            String basketStatsPath
    ) {
    }

    public record CourseSnapshot(
            CourseDescriptor course,
            List<CompetitionDescriptor> competitions,
            List<ScoreSample> samples
    ) {
    }

    public record CourseDescriptor(
            Long id,
            String name
    ) {
    }

    public record CompetitionDescriptor(
            Long id,
            String name
    ) {
    }

    public record ScoreSample(
            Long basketCourseId,
            Long competitionId,
            Long basketId,
            String basketLabel,
            Long variationId,
            String variationLabel,
            Integer rating,
            Integer score
    ) {
    }

    public record PlayerLookupEntry(
            Long id,
            String name,
            Long pdgaNum,
            String label,
            String path
    ) {
    }

    public record PersonalPlayerSnapshot(
            PlayerDescriptor player,
            List<PersonalVariationRow> variations
    ) {
    }

    public record PlayerDescriptor(
            Long id,
            String name,
            Long pdgaNum,
            String label
    ) {
    }

    public record PersonalVariationRow(
            Long basketCourseId,
            String basketCourseName,
            Long basketId,
            String basketLabel,
            Long variationId,
            String variationLabel,
            int globalSampleCount,
            int count,
            double rating,
            int displayRating,
            List<Integer> scores
    ) {
    }

    public record BasketStatsSnapshot(
            CourseDescriptor course,
            List<BasketStatsVariation> variations
    ) {
    }

    public record BasketStatsVariation(
            Long basketId,
            String basketLabel,
            Long variationId,
            String variationLabel,
            int sampleCount,
            List<BasketStatsWindow> windows
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BasketStatsWindow(
            int ratingFrom,
            int ratingTo,
            int ratingMidpoint,
            int count,
            double sprw,
            double sprwCount,
            String sprwCountBucket
    ) {
    }

    private record Regression(
            double slope,
            double intercept
    ) {

        private double expectedScore(int rating) {
            return intercept + slope * rating;
        }
    }

    private record WeightedScoreSample(
            ScoreSample sample,
            double weight
    ) {
    }

    private record WeightedRegression(
            Regression regression,
            double weightedCount
    ) {
    }

    private record PersonalStatisticsExport(
            List<PlayerLookupEntry> players,
            List<PersonalPlayerSnapshot> snapshots
    ) {
    }

    private record PersonalScoreSample(
            Long basketCourseId,
            String basketCourseName,
            Long basketId,
            String basketLabel,
            Long variationId,
            String variationLabel,
            Long playerId,
            String playerName,
            Long playerPdgaNum,
            Long roundId,
            LocalDate roundDate,
            Integer rating,
            Integer score
    ) {
    }

    private record PersonalGlobalVariation(
            Long basketId,
            Long variationId,
            int sampleCount,
            Regression regression
    ) {
    }

    private static class PersonalGlobalVariationBuilder {

        private final Long basketId;
        private final Long variationId;
        private final List<PersonalScoreSample> samples = new ArrayList<>();

        private PersonalGlobalVariationBuilder(PersonalScoreSample sample) {
            this.basketId = sample.basketId();
            this.variationId = sample.variationId();
        }

        private PersonalGlobalVariation toEligibleVariation() {
            if (samples.size() < PERSONAL_STATS_MIN_GLOBAL_SAMPLES) {
                return null;
            }
            Regression regression = personalRegression(samples);
            if (regression == null || regression.slope() >= 0) {
                return null;
            }
            return new PersonalGlobalVariation(basketId, variationId, samples.size(), regression);
        }
    }

    private static class PersonalPlayerBuilder {

        private final Long id;
        private final String name;
        private final Long pdgaNum;
        private final Map<String, PersonalVariationBuilder> variationsByKey = new LinkedHashMap<>();

        private PersonalPlayerBuilder(Long id, String name, Long pdgaNum) {
            this.id = id;
            this.name = name;
            this.pdgaNum = pdgaNum;
        }

        private void add(PersonalScoreSample sample, PersonalGlobalVariation globalVariation) {
            variationsByKey.computeIfAbsent(
                    variationKey(sample.basketId(), sample.variationId()),
                    ignored -> new PersonalVariationBuilder(sample, globalVariation)
            ).samples.add(sample);
        }

        private PersonalPlayerSnapshot toSnapshot() {
            List<PersonalVariationRow> rows = variationsByKey.values().stream()
                    .map(PersonalVariationBuilder::toRow)
                    .filter(row -> row != null)
                    .sorted(Comparator.comparingDouble(PersonalVariationRow::rating).reversed()
                            .thenComparing(PersonalVariationRow::basketCourseName, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(PersonalVariationRow::basketId)
                            .thenComparing(PersonalVariationRow::variationId))
                    .toList();
            return new PersonalPlayerSnapshot(
                    new PlayerDescriptor(id, name, pdgaNum, playerLabel(name, pdgaNum)),
                    rows
            );
        }
    }

    private static class PersonalVariationBuilder {

        private final Long basketCourseId;
        private final String basketCourseName;
        private final Long basketId;
        private final String basketLabel;
        private final Long variationId;
        private final String variationLabel;
        private final PersonalGlobalVariation globalVariation;
        private final List<PersonalScoreSample> samples = new ArrayList<>();

        private PersonalVariationBuilder(PersonalScoreSample sample, PersonalGlobalVariation globalVariation) {
            this.basketCourseId = sample.basketCourseId();
            this.basketCourseName = sample.basketCourseName();
            this.basketId = sample.basketId();
            this.basketLabel = sample.basketLabel();
            this.variationId = sample.variationId();
            this.variationLabel = sample.variationLabel();
            this.globalVariation = globalVariation;
        }

        private PersonalVariationRow toRow() {
            if (samples.size() < PERSONAL_STATS_MIN_PLAYER_SAMPLES) {
                return null;
            }
            List<PersonalScoreSample> sortedSamples = samples.stream()
                    .sorted(Comparator.comparing(
                                    PersonalScoreSample::roundDate,
                                    Comparator.nullsLast(Comparator.naturalOrder())
                            )
                            .thenComparing(PersonalScoreSample::roundId))
                    .toList();
            double averageRating = sortedSamples.stream()
                    .mapToDouble(sample -> calculatedRating(sample.score(), globalVariation.regression()))
                    .average()
                    .orElse(0);
            return new PersonalVariationRow(
                    basketCourseId,
                    basketCourseName,
                    basketId,
                    basketLabel,
                    variationId,
                    variationLabel,
                    globalVariation.sampleCount(),
                    sortedSamples.size(),
                    averageRating,
                    (int) Math.round(averageRating),
                    sortedSamples.stream()
                            .map(PersonalScoreSample::score)
                            .toList()
            );
        }
    }

    private static Regression personalRegression(List<PersonalScoreSample> samples) {
        int count = samples.size();
        double meanRating = samples.stream()
                .mapToDouble(PersonalScoreSample::rating)
                .average()
                .orElse(0);
        double meanScore = samples.stream()
                .mapToDouble(PersonalScoreSample::score)
                .average()
                .orElse(0);
        double ratingVariance = 0;
        double covariance = 0;

        for (PersonalScoreSample sample : samples) {
            double ratingDelta = sample.rating() - meanRating;
            ratingVariance += ratingDelta * ratingDelta;
            covariance += ratingDelta * (sample.score() - meanScore);
        }

        if (ratingVariance == 0 || count == 0) {
            return null;
        }

        double slope = covariance / ratingVariance;
        return new Regression(slope, meanScore - slope * meanRating);
    }

    private static double calculatedRating(int score, Regression regression) {
        return (score - regression.intercept()) / regression.slope();
    }

    private static String playerLabel(String name, Long pdgaNum) {
        if (pdgaNum == null) {
            return name;
        }
        return name + " (" + pdgaNum + ")";
    }

    public record ExportResult(
            String path,
            int generatedCourseFiles,
            int generatedBasketStatsFiles,
            int generatedPersonalStatsFiles,
            ExportDiagnostics diagnostics
    ) {
    }
}
