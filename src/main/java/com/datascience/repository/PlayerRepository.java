package com.datascience.repository;

import com.datascience.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByPdgaNum(Long pdgaNum);

    Optional<Player> findFirstByNameAndCityAndCountry(String name, String city, String country);
}
