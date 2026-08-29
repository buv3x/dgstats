package com.datascience.repository;

import com.datascience.domain.Competition;
import com.datascience.domain.CompetitionDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetitionDivisionRepository extends JpaRepository<CompetitionDivision, Long> {

    List<CompetitionDivision> findByCompetition(Competition competition);

    Optional<CompetitionDivision> findByCompetitionAndCode(Competition competition, String code);
}
