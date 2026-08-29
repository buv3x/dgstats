package com.datascience.repository;

import com.datascience.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByPdgaId(Long pdgaId);

    Optional<Course> findFirstByName(String name);
}
