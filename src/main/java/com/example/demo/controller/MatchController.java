package com.example.demo.controller;

import com.example.demo.dto.MatchResponse;
import com.example.demo.dto.MatchResultRequest;
import com.example.demo.dto.StandingResponse;
import com.example.demo.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * POST /api/matches/result
     * Maç sonucunu girer ve puan tablosunu günceller.
     * Örnek body: { "matchId": 1, "homeScore": 3, "awayScore": 1 }
     */
    @PostMapping("/result")
    public ResponseEntity<MatchResponse> submitResult(
            @Valid @RequestBody MatchResultRequest request) {
        return ResponseEntity.ok(matchService.submitResult(request));
    }

    /**
     * GET /api/matches/tournament/{tournamentId}
     * Turnuvaya ait tüm maçları listeler.
     */
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchResponse>> getMatchesByTournament(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    /**
     * GET /api/matches/standings/{tournamentId}
     * Turnuvanın puan tablosunu puana göre sıralı döndürür.
     */
    @GetMapping("/standings/{tournamentId}")
    public ResponseEntity<List<StandingResponse>> getStandings(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getStandings(tournamentId));
    }
}
