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
@Table(schema = "datas", name = "layout_hole")
@Getter
@Setter
public class LayoutHole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private Layout layout;

    @Column(name = "hole_ordinal", nullable = false)
    private Integer holeOrdinal;

    @Column(name = "hole_code")
    private String holeCode;

    @Column(name = "label")
    private String label;

    @Column(name = "par")
    private Integer par;

    @Column(name = "length")
    private Integer length;
}
