package com.example.demo.repository;

import com.example.demo.entity.Standing;
import com.example.demo.entity.Team;
import com.example.demo.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingRepository extends JpaRepository<Standing, Long> {
    Optional<Standing> findByTournamentAndTeam(Tournament tournament, Team team);
    List<Standing> findByTournamentOrderByPointsDesc(Tournament tournament);
}