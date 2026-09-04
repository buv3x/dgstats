package com.datascience.service;

import com.datascience.domain.Basket;
import com.datascience.domain.BasketCourse;
import com.datascience.domain.BasketVariation;
import com.datascience.domain.BasketVariationRoundDivision;
import com.datascience.domain.Competition;
import com.datascience.domain.HoleScore;
import com.datascience.domain.Round;
import com.datascience.domain.RoundDivision;
import com.datascience.domain.RoundResult;
import com.datascience.repository.BasketCourseRepository;
import com.datascience.repository.BasketRepository;
import com.datascience.repository.BasketVariationRepository;
import com.datascience.repository.BasketVariationRoundDivisionRepository;
import com.datascience.repository.CompetitionRepository;
import com.datascience.repository.HoleScoreRepository;
import com.datascience.repository.RoundDivisionRepository;
import com.datascience.repository.RoundResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BasketVariationMappingAdminService {

    private final CompetitionRepository competitionRepository;
    private final BasketCourseRepository basketCourseRepository;
    private final BasketRepository basketRepository;
    private final BasketVariationRepository basketVariationRepository;
    private final RoundDivisionRepository roundDivisionRepository;
    private final RoundResultRepository roundResultRepository;
    private final HoleScoreRepository holeScoreRepository;
    private final BasketVariationRoundDivisionRepository mappingRepository;

    @Transactional(readOnly = true)
    public MappingEditorModel loadEditor(Long competitionId) {
        Set<Long> mappedCompetitionIds = mappingRepository.findMappedCompetitionIds();
        List<CompetitionOption> competitions = competitionRepository.findAllForMappingOrder().stream()
                .map(competition -> new CompetitionOption(
                        competition.getId(),
                        competitionLabel(competition),
                        mappedCompetitionIds.contains(competition.getId())
                ))
                .toList();
        List<BasketCourse> basketCourseEntities = basketCourseRepository.findAllByOrderByNameAsc();
        List<BasketCourseOption> basketCourses = basketCourseEntities.stream()
                .map(course -> new BasketCourseOption(course.getId(), course.getName()))
                .toList();
        Long defaultBasketCourseId = defaultBasketCourseId(basketCourses);

        if (competitionId == null) {
            return new MappingEditorModel(competitions, null, basketCourses, defaultBasketCourseId, false, false, List.of(), List.of());
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        List<VariationOption> variationOptions = loadVariationOptions(basketCourseEntities);
        List<RoundTable> roundTables = loadRoundTables(competition, defaultBasketCourseId);

        return new MappingEditorModel(
                competitions,
                competition.getId(),
                basketCourses,
                defaultBasketCourseId,
                false,
                false,
                variationOptions,
                roundTables
        );
    }

    @Transactional
    public void saveMappings(Long competitionId, MappingFormSubmission form, List<CellSubmission> submissions) {
        if (competitionId == null) {
            throw new IllegalArgumentException("Competition is required");
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        List<RoundDivision> roundDivisions = roundDivisionRepository.findByCompetitionForMapping(competition);
        Map<Long, Round> roundsById = roundsById(roundDivisions);
        Map<Long, RoundDivision> roundDivisionsById = roundDivisionsById(roundDivisions);
        Map<Long, Set<Integer>> editableSlots = editableSlotsByRoundDivision(roundDivisions);
        Map<Long, BasketCourse> basketCoursesById = basketCourseRepository.findAllByOrderByNameAsc().stream()
                .collect(LinkedHashMap::new, (map, course) -> map.put(course.getId(), course), Map::putAll);

        validateScopeSelection(form, roundsById, roundDivisionsById, basketCoursesById);
        Map<Long, Long> effectiveCourseByRoundDivision = effectiveCourseByRoundDivision(form, roundDivisions, basketCoursesById);
        Map<Long, Set<Long>> allowedVariationIdsByBasketCourse = allowedVariationIdsByBasketCourse(basketCoursesById);
        List<CellSubmission> effectiveSubmissions = expandSameLayoutSubmissions(roundDivisions, editableSlots, form, submissions);

        for (CellSubmission submission : effectiveSubmissions) {
            if (submission.roundDivisionId() == null || submission.holeOrdinal() == null) {
                throw new IllegalArgumentException("Invalid mapping cell submitted");
            }
            Set<Integer> holeOrdinals = editableSlots.get(submission.roundDivisionId());
            if (holeOrdinals == null) {
                throw new IllegalArgumentException("Submitted round division does not belong to the selected competition");
            }
            if (!holeOrdinals.contains(submission.holeOrdinal())) {
                throw new IllegalArgumentException("Submitted hole ordinal is not editable for the selected round division");
            }
            Long basketCourseId = effectiveCourseByRoundDivision.get(submission.roundDivisionId());
            Set<Long> allowedVariationIds = allowedVariationIdsByBasketCourse.get(basketCourseId);
            if (submission.basketVariationId() != null
                    && (allowedVariationIds == null || !allowedVariationIds.contains(submission.basketVariationId()))) {
                throw new IllegalArgumentException("Submitted basket variation does not belong to the selected basket course");
            }
        }

        Set<Long> allAllowedVariationIds = allowedVariationIdsByBasketCourse.values().stream()
                .flatMap(Collection::stream)
                .collect(HashSet::new, Set::add, Set::addAll);
        Map<Long, BasketVariation> variationsById = basketVariationRepository.findAllById(allAllowedVariationIds).stream()
                .collect(LinkedHashMap::new, (map, variation) -> map.put(variation.getId(), variation), Map::putAll);

        for (CellSubmission submission : effectiveSubmissions) {
            RoundDivision roundDivision = roundDivisionsById.get(submission.roundDivisionId());
            if (submission.basketVariationId() == null) {
                mappingRepository.deleteByRoundDivisionAndHoleOrdinal(roundDivision, submission.holeOrdinal());
                continue;
            }

            BasketVariationRoundDivision mapping = mappingRepository
                    .findByRoundDivisionAndHoleOrdinal(roundDivision, submission.holeOrdinal())
                    .orElseGet(BasketVariationRoundDivision::new);
            mapping.setRoundDivision(roundDivision);
            mapping.setHoleOrdinal(submission.holeOrdinal());
            mapping.setBasketVariation(variationsById.get(submission.basketVariationId()));
            mappingRepository.save(mapping);
        }
    }

    private String competitionLabel(Competition competition) {
        String name = firstNonBlank(competition.getName(), competition.getSimpleName(), "Competition " + competition.getId());
        LocalDate startDate = competition.getStartDate();
        return startDate == null ? name + " (no date)" : name + " (" + startDate + ")";
    }

    private List<VariationOption> loadVariationOptions(List<BasketCourse> basketCourses) {
        List<VariationOption> options = new ArrayList<>();
        for (BasketCourse basketCourse : basketCourses) {
            List<Basket> baskets = basketRepository.findByBasketCourseOrderByIdAsc(basketCourse);
            if (baskets.isEmpty()) {
                continue;
            }
            List<VariationOption> courseOptions = basketVariationRepository.findByBasketInForMapping(baskets).stream()
                    .map(variation -> new VariationOption(
                            variation.getId(),
                            variationLabel(variation),
                            variation.getBasket().getId(),
                            basketCourse.getId()
                    ))
                    .toList();
            options.addAll(courseOptions);
        }
        return options;
    }

    private String variationLabel(BasketVariation variation) {
        String basketName = firstNonBlank(variation.getBasket().getName(), "Basket " + variation.getBasket().getId());
        String variationName = firstNonBlank(variation.getName(), "Variation " + variation.getId());
        if ("default".equalsIgnoreCase(variationName) || "main".equalsIgnoreCase(variationName)) {
            return basketName + distanceSuffix(variation);
        }
        return basketName + " - " + variationName + distanceSuffix(variation);
    }

    private String distanceSuffix(BasketVariation variation) {
        return variation.getDistance() == null ? "" : " [" + variation.getDistance() + "]";
    }

    private List<RoundTable> loadRoundTables(Competition competition, Long defaultBasketCourseId) {
        List<RoundDivision> roundDivisions = roundDivisionRepository.findByCompetitionForMapping(competition);
        Map<Long, Set<Integer>> slotsByRoundDivision = editableSlotsByRoundDivision(roundDivisions);
        Map<String, Long> selectedVariationIds = selectedVariationIds(roundDivisions);
        Map<Long, List<RoundDivision>> roundDivisionsByRound = roundDivisionsByRound(roundDivisions);

        List<RoundTable> tables = new ArrayList<>();
        for (List<RoundDivision> divisions : roundDivisionsByRound.values()) {
            Round round = divisions.get(0).getRound();
            List<DivisionColumn> columns = divisions.stream()
                    .map(roundDivision -> new DivisionColumn(
                            roundDivision.getId(),
                            divisionLabel(roundDivision),
                            divisionCopyKey(roundDivision),
                            defaultBasketCourseId,
                            slotsByRoundDivision.getOrDefault(roundDivision.getId(), Set.of())
                    ))
                    .toList();
            int maxHoleOrdinal = columns.stream()
                    .map(DivisionColumn::holeOrdinals)
                    .flatMap(Collection::stream)
                    .max(Integer::compareTo)
                    .orElse(0);
            List<Integer> holeOrdinals = IntStream.rangeClosed(1, maxHoleOrdinal).boxed().toList();
            tables.add(new RoundTable(
                    round.getId(),
                    roundLabel(round),
                    defaultBasketCourseId,
                    false,
                    false,
                    columns,
                    holeOrdinals,
                    selectedVariationIds
            ));
        }
        return tables;
    }

    private void validateScopeSelection(
            MappingFormSubmission form,
            Map<Long, Round> roundsById,
            Map<Long, RoundDivision> roundDivisionsById,
            Map<Long, BasketCourse> basketCoursesById
    ) {
        if (!form.courseSelectionByRound()) {
            validateBasketCourse(form.basketCourseId(), basketCoursesById);
            return;
        }

        for (Long roundId : roundsById.keySet()) {
            if (form.courseSelectionByGroupByRound().getOrDefault(roundId, false)) {
                continue;
            }
            validateBasketCourse(form.roundBasketCourseIds().get(roundId), basketCoursesById);
        }
        for (RoundDivision roundDivision : roundDivisionsById.values()) {
            Long roundId = roundDivision.getRound().getId();
            if (!form.courseSelectionByGroupByRound().getOrDefault(roundId, false)) {
                continue;
            }
            validateBasketCourse(form.groupBasketCourseIds().get(roundDivision.getId()), basketCoursesById);
        }
    }

    private void validateBasketCourse(Long basketCourseId, Map<Long, BasketCourse> basketCoursesById) {
        if (basketCourseId == null || !basketCoursesById.containsKey(basketCourseId)) {
            throw new IllegalArgumentException("Submitted basket course does not exist");
        }
    }

    private Map<Long, Long> effectiveCourseByRoundDivision(
            MappingFormSubmission form,
            List<RoundDivision> roundDivisions,
            Map<Long, BasketCourse> basketCoursesById
    ) {
        Map<Long, Long> effectiveCourses = new HashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            Long roundId = roundDivision.getRound().getId();
            Long basketCourseId;
            if (!form.courseSelectionByRound()) {
                basketCourseId = form.basketCourseId();
            } else if (form.courseSelectionByGroupByRound().getOrDefault(roundId, false)) {
                basketCourseId = form.groupBasketCourseIds().get(roundDivision.getId());
            } else {
                basketCourseId = form.roundBasketCourseIds().get(roundId);
            }
            validateBasketCourse(basketCourseId, basketCoursesById);
            effectiveCourses.put(roundDivision.getId(), basketCourseId);
        }
        return effectiveCourses;
    }

    private Map<Long, Set<Long>> allowedVariationIdsByBasketCourse(Map<Long, BasketCourse> basketCoursesById) {
        Map<Long, Set<Long>> allowedByCourse = new HashMap<>();
        for (BasketCourse basketCourse : basketCoursesById.values()) {
            allowedByCourse.put(basketCourse.getId(), allowedVariationIds(basketCourse));
        }
        return allowedByCourse;
    }

    private Map<Long, Set<Integer>> editableSlotsByRoundDivision(List<RoundDivision> roundDivisions) {
        Map<Long, Set<Integer>> slots = new HashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            slots.put(roundDivision.getId(), deriveHoleOrdinals(roundDivision));
        }
        return slots;
    }

    private List<CellSubmission> expandSameLayoutSubmissions(
            List<RoundDivision> roundDivisions,
            Map<Long, Set<Integer>> editableSlots,
            MappingFormSubmission form,
            List<CellSubmission> submissions
    ) {
        Map<String, CellSubmission> submittedCells = new HashMap<>();
        for (CellSubmission submission : submissions) {
            if (submission.roundDivisionId() != null && submission.holeOrdinal() != null) {
                submittedCells.put(cellKey(submission.roundDivisionId(), submission.holeOrdinal()), submission);
            }
        }

        Map<String, CellSubmission> expandedCells = new LinkedHashMap<>();
        for (List<RoundDivision> divisions : roundDivisionsByRound(roundDivisions).values()) {
            if (divisions.isEmpty()) {
                continue;
            }
            Long roundId = divisions.get(0).getRound().getId();
            boolean sameLayout = effectiveSameLayout(form, roundId);
            if (!sameLayout) {
                for (RoundDivision division : divisions) {
                    for (Integer holeOrdinal : editableSlots.getOrDefault(division.getId(), Set.of())) {
                        CellSubmission submission = submittedCells.get(cellKey(division.getId(), holeOrdinal));
                        if (submission != null) {
                            expandedCells.put(cellKey(division.getId(), holeOrdinal), submission);
                        }
                    }
                }
                continue;
            }

            RoundDivision sourceDivision = divisions.get(0);
            Set<Integer> sourceHoles = editableSlots.getOrDefault(sourceDivision.getId(), Set.of());
            for (Integer holeOrdinal : sourceHoles) {
                CellSubmission sourceSubmission = submittedCells.get(cellKey(sourceDivision.getId(), holeOrdinal));
                if (sourceSubmission == null) {
                    continue;
                }
                for (RoundDivision targetDivision : divisions) {
                    if (editableSlots.getOrDefault(targetDivision.getId(), Set.of()).contains(holeOrdinal)) {
                        CellSubmission expandedSubmission = new CellSubmission(
                                targetDivision.getId(),
                                holeOrdinal,
                                sourceSubmission.basketVariationId()
                        );
                        expandedCells.put(cellKey(targetDivision.getId(), holeOrdinal), expandedSubmission);
                    }
                }
            }
        }
        return new ArrayList<>(expandedCells.values());
    }

    private boolean effectiveSameLayout(MappingFormSubmission form, Long roundId) {
        if (!form.courseSelectionByRound()) {
            return form.sameLayout();
        }
        if (form.courseSelectionByGroupByRound().getOrDefault(roundId, false)) {
            return false;
        }
        return form.sameLayoutByRound().getOrDefault(roundId, false);
    }

    private Set<Integer> deriveHoleOrdinals(RoundDivision roundDivision) {
        Optional<RoundResult> firstResult = roundResultRepository.findFirstByRoundDivisionOrderByIdAsc(roundDivision);
        if (firstResult.isPresent()) {
            List<Integer> ordinals = holeScoreRepository.findByRoundResultOrderByHoleOrdinalAsc(firstResult.get()).stream()
                    .map(HoleScore::getHoleOrdinal)
                    .filter(Objects::nonNull)
                    .toList();
            if (!ordinals.isEmpty()) {
                return new HashSet<>(ordinals);
            }
        }
        Integer holes = roundDivision.getLayout() == null ? null : roundDivision.getLayout().getHoles();
        if (holes == null || holes < 1) {
            return Set.of();
        }
        return new HashSet<>(IntStream.rangeClosed(1, holes).boxed().toList());
    }

    private Map<String, Long> selectedVariationIds(List<RoundDivision> roundDivisions) {
        if (roundDivisions.isEmpty()) {
            return Map.of();
        }
        return mappingRepository.findByRoundDivisionIn(roundDivisions).stream()
                .collect(HashMap::new,
                        (map, mapping) -> map.put(cellKey(mapping.getRoundDivision().getId(), mapping.getHoleOrdinal()),
                                mapping.getBasketVariation().getId()),
                        Map::putAll);
    }

    private Set<Long> allowedVariationIds(BasketCourse basketCourse) {
        List<Basket> baskets = basketRepository.findByBasketCourseOrderByIdAsc(basketCourse);
        if (baskets.isEmpty()) {
            return Set.of();
        }
        return basketVariationRepository.findByBasketInForMapping(baskets).stream()
                .map(BasketVariation::getId)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private Map<Long, Round> roundsById(List<RoundDivision> roundDivisions) {
        Map<Long, Round> roundsById = new LinkedHashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            roundsById.put(roundDivision.getRound().getId(), roundDivision.getRound());
        }
        return roundsById;
    }

    private Map<Long, RoundDivision> roundDivisionsById(List<RoundDivision> roundDivisions) {
        return roundDivisions.stream()
                .collect(LinkedHashMap::new, (map, rd) -> map.put(rd.getId(), rd), Map::putAll);
    }

    private Map<Long, List<RoundDivision>> roundDivisionsByRound(List<RoundDivision> roundDivisions) {
        Map<Long, List<RoundDivision>> roundDivisionsByRound = new LinkedHashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            roundDivisionsByRound.computeIfAbsent(roundDivision.getRound().getId(), ignored -> new ArrayList<>())
                    .add(roundDivision);
        }
        return roundDivisionsByRound;
    }

    private Long defaultBasketCourseId(List<BasketCourseOption> basketCourses) {
        return basketCourses.isEmpty() ? null : basketCourses.get(0).id();
    }

    private String roundLabel(Round round) {
        String label = firstNonBlank(round.getLabel(), round.getLabelAbbreviated());
        if (label != null) {
            return label;
        }
        return round.getNumber() == null ? "Round " + round.getId() : "Round " + round.getNumber();
    }

    private String divisionLabel(RoundDivision roundDivision) {
        if (roundDivision.getCompetitionDivision() != null) {
            return firstNonBlank(
                    roundDivision.getCompetitionDivision().getCode(),
                    roundDivision.getCompetitionDivision().getShortName(),
                    roundDivision.getCompetitionDivision().getName(),
                    roundDivision.getDivisionCode(),
                    "Division " + roundDivision.getId()
            );
        }
        return firstNonBlank(roundDivision.getDivisionCode(), "Division " + roundDivision.getId());
    }

    private String divisionCopyKey(RoundDivision roundDivision) {
        if (roundDivision.getCompetitionDivision() != null) {
            Long competitionDivisionId = roundDivision.getCompetitionDivision().getId();
            if (competitionDivisionId != null) {
                return "competitionDivision:" + competitionDivisionId;
            }
        }
        String divisionCode = firstNonBlank(roundDivision.getDivisionCode());
        if (divisionCode != null) {
            return "divisionCode:" + divisionCode;
        }
        return "roundDivision:" + roundDivision.getId();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    public static String cellKey(Long roundDivisionId, Integer holeOrdinal) {
        return roundDivisionId + "_" + holeOrdinal;
    }

    public record MappingEditorModel(
            List<CompetitionOption> competitions,
            Long selectedCompetitionId,
            List<BasketCourseOption> basketCourses,
            Long selectedBasketCourseId,
            boolean sameLayout,
            boolean courseSelectionByRound,
            List<VariationOption> variationOptions,
            List<RoundTable> roundTables
    ) {
        public boolean hasCompetitionSelection() {
            return selectedCompetitionId != null;
        }

        public boolean hasBasketCourseSelection() {
            return !basketCourses.isEmpty();
        }

        public boolean hasVariationOptions() {
            return !variationOptions.isEmpty();
        }
    }

    public record CompetitionOption(Long id, String label, boolean mapped) {
        public String displayLabel() {
            return mapped ? "* " + label : label;
        }
    }

    public record BasketCourseOption(Long id, String name) {
    }

    public record VariationOption(Long id, String label, Long basketId, Long basketCourseId) {
    }

    public record RoundTable(
            Long roundId,
            String label,
            Long selectedBasketCourseId,
            boolean sameLayout,
            boolean courseSelectionByGroup,
            List<DivisionColumn> divisions,
            List<Integer> holeOrdinals,
            Map<String, Long> selectedVariationIds
    ) {
        public Long selectedVariationId(Long roundDivisionId, Integer holeOrdinal) {
            return selectedVariationIds.get(cellKey(roundDivisionId, holeOrdinal));
        }

        public boolean isFirstDivision(Long roundDivisionId) {
            return !divisions.isEmpty() && Objects.equals(divisions.get(0).roundDivisionId(), roundDivisionId);
        }

        public Long sameLayoutSelectedVariationId(Long roundDivisionId, Integer holeOrdinal) {
            if (isFirstDivision(roundDivisionId) || divisions.isEmpty()) {
                return selectedVariationId(roundDivisionId, holeOrdinal);
            }
            return selectedVariationId(divisions.get(0).roundDivisionId(), holeOrdinal);
        }
    }

    public record DivisionColumn(
            Long roundDivisionId,
            String label,
            String copyKey,
            Long selectedBasketCourseId,
            Set<Integer> holeOrdinals
    ) {
        public boolean hasHole(Integer holeOrdinal) {
            return holeOrdinals.contains(holeOrdinal);
        }
    }

    public record MappingFormSubmission(
            Long basketCourseId,
            boolean sameLayout,
            boolean courseSelectionByRound,
            Map<Long, Long> roundBasketCourseIds,
            Map<Long, Boolean> sameLayoutByRound,
            Map<Long, Boolean> courseSelectionByGroupByRound,
            Map<Long, Long> groupBasketCourseIds
    ) {
    }

    public record CellSubmission(Long roundDivisionId, Integer holeOrdinal, Long basketVariationId) {
    }
}
