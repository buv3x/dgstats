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
@Table(schema = "datas", name = "round_result_throws")
@Getter
@Setter
public class RoundResultThrows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_result_id", nullable = false)
    private RoundResult roundResult;

    @Column(name = "hole_ordinal", nullable = false)
    private Integer holeOrdinal;

    @Column(name = "score_ordinal", nullable = false)
    private Integer scoreOrdinal;

    @Column(name = "penalty_count", nullable = false)
    private Integer penaltyCount;

    @Column(name = "throw_id")
    private Long throwId;

    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "distance_to_target")
    private Integer distanceToTarget;

    @Column(name = "penalty_type")
    private String penaltyType;

    @Column(name = "drop_zone_id")
    private Long dropZoneId;

    @Column(name = "drop_distance_to_target")
    private Integer dropDistanceToTarget;
}
