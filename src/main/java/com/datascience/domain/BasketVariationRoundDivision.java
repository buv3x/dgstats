package com.datascience.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "datas", name = "basket_variation_round_division")
@Getter
@Setter
public class BasketVariationRoundDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_division_id", nullable = false)
    private RoundDivision roundDivision;

    @Column(name = "hole_ordinal", nullable = false)
    private Integer holeOrdinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_variation_id", nullable = false)
    private BasketVariation basketVariation;
}
