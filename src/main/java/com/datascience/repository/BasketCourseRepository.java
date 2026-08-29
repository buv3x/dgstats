package com.datascience.repository;

import com.datascience.domain.BasketCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketCourseRepository extends JpaRepository<BasketCourse, Long> {

    List<BasketCourse> findAllByOrderByNameAsc();
}
