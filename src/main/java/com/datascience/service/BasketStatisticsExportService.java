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

        ExportDiagnostics diagnostics = new ExportDiagnostics(
                exportedSamples,
                ignoredUnratedSamples,
                ignoredUnmappedSamples,
                courses.size(),
                courses.size()
        );
        Path manifestPath = resolveManifestPath();
        Path coursesDirectory = resolveCoursesDirectory();
        List<CourseOption> courseOptions = new ArrayList<>();

        for (CourseExportBuilder course : courses) {
            String relativePath = courseRelativePath(course.id());
            CourseSnapshot snapshot = course.toSnapshot();
            writeJson(snapshot, coursesDirectory.resolve(courseFileName(course.id())));
            courseOptions.add(new CourseOption(course.id(), course.name(), course.samples.size(), relativePath));
        }

        StatisticsManifest manifest = new StatisticsManifest(
                new SnapshotMetadata(Instant.now().toString(), diagnostics),
                courseOptions
        );
        writeJson(manifest, manifestPath);
        return new ExportResult(manifestPath.toString(), courses.size(), diagnostics);
    }

    private Path resolveManifestPath() {
        return resolveProjectRoot().resolve(MANIFEST_PATH).toAbsolutePath().normalize();
    }

    private Path resolveCoursesDirectory() {
        return resolveProjectRoot().resolve(COURSES_DIRECTORY).toAbsolutePath().normalize();
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
            long generatedCourseFiles
    ) {
    }

    public record CourseOption(
            Long id,
            String name,
            int sampleCount,
            String path
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

    public record ExportResult(
            String path,
            int generatedCourseFiles,
            ExportDiagnostics diagnostics
    ) {
    }
}
