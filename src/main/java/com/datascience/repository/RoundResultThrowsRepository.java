package com.datascience.repository;

import com.datascience.domain.RoundResult;
import com.datascience.domain.RoundResultThrows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoundResultThrowsRepository extends JpaRepository<RoundResultThrows, Long> {

    Optional<RoundResultThrows> findByRoundResultAndHoleOrdinalAndScoreOrdinal(
            RoundResult roundResult,
            Integer holeOrdinal,
            Integer scoreOrdinal
    );
}
