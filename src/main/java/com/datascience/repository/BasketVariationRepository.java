package com.datascience.repository;

import com.datascience.domain.Basket;
import com.datascience.domain.BasketVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BasketVariationRepository extends JpaRepository<BasketVariation, Long> {

    List<BasketVariation> findByBasketOrderByIdAsc(Basket basket);

    @Query("""
            select bv from BasketVariation bv
            join fetch bv.basket b
            where b in :baskets
            order by b.id asc, bv.id asc
            """)
    List<BasketVariation> findByBasketInForMapping(Collection<Basket> baskets);
}
