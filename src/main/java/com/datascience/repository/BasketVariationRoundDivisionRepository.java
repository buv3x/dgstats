package com.datascience.repository;

import com.datascience.domain.BasketVariationRoundDivision;
import com.datascience.domain.RoundDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BasketVariationRoundDivisionRepository extends JpaRepository<BasketVariationRoundDivision, Long> {

    List<BasketVariationRoundDivision> findByRoundDivisionIn(Collection<RoundDivision> roundDivisions);

    Optional<BasketVariationRoundDivision> findByRoundDivisionAndHoleOrdinal(
            RoundDivision roundDivision,
            Integer holeOrdinal
    );

    void deleteByRoundDivisionAndHoleOrdinal(RoundDivision roundDivision, Integer holeOrdinal);
}
