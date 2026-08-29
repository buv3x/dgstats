package com.datascience.repository;

import com.datascience.domain.Player;
import com.datascience.domain.RoundDivision;
import com.datascience.domain.RoundResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoundResultRepository extends JpaRepository<RoundResult, Long> {

    Optional<RoundResult> findByPdgaScoreId(Long pdgaScoreId);

    Optional<RoundResult> findByRoundDivisionAndPlayer(RoundDivision roundDivision, Player player);

    Optional<RoundResult> findFirstByRoundDivisionOrderByIdAsc(RoundDivision roundDivision);
}
