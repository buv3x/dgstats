package com.datascience.repository;

import com.datascience.domain.Basket;
import com.datascience.domain.BasketCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    List<Basket> findByBasketCourseOrderByIdAsc(BasketCourse basketCourse);
}
