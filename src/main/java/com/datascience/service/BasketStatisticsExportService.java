package com.datascience.service;

import com.datascience.repository.HoleScoreRepository;
import com.datascience.repository.HoleScoreRepository.StatisticsExportRow;
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
    private static final int BASKET_STATS_WINDOW_SIZE = 50;
    private static final int BASKET_STATS_WINDOW_STEP = 5;
    private static final int BASKET_STATS_MIN_SAMPLES = 50;

    private final HoleScoreRepository holeScoreRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public ExportResult exportStatistics() {
        List<StatisticsExportRow> rows = holeScoreRepository.findStatisticsExportRows();
        Map<Long, CourseExportBuilder> coursesById = new LinkedHashMap<>();
        long ignoredUnratedSamples = 0;
        long ignoredUnmappedSamples = 0;
        long exportedSamples = 0;

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
            exportedSamples++;
        }

        List<CourseExportBuilder> courses = coursesById.values().stream()
                .sorted(Comparator.comparing(CourseExportBuilder::name, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(CourseExportBuilder::id))
                .toList();

        Path manifestPath = resolveManifestPath();
        Path coursesDirectory = resolveCoursesDirectory();
        Path basketStatsDirectory = resolveBasketStatsDirectory();
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

        ExportDiagnostics diagnostics = new ExportDiagnostics(
                exportedSamples,
                ignoredUnratedSamples,
                ignoredUnmappedSamples,
                courses.size(),
                courses.size(),
                generatedBasketStatsFiles
        );
        StatisticsManifest manifest = new StatisticsManifest(
                new SnapshotMetadata(Instant.now().toString(), diagnostics),
                courseOptions
        );
        writeJson(manifest, manifestPath);
        return new ExportResult(manifestPath.toString(), courses.size(), generatedBasketStatsFiles, diagnostics);
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

    private String firstNonBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String courseRelativePath(Long courseId) {
        return "courses/" + courseFileName(courseId);
    }

    private String basketStatsRelativePath(Long courseId) {
        return "basket-stats/" + courseFileName(courseId);
    }

    private String courseFileName(Long courseId) {
        return courseId + ".json";
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

            Regression regression = regression(windowSamples);
            if (regression == null) {
                continue;
            }

            double residualTotal = windowSamples.stream()
                    .mapToDouble(sample -> Math.abs(sample.score() - regression.expectedScore(sample.rating())))
                    .sum();
            int count = windowSamples.size();
            windows.add(new BasketStatsWindow(
                    start,
                    end,
                    start + BASKET_STATS_WINDOW_SIZE / 2,
                    count,
                    countBucket(count),
                    -100 * regression.slope(),
                    residualTotal / count
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

    private static String countBucket(int count) {
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
            List<CourseOption> courses
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
            long generatedBasketStatsFiles
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

    public record BasketStatsWindow(
            int ratingFrom,
            int ratingTo,
            int ratingMidpoint,
            int count,
            String countBucket,
            double spr,
            double var
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

    public record ExportResult(
            String path,
            int generatedCourseFiles,
            int generatedBasketStatsFiles,
            ExportDiagnostics diagnostics
    ) {
    }
}
