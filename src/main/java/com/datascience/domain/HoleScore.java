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
@Table(schema = "datas", name = "hole_score")
@Getter
@Setter
public class HoleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_result_id", nullable = false)
    private RoundResult roundResult;

    @Column(name = "hole_ordinal", nullable = false)
    private Integer holeOrdinal;

    @Column(name = "score")
    private Integer score;

    @Column(name = "par")
    private Integer par;
}
