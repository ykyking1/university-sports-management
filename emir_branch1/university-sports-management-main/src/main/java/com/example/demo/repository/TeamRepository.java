package com.example.demo.repository;

import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // Takım isminin eşsiz olup olmadığını kontrol eder
    boolean existsByName(String name);

    // Bir oyuncunun (User ID bazlı) herhangi bir takımda olup olmadığını kontrol eder
    boolean existsByPlayersId(Long userId);
}