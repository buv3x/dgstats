package com.datascience.repository;

import com.datascience.domain.Competition;
import com.datascience.domain.CompetitionDivision;
import com.datascience.domain.Round;
import com.datascience.domain.RoundDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoundDivisionRepository extends JpaRepository<RoundDivision, Long> {

    Optional<RoundDivision> findByRoundAndCompetitionDivision(Round round, CompetitionDivision competitionDivision);

    @Query("""
            select rd from RoundDivision rd
            join fetch rd.round r
            left join fetch rd.competitionDivision cd
            left join fetch rd.layout l
            where r.competition = :competition
            order by r.number asc, r.id asc, cd.code asc, rd.divisionCode asc, rd.id asc
            """)
    List<RoundDivision> findByCompetitionForMapping(Competition competition);
}
