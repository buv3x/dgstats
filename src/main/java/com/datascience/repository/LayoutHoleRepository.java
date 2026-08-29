package com.datascience.repository;

import com.datascience.domain.Layout;
import com.datascience.domain.LayoutHole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LayoutHoleRepository extends JpaRepository<LayoutHole, Long> {

    Optional<LayoutHole> findByLayoutAndHoleOrdinal(Layout layout, Integer holeOrdinal);
}
