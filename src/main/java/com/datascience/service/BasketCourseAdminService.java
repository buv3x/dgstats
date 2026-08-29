package com.datascience.service;

import com.datascience.domain.Basket;
import com.datascience.domain.BasketCourse;
import com.datascience.domain.BasketVariation;
import com.datascience.repository.BasketCourseRepository;
import com.datascience.repository.BasketRepository;
import com.datascience.repository.BasketVariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketCourseAdminService {

    private final BasketCourseRepository basketCourseRepository;
    private final BasketRepository basketRepository;
    private final BasketVariationRepository basketVariationRepository;

    @Transactional(readOnly = true)
    public List<BasketCourse> listCourses() {
        return basketCourseRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public BasketCourse getCourse(Long courseId) {
        return basketCourseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Basket course not found"));
    }

    @Transactional(readOnly = true)
    public List<Basket> listBaskets(Long courseId) {
        BasketCourse course = getCourse(courseId);
        return basketRepository.findByBasketCourseOrderByIdAsc(course);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<BasketVariation>> listVariationsByBasket(Long courseId) {
        return listBaskets(courseId).stream()
                .collect(Collectors.toMap(
                        Basket::getId,
                        basketVariationRepository::findByBasketOrderByIdAsc
                ));
    }

    @Transactional
    public BasketCourse createCourse(String name) {
        BasketCourse course = new BasketCourse();
        course.setName(requireName(name, "Course name is required"));
        return basketCourseRepository.save(course);
    }

    @Transactional
    public Basket createBasket(Long courseId, String name) {
        Basket basket = new Basket();
        basket.setBasketCourse(getCourse(courseId));
        basket.setName(requireName(name, "Basket name is required"));
        return basketRepository.save(basket);
    }

    @Transactional
    public Basket updateBasket(Long courseId, Long basketId, String name) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));
        requireBasketInCourse(courseId, basket);
        basket.setName(requireName(name, "Basket name is required"));
        return basketRepository.save(basket);
    }

    @Transactional
    public BasketVariation createVariation(Long courseId, Long basketId, String name, String distance) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));
        requireBasketInCourse(courseId, basket);

        BasketVariation variation = new BasketVariation();
        variation.setBasket(basket);
        variation.setName(requireName(name, "Variation name is required"));
        variation.setDistance(requireDistance(distance));
        return basketVariationRepository.save(variation);
    }

    @Transactional
    public BasketVariation updateVariation(Long courseId, Long basketId, Long variationId, String name, String distance) {
        BasketVariation variation = basketVariationRepository.findById(variationId)
                .orElseThrow(() -> new IllegalArgumentException("Basket variation not found"));
        requireBasketInCourse(courseId, variation.getBasket());
        if (!variation.getBasket().getId().equals(basketId)) {
            throw new IllegalArgumentException("Basket variation does not belong to the selected basket");
        }

        variation.setName(requireName(name, "Variation name is required"));
        variation.setDistance(requireDistance(distance));
        return basketVariationRepository.save(variation);
    }

    private String requireName(String name, String message) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return name.trim();
    }

    private Integer requireDistance(String distance) {
        if (distance == null || distance.isBlank()) {
            throw new IllegalArgumentException("Distance must be greater than zero");
        }
        try {
            int parsedDistance = Integer.parseInt(distance.trim());
            if (parsedDistance > 0) {
                return parsedDistance;
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Distance must be a whole number");
        }
        throw new IllegalArgumentException("Distance must be greater than zero");
    }

    private void requireBasketInCourse(Long courseId, Basket basket) {
        if (basket.getBasketCourse() == null || !basket.getBasketCourse().getId().equals(courseId)) {
            throw new IllegalArgumentException("Basket does not belong to the selected course");
        }
    }
}
