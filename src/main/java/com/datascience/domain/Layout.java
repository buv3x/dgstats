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
@Table(schema = "datas", name = "layout")
@Getter
@Setter
public class Layout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "pdga_id")
    private Long pdgaId;

    @Column(name = "name")
    private String name;

    @Column(name = "holes")
    private Integer holes;

    @Column(name = "par")
    private Integer par;

    @Column(name = "length")
    private Integer length;

    @Column(name = "units")
    private String units;

    @Column(name = "accuracy")
    private String accuracy;
}
