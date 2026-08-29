package com.datascience.repository;

import com.datascience.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    Optional<Competition> findByPdgaId(Long pdgaId);

    @Query("select c from Competition c order by case when c.startDate is null then 1 else 0 end, c.startDate desc, c.id desc")
    List<Competition> findAllForMappingOrder();
}
