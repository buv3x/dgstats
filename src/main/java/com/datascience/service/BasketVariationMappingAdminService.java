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
import java.util.Comparator;
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
    public MappingEditorModel loadEditor(Long competitionId, Long basketCourseId) {
        List<CompetitionOption> competitions = competitionRepository.findAllForMappingOrder().stream()
                .map(competition -> new CompetitionOption(competition.getId(), competitionLabel(competition)))
                .toList();
        List<BasketCourseOption> basketCourses = basketCourseRepository.findAllByOrderByNameAsc().stream()
                .map(course -> new BasketCourseOption(course.getId(), course.getName()))
                .toList();

        if (competitionId == null) {
            return new MappingEditorModel(competitions, null, basketCourses, basketCourseId, List.of(), List.of());
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        Long resolvedBasketCourseId = resolveBasketCourseId(basketCourseId, basketCourses);
        List<VariationOption> variationOptions = resolvedBasketCourseId == null
                ? List.of()
                : loadVariationOptions(resolvedBasketCourseId);
        List<RoundTable> roundTables = loadRoundTables(competition);

        return new MappingEditorModel(
                competitions,
                competition.getId(),
                basketCourses,
                resolvedBasketCourseId,
                variationOptions,
                roundTables
        );
    }

    @Transactional
    public void saveMappings(Long competitionId, Long basketCourseId, List<CellSubmission> submissions) {
        if (competitionId == null) {
            throw new IllegalArgumentException("Competition is required");
        }
        if (basketCourseId == null) {
            throw new IllegalArgumentException("Basket course is required");
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        BasketCourse basketCourse = basketCourseRepository.findById(basketCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Basket course not found"));

        Map<Long, Set<Integer>> editableSlots = editableSlotsByRoundDivision(competition);
        Set<Long> allowedVariationIds = allowedVariationIds(basketCourse);

        for (CellSubmission submission : submissions) {
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
            if (submission.basketVariationId() != null && !allowedVariationIds.contains(submission.basketVariationId())) {
                throw new IllegalArgumentException("Submitted basket variation does not belong to the selected basket course");
            }
        }

        Map<Long, RoundDivision> roundDivisionsById = roundDivisionRepository.findAllById(editableSlots.keySet()).stream()
                .collect(LinkedHashMap::new, (map, rd) -> map.put(rd.getId(), rd), Map::putAll);
        Map<Long, BasketVariation> variationsById = basketVariationRepository.findAllById(allowedVariationIds).stream()
                .collect(LinkedHashMap::new, (map, variation) -> map.put(variation.getId(), variation), Map::putAll);

        for (CellSubmission submission : submissions) {
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

    private Long resolveBasketCourseId(Long basketCourseId, List<BasketCourseOption> basketCourses) {
        if (basketCourseId != null) {
            return basketCourseId;
        }
        return basketCourses.isEmpty() ? null : basketCourses.get(0).id();
    }

    private List<VariationOption> loadVariationOptions(Long basketCourseId) {
        BasketCourse basketCourse = basketCourseRepository.findById(basketCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Basket course not found"));
        List<Basket> baskets = basketRepository.findByBasketCourseOrderByIdAsc(basketCourse);
        if (baskets.isEmpty()) {
            return List.of();
        }
        return basketVariationRepository.findByBasketInForMapping(baskets).stream()
                .map(variation -> new VariationOption(
                        variation.getId(),
                        variationLabel(variation),
                        variation.getBasket().getId()
                ))
                .toList();
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

    private List<RoundTable> loadRoundTables(Competition competition) {
        List<RoundDivision> roundDivisions = roundDivisionRepository.findByCompetitionForMapping(competition);
        Map<Long, Set<Integer>> slotsByRoundDivision = new HashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            slotsByRoundDivision.put(roundDivision.getId(), deriveHoleOrdinals(roundDivision));
        }

        Map<String, Long> selectedVariationIds = selectedVariationIds(roundDivisions);
        Map<Long, List<RoundDivision>> roundDivisionsByRound = new LinkedHashMap<>();
        for (RoundDivision roundDivision : roundDivisions) {
            roundDivisionsByRound.computeIfAbsent(roundDivision.getRound().getId(), ignored -> new ArrayList<>())
                    .add(roundDivision);
        }

        List<RoundTable> tables = new ArrayList<>();
        for (List<RoundDivision> divisions : roundDivisionsByRound.values()) {
            Round round = divisions.get(0).getRound();
            List<DivisionColumn> columns = divisions.stream()
                    .map(roundDivision -> new DivisionColumn(
                            roundDivision.getId(),
                            divisionLabel(roundDivision),
                            divisionCopyKey(roundDivision),
                            slotsByRoundDivision.getOrDefault(roundDivision.getId(), Set.of())
                    ))
                    .toList();
            int maxHoleOrdinal = columns.stream()
                    .map(DivisionColumn::holeOrdinals)
                    .flatMap(Collection::stream)
                    .max(Integer::compareTo)
                    .orElse(0);
            List<Integer> holeOrdinals = IntStream.rangeClosed(1, maxHoleOrdinal).boxed().toList();
            tables.add(new RoundTable(round.getId(), roundLabel(round), columns, holeOrdinals, selectedVariationIds));
        }
        return tables;
    }

    private Map<Long, Set<Integer>> editableSlotsByRoundDivision(Competition competition) {
        Map<Long, Set<Integer>> slots = new HashMap<>();
        for (RoundDivision roundDivision : roundDivisionRepository.findByCompetitionForMapping(competition)) {
            slots.put(roundDivision.getId(), deriveHoleOrdinals(roundDivision));
        }
        return slots;
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
            List<VariationOption> variationOptions,
            List<RoundTable> roundTables
    ) {
        public boolean hasCompetitionSelection() {
            return selectedCompetitionId != null;
        }

        public boolean hasBasketCourseSelection() {
            return selectedBasketCourseId != null;
        }
    }

    public record CompetitionOption(Long id, String label) {
    }

    public record BasketCourseOption(Long id, String name) {
    }

    public record VariationOption(Long id, String label, Long basketId) {
    }

    public record RoundTable(
            Long roundId,
            String label,
            List<DivisionColumn> divisions,
            List<Integer> holeOrdinals,
            Map<String, Long> selectedVariationIds
    ) {
        public Long selectedVariationId(Long roundDivisionId, Integer holeOrdinal) {
            return selectedVariationIds.get(cellKey(roundDivisionId, holeOrdinal));
        }
    }

    public record DivisionColumn(Long roundDivisionId, String label, String copyKey, Set<Integer> holeOrdinals) {
        public boolean hasHole(Integer holeOrdinal) {
            return holeOrdinals.contains(holeOrdinal);
        }
    }

    public record CellSubmission(Long roundDivisionId, Integer holeOrdinal, Long basketVariationId) {
    }
}
