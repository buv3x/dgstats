package com.datascience.repository;

import com.datascience.domain.Competition;
import com.datascience.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Long> {

    List<Round> findByCompetitionOrderByNumber(Competition competition);

    Optional<Round> findByCompetitionAndNumber(Competition competition, Integer number);
}
