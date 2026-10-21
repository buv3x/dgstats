package com.datascience.repository;

import com.datascience.domain.RoundResult;
import com.datascience.domain.RoundResultThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoundResultThrowsRepository extends JpaRepository<RoundResultThrows, Long> {

    Optional<RoundResultThrows> findByRoundResultAndHoleOrdinalAndScoreOrdinal(
            RoundResult roundResult,
            Integer holeOrdinal,
            Integer scoreOrdinal
    );

    @Query("""
            select
                rr.id as roundResultId,
                p.id as playerId,
                p.name as playerName,
                p.pdgaNum as playerPdgaNum,
                r.id as roundId,
                r.date as roundDate,
                c.simpleName as competitionSimpleName,
                c.name as competitionName,
                r.labelAbbreviated as roundAbbreviatedLabel,
                r.label as roundLabel,
                coalesce(resultLayout.holes, divisionLayout.holes) as expectedHoleCount,
                rrt.holeOrdinal as holeOrdinal,
                rrt.scoreOrdinal as scoreOrdinal,
                rrt.penaltyCount as penaltyCount,
                rrt.zoneId as zoneId,
                rrt.distanceToTarget as distanceToTarget,
                rrt.dropZoneId as dropZoneId,
                rrt.dropDistanceToTarget as dropDistanceToTarget
            from RoundResult rr
            join rr.player p
            join rr.roundDivision rd
            join rd.round r
            join r.competition c
            left join rr.layout resultLayout
            left join rd.layout divisionLayout
            left join RoundResultThrows rrt on rrt.roundResult = rr
            order by p.id asc, rr.id asc, rrt.holeOrdinal asc, rrt.scoreOrdinal asc
            """)
    List<PuttingThrowRow> findPuttingThrowRows();

    interface PuttingThrowRow {
        Long getRoundResultId();

        Long getPlayerId();

        String getPlayerName();

        Long getPlayerPdgaNum();

        Long getRoundId();

        java.time.LocalDate getRoundDate();

        String getCompetitionSimpleName();

        String getCompetitionName();

        String getRoundAbbreviatedLabel();

        String getRoundLabel();

        Integer getExpectedHoleCount();

        Integer getHoleOrdinal();

        Integer getScoreOrdinal();

        Integer getPenaltyCount();

        Long getZoneId();

        Integer getDistanceToTarget();

        Long getDropZoneId();

        Integer getDropDistanceToTarget();
    }
}
