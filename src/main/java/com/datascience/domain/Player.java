package com.datascience.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "datas", name = "player")
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pdga_num")
    private Long pdgaNum;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "profile_url")
    private String profileUrl;
}
