package com.example.demo.service;

import com.example.demo.entity.Match;
import com.example.demo.entity.Team;
import com.example.demo.entity.Tournament;
import com.example.demo.enums.MatchStatus;
import com.example.demo.enums.TournamentFormat;
import com.example.demo.repository.MatchRepository;
import com.example.demo.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public void generateSchedule(Long tournamentId)
    {
        // 1. Turnuvayı veritabanından çek
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Turnuva bulunamadı"));

        // 2. Daha önce oluşturulmuş mu kontrol et
        if (tournament.isScheduleGenerated()) {
            throw new RuntimeException("Bu turnuvanın fikstürü zaten oluşturulmuş.");
        }

        List<Team> teams = tournament.getTeams(); //
        
        // 3. Formata göre ilgili algoritmayı çağır
        if (tournament.getFormat() == TournamentFormat.ROUND_ROBIN) {
            createRoundRobinSchedule(tournament, teams);
        } else if (tournament.getFormat() == TournamentFormat.SINGLE_ELIMINATION) {
            createSingleEliminationSchedule(tournament, teams);
        }

        // 4. Durumu güncelle ve kaydet
        tournament.setScheduleGenerated(true);
        tournamentRepository.save(tournament);
    }



    private void createRoundRobinSchedule(Tournament tournament, List<Team> teams)
    {
        List<Team> teamList = new ArrayList<>(teams);
    
        // Takım sayısı tekse, bir "null" ekleyerek çift yapıyoruz (BAY durumu)
        if (teamList.size() % 2 != 0) {
            teamList.add(null);
        }

        int totalTeams = teamList.size();
        int totalRounds = totalTeams - 1;
        int matchesPerRound = totalTeams / 2;

        for (int round = 1; round <= totalRounds; round++) {
            for (int i = 0; i < matchesPerRound; i++) {
                Team home = teamList.get(i);
                Team away = teamList.get(totalTeams - 1 - i);

                // Eğer her iki taraf da null değilse (gerçek bir maçsa) kaydet
                if (home != null && away != null) {
                    saveMatch(tournament, home, away, round);
                }
            }
            // Circle Algorithm: İlk takımı sabit tutup diğerlerini döndür
            Collections.rotate(teamList.subList(1, teamList.size()), 1);
        }
    }


    private void createSingleEliminationSchedule(Tournament tournament, List<Team> teams)
    {
        List<Team> teamList = new ArrayList<>(teams);
        Collections.shuffle(teamList); // Takımları karıştır

        // İlk tur eşleşmeleri (Sadece başlangıç turu için)
        for (int i = 0; i < teamList.size() - 1; i += 2) {
            saveMatch(tournament, teamList.get(i), teamList.get(i + 1), 1);
        }
    }

    private void saveMatch(Tournament tournament, Team home, Team away, int round)
    {
        Match match = new Match();
        match.setTournament(tournament);
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        match.setRoundNumber(round);
        match.setStatus(MatchStatus.SCHEDULED);
        matchRepository.save(match);
    }
}