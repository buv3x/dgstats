package com.datascience.repository;

import com.datascience.domain.HoleScore;
import com.datascience.domain.RoundResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HoleScoreRepository extends JpaRepository<HoleScore, Long> {

    Optional<HoleScore> findByRoundResultAndHoleOrdinal(RoundResult roundResult, Integer holeOrdinal);

    List<HoleScore> findByRoundResultOrderByHoleOrdinalAsc(RoundResult roundResult);

    @Query("""
            select
                c.id as competitionId,
                c.name as competitionName,
                bc.id as basketCourseId,
                bc.name as basketCourseName,
                b.id as basketId,
                b.name as basketName,
                bv.id as variationId,
                bv.name as variationName,
                bv.distance as variationDistance,
                rr.rating as rating,
                hs.score as score
            from HoleScore hs
            join hs.roundResult rr
            join rr.roundDivision rd
            join rd.round r
            join r.competition c
            left join BasketVariationRoundDivision mapping
                on mapping.roundDivision = rd
                and mapping.holeOrdinal = hs.holeOrdinal
            left join mapping.basketVariation bv
            left join bv.basket b
            left join b.basketCourse bc
            where hs.score is not null
            order by bc.name asc, bc.id asc, c.name asc, c.id asc, b.id asc, bv.id asc
            """)
    List<StatisticsExportRow> findStatisticsExportRows();

    interface StatisticsExportRow {
        Long getCompetitionId();

        String getCompetitionName();

        Long getBasketCourseId();

        String getBasketCourseName();

        Long getBasketId();

        String getBasketName();

        Long getVariationId();

        String getVariationName();

        Integer getVariationDistance();

        Integer getRating();

        Integer getScore();
    }
}
