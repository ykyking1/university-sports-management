package com.example.demo.controller;

import com.example.demo.dto.TournamentCreateRequest;
import com.example.demo.entity.Match;
import com.example.demo.entity.Tournament;
import com.example.demo.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<Tournament> createTournament(
            @Valid @RequestBody TournamentCreateRequest request) {
        return new ResponseEntity<>(tournamentService.createTournament(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAll() {
        return ResponseEntity.ok(tournamentService.getAll());
    }

    @PostMapping("/{id}/teams/{teamId}")
    public ResponseEntity<Void> addTeam(
            @PathVariable Long id,
            @PathVariable Long teamId) {
        tournamentService.addTeamToTournament(id, teamId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/generate-schedule")
    public ResponseEntity<String> generateSchedule(@PathVariable Long id) {
        try {
            tournamentService.generateSchedule(id);
            return ResponseEntity.ok("Fikstür başarıyla oluşturuldu.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<Match>> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getSchedule(id));
    }

    // Turnuva sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}