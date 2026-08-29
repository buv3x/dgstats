package com.datascience.repository;

import com.datascience.domain.Competition;
import com.datascience.domain.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {

    Optional<Layout> findByPdgaId(Long pdgaId);

    Optional<Layout> findFirstByCompetition(Competition competition);
}
