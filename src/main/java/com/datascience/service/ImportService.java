package com.datascience.service;

import com.datascience.domain.Competition;
import com.datascience.domain.CompetitionDivision;
import com.datascience.domain.CompetitionImport;
import com.datascience.domain.Course;
import com.datascience.domain.HoleScore;
import com.datascience.domain.Layout;
import com.datascience.domain.LayoutHole;
import com.datascience.domain.Player;
import com.datascience.domain.Round;
import com.datascience.domain.RoundDivision;
import com.datascience.domain.RoundResult;
import com.datascience.dto.pdga.PdgaCompetitionInfo;
import com.datascience.dto.pdga.PdgaDivision;
import com.datascience.dto.pdga.PdgaLayout;
import com.datascience.dto.pdga.PdgaLayoutHole;
import com.datascience.dto.pdga.PdgaResponse;
import com.datascience.dto.pdga.PdgaRoundInfo;
import com.datascience.dto.pdga.PdgaRoundResults;
import com.datascience.dto.pdga.PdgaScore;
import com.datascience.repository.CompetitionDivisionRepository;
import com.datascience.repository.CompetitionImportRepository;
import com.datascience.repository.CompetitionRepository;
import com.datascience.repository.CourseRepository;
import com.datascience.repository.HoleScoreRepository;
import com.datascience.repository.LayoutHoleRepository;
import com.datascience.repository.LayoutRepository;
import com.datascience.repository.PlayerRepository;
import com.datascience.repository.RoundDivisionRepository;
import com.datascience.repository.RoundRepository;
import com.datascience.repository.RoundResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImportService {

    private static final Logger log = LoggerFactory.getLogger(ImportService.class);

    private final PdgaConnector pdgaConnector;
    private final TransactionTemplate transactionTemplate;
    private final CompetitionImportRepository competitionImportRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionDivisionRepository competitionDivisionRepository;
    private final CourseRepository courseRepository;
    private final RoundRepository roundRepository;
    private final LayoutRepository layoutRepository;
    private final LayoutHoleRepository layoutHoleRepository;
    private final PlayerRepository playerRepository;
    private final RoundDivisionRepository roundDivisionRepository;
    private final RoundResultRepository roundResultRepository;
    private final HoleScoreRepository holeScoreRepository;

    @Async
    public void importPendingCompetitions() {
        List<CompetitionImport> pendingImports = competitionImportRepository.findByImportedFalse();
        int imported = 0;
        for (CompetitionImport pendingImport : pendingImports) {
            try {
                importCompetition(pendingImport.getCompetitionId());
                imported++;
            } catch (RuntimeException ex) {
                log.error("Failed to import PDGA competition {}", pendingImport.getCompetitionId(), ex);
            }
        }
        log.info("Imported " + imported + " competitions");
    }

    private void importCompetition(Long tournamentId) {
        PdgaResponse<PdgaCompetitionInfo> response = pdgaConnector.getCompetitionInfo(tournamentId);
        PdgaCompetitionInfo info = requireData(response, "competition info", tournamentId);

        List<RoundDivisionImportTarget> importTargets = transactionTemplate.execute(status ->
                saveCompetitionMetadata(tournamentId, info));

        for (RoundDivisionImportTarget importTarget : safeList(importTargets)) {
            PdgaResponse<PdgaRoundResults> roundResponse = pdgaConnector.getRoundResults(
                    tournamentId,
                    importTarget.divisionCode(),
                    importTarget.roundNumber()
            );
            PdgaRoundResults results = requireData(roundResponse, "round results", tournamentId);
            transactionTemplate.executeWithoutResult(status -> saveRoundDivisionResults(importTarget, results));
        }

        transactionTemplate.executeWithoutResult(status -> markImported(tournamentId));
    }

    private List<RoundDivisionImportTarget> saveCompetitionMetadata(Long tournamentId, PdgaCompetitionInfo info) {
        Competition competition = saveCompetition(tournamentId, info);
        saveRounds(competition, info);
        saveDivisions(competition, info);
        saveLayouts(competition, info.getLayouts());

        List<Round> rounds = roundRepository.findByCompetitionOrderByNumber(competition);
        List<CompetitionDivision> divisions = competitionDivisionRepository.findByCompetition(competition);
        return rounds.stream()
                .filter(round -> round.getId() != null && round.getNumber() != null)
                .flatMap(round -> divisions.stream()
                        .filter(division -> division.getId() != null && division.getCode() != null)
                        .map(division -> new RoundDivisionImportTarget(
                                competition.getId(),
                                round.getId(),
                                division.getId(),
                                division.getCode(),
                                round.getNumber()
                        )))
                .toList();
    }

    private void markImported(Long tournamentId) {
        CompetitionImport competitionImport = competitionImportRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalStateException("Missing competition_import row for " + tournamentId));
        competitionImport.setImported(Boolean.TRUE);
        competitionImportRepository.save(competitionImport);
    }

    private void saveRoundDivisionResults(RoundDivisionImportTarget importTarget, PdgaRoundResults results) {
        Competition competition = competitionRepository.findById(importTarget.competitionId())
                .orElseThrow(() -> new IllegalStateException("Missing competition " + importTarget.competitionId()));
        Round round = roundRepository.findById(importTarget.roundId())
                .orElseThrow(() -> new IllegalStateException("Missing round " + importTarget.roundId()));
        CompetitionDivision division = competitionDivisionRepository.findById(importTarget.divisionId())
                .orElseThrow(() -> new IllegalStateException("Missing competition division " + importTarget.divisionId()));

        saveLayouts(competition, results.getLayouts());

        Layout layout = safeList(results.getLayouts()).stream()
                .map(PdgaLayout::getLayoutId)
                .filter(Objects::nonNull)
                .findFirst()
                .flatMap(layoutRepository::findByPdgaId)
                .orElseGet(() -> layoutRepository.findFirstByCompetition(competition).orElse(null));

        RoundDivision roundDivision = roundDivisionRepository.findByRoundAndCompetitionDivision(round, division)
                .orElseGet(RoundDivision::new);
        roundDivision.setRound(round);
        roundDivision.setCompetitionDivision(division);
        roundDivision.setDivisionCode(results.getDivision());
        roundDivision.setPdgaLiveRoundId(results.getLiveRoundId());
        roundDivision.setPool(results.getPool());
        roundDivision.setLayout(layout);
        RoundDivision savedRoundDivision = roundDivisionRepository.save(roundDivision);

        for (PdgaScore score : safeList(results.getScores())) {
            saveRoundResult(savedRoundDivision, score);
        }
    }

    private record RoundDivisionImportTarget(
            Long competitionId,
            Long roundId,
            Long divisionId,
            String divisionCode,
            Integer roundNumber
    ) {
    }

    private Competition saveCompetition(Long tournamentId, PdgaCompetitionInfo info) {
        Course primaryCourse = primaryCourse(info.getLayouts());
        Competition competition = competitionRepository.findByPdgaId(tournamentId).orElseGet(Competition::new);
        competition.setPdgaId(tournamentId);
        competition.setName(info.getName());
        competition.setSimpleName(info.getSimpleName());
        competition.setStartDate(info.getStartDate());
        competition.setEndDate(info.getEndDate());
        competition.setCountry(info.getCountry());
        competition.setLocation(info.getLocation());
        competition.setTier(info.getTier());
        competition.setTotalPlayers(info.getTotalPlayers());
        competition.setCourse(primaryCourse);
        return competitionRepository.save(competition);
    }

    private Course primaryCourse(List<PdgaLayout> layouts) {
        PdgaLayout firstLayout = safeList(layouts).stream()
                .filter(layout -> layout.getCourseId() != null || layout.getCourseName() != null)
                .findFirst()
                .orElse(null);
        if (firstLayout == null) {
            return courseRepository.findFirstByName("Unknown PDGA course").orElseGet(() -> {
                Course course = new Course();
                course.setName("Unknown PDGA course");
                return courseRepository.save(course);
            });
        }
        return saveCourse(firstLayout);
    }

    private Course saveCourse(PdgaLayout layout) {
        Course course = layout.getCourseId() != null
                ? courseRepository.findByPdgaId(layout.getCourseId()).orElseGet(Course::new)
                : courseRepository.findFirstByName(layout.getCourseName()).orElseGet(Course::new);
        course.setPdgaId(layout.getCourseId());
        course.setName(layout.getCourseName());
        return courseRepository.save(course);
    }

    private void saveDivisions(Competition competition, PdgaCompetitionInfo info) {
        for (PdgaDivision pdgaDivision : safeList(info.getDivisions())) {
            CompetitionDivision division = competitionDivisionRepository
                    .findByCompetitionAndCode(competition, pdgaDivision.getDivision())
                    .orElseGet(CompetitionDivision::new);
            division.setCompetition(competition);
            division.setPdgaDivisionId(pdgaDivision.getDivisionId());
            division.setCode(pdgaDivision.getDivision());
            division.setName(pdgaDivision.getDivisionName());
            division.setPlayers(pdgaDivision.getPlayers());
            division.setPro(pdgaDivision.getPro());
            division.setShortName(pdgaDivision.getShortName());
            division.setLatestRound(pdgaDivision.getLatestRound());
            competitionDivisionRepository.save(division);
        }
    }

    private void saveRounds(Competition competition, PdgaCompetitionInfo info) {
        List<PdgaRoundInfo> rounds = safeList(info.getRoundsList() == null
                ? null
                : info.getRoundsList().values().stream()
                .sorted(Comparator.comparing(PdgaRoundInfo::getNumber, Comparator.nullsLast(Integer::compareTo)))
                .toList());
        for (PdgaRoundInfo pdgaRound : rounds) {
            Round round = roundRepository.findByCompetitionAndNumber(competition, pdgaRound.getNumber())
                    .orElseGet(Round::new);
            round.setCompetition(competition);
            round.setNumber(pdgaRound.getNumber());
            round.setLabel(pdgaRound.getLabel());
            round.setLabelAbbreviated(pdgaRound.getLabelAbbreviated());
            round.setDate(pdgaRound.getDate());
            roundRepository.save(round);
        }
    }

    private void saveLayouts(Competition competition, List<PdgaLayout> pdgaLayouts) {
        for (PdgaLayout pdgaLayout : safeList(pdgaLayouts)) {
            saveLayout(competition, pdgaLayout);
        }
    }

    private Layout saveLayout(Competition competition, PdgaLayout pdgaLayout) {
        Course course = saveCourse(pdgaLayout);
        Layout layout = pdgaLayout.getLayoutId() != null
                ? layoutRepository.findByPdgaId(pdgaLayout.getLayoutId()).orElseGet(Layout::new)
                : layoutRepository.findFirstByCompetition(competition).orElseGet(Layout::new);
        layout.setCompetition(competition);
        layout.setCourse(course);
        layout.setPdgaId(pdgaLayout.getLayoutId());
        layout.setName(pdgaLayout.getName());
        layout.setHoles(pdgaLayout.getHoles());
        layout.setPar(pdgaLayout.getPar());
        layout.setLength(pdgaLayout.getLength());
        layout.setUnits(pdgaLayout.getUnits());
        layout.setAccuracy(pdgaLayout.getAccuracy());
        Layout savedLayout = layoutRepository.save(layout);

        for (PdgaLayoutHole pdgaHole : safeList(pdgaLayout.getHoleDetails())) {
            Integer ordinal = pdgaHole.resolvedOrdinal();
            if (ordinal == null) {
                ordinal = parseHoleOrdinal(pdgaHole.getHole());
            }
            if (ordinal != null) {
                saveLayoutHole(savedLayout, pdgaHole, ordinal);
            }
        }
        return savedLayout;
    }

    private void saveLayoutHole(Layout layout, PdgaLayoutHole pdgaHole, Integer ordinal) {
        LayoutHole hole = layoutHoleRepository.findByLayoutAndHoleOrdinal(layout, ordinal).orElseGet(LayoutHole::new);
        hole.setLayout(layout);
        hole.setHoleOrdinal(ordinal);
        hole.setHoleCode(pdgaHole.getHole());
        hole.setLabel(pdgaHole.getLabel());
        hole.setPar(pdgaHole.getPar());
        hole.setLength(pdgaHole.getLength());
        layoutHoleRepository.save(hole);
    }

    private void saveRoundResult(RoundDivision roundDivision, PdgaScore score) {
        Player player = savePlayer(score);
        RoundResult roundResult = score.getScoreId() != null
                ? roundResultRepository.findByPdgaScoreId(score.getScoreId()).orElseGet(RoundResult::new)
                : roundResultRepository.findByRoundDivisionAndPlayer(roundDivision, player).orElseGet(RoundResult::new);

        Layout layout = score.getLayoutId() == null
                ? roundDivision.getLayout()
                : layoutRepository.findByPdgaId(score.getLayoutId()).orElse(roundDivision.getLayout());

        roundResult.setRoundDivision(roundDivision);
        roundResult.setPlayer(player);
        roundResult.setPdgaResultId(score.getResultId());
        roundResult.setPdgaRoundId(score.getRoundId());
        roundResult.setPdgaScoreId(score.getScoreId());
        roundResult.setLayout(layout);
        roundResult.setRating(score.getRating());
        roundResult.setRoundScore(score.getRoundScore());
        roundResult.setRoundToPar(score.getRoundToPar());
        roundResult.setGrandTotal(score.getGrandTotal());
        roundResult.setTotalToPar(score.getToPar());
        roundResult.setRoundRating(score.getRoundRating());
        roundResult.setPreviousPlace(score.getPreviousPlace());
        roundResult.setRunningPlace(score.getRunningPlace());
        roundResult.setTied(score.getTied());
        roundResult.setCompleted(score.getCompleted() == null ? null : score.getCompleted() == 1);
        roundResult.setPlayed(score.getPlayed());
        RoundResult savedRoundResult = roundResultRepository.save(roundResult);

        saveHoleScores(savedRoundResult, layout, score.getHoleScores());
    }

    private Player savePlayer(PdgaScore score) {
        Player player = score.getPdgaNum() != null
                ? playerRepository.findByPdgaNum(score.getPdgaNum()).orElseGet(Player::new)
                : playerRepository.findFirstByNameAndCityAndCountry(score.getName(), score.getCity(), score.getCountry()).orElseGet(Player::new);
        player.setPdgaNum(score.getPdgaNum());
        player.setFirstName(score.getFirstName());
        player.setLastName(score.getLastName());
        player.setName(score.getName());
        player.setCity(score.getCity());
        player.setCountry(score.getCountry());
        player.setNationality(score.getNationality());
        player.setProfileUrl(score.getProfileUrl());
        return playerRepository.save(player);
    }

    private void saveHoleScores(RoundResult roundResult, Layout layout, List<String> holeScores) {
        int ordinal = 1;
        for (String rawScore : safeList(holeScores)) {
            Integer parsedScore = parseInteger(rawScore);
            if (parsedScore != null) {
                HoleScore holeScore = holeScoreRepository.findByRoundResultAndHoleOrdinal(roundResult, ordinal)
                        .orElseGet(HoleScore::new);
                holeScore.setRoundResult(roundResult);
                holeScore.setHoleOrdinal(ordinal);
                holeScore.setScore(parsedScore);
                holeScore.setPar(layout == null ? null : layoutHoleRepository
                        .findByLayoutAndHoleOrdinal(layout, ordinal)
                        .map(LayoutHole::getPar)
                        .orElse(null));
                holeScoreRepository.save(holeScore);
            }
            ordinal++;
        }
    }

    private <T> T requireData(PdgaResponse<T> response, String label, Long tournamentId) {
        if (response == null || response.getData() == null) {
            throw new IllegalStateException("PDGA " + label + " response had no data for tournament " + tournamentId);
        }
        return response.getData();
    }

    private Integer parseHoleOrdinal(String holeCode) {
        if (holeCode == null || !holeCode.startsWith("H")) {
            return null;
        }
        return parseInteger(holeCode.substring(1));
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
