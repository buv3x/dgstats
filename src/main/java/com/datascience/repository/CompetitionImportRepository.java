package com.datascience.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datascience.domain.CompetitionImport;

import java.util.List;

public interface CompetitionImportRepository extends JpaRepository<CompetitionImport, Long> {

    List<CompetitionImport> findByImportedFalse();
}
