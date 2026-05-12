package com.example.demo.controller;

import com.example.demo.dto.TeamRegistrationRequest;
import com.example.demo.dto.TeamResponse;
import com.example.demo.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // Takım Oluşturma
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRegistrationRequest request) {
        return new ResponseEntity<>(teamService.createTeam(request), HttpStatus.CREATED);
    }

    // Oyuncu Ekleme
    @PostMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<Void> addPlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    // Oyuncu Çıkarma
    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<Void> removePlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getById(id));
    }

    // Takım sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
