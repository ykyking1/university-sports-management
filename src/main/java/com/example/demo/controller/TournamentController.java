package com.example.demo.controller;

import com.example.demo.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Frontend'in bağlanabilmesi için CORS izni
public class TournamentController {

    private final TournamentService tournamentService;

    // POST http://localhost:8080/api/tournaments/{id}/generate-schedule
    @PostMapping("/{id}/generate-schedule")
    public ResponseEntity<String> generateSchedule(@PathVariable Long id)
    {
        try{
            tournamentService.generateSchedule(id);
            return ResponseEntity.ok("Fikstür başarıyla oluşturuldu.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }
}