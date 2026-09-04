package com.datascience.repository;

import com.datascience.domain.BasketVariationRoundDivision;
import com.datascience.domain.RoundDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BasketVariationRoundDivisionRepository extends JpaRepository<BasketVariationRoundDivision, Long> {

    @Query("""
            select distinct rd.round.competition.id
            from BasketVariationRoundDivision mapping
            join mapping.roundDivision rd
            where rd.round.competition.id is not null
            """)
    Set<Long> findMappedCompetitionIds();

    List<BasketVariationRoundDivision> findByRoundDivisionIn(Collection<RoundDivision> roundDivisions);

    Optional<BasketVariationRoundDivision> findByRoundDivisionAndHoleOrdinal(
            RoundDivision roundDivision,
            Integer holeOrdinal
    );

    void deleteByRoundDivisionAndHoleOrdinal(RoundDivision roundDivision, Integer holeOrdinal);
}
