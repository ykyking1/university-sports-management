package com.example.demo.service;

import com.example.demo.dto.MatchResponse;
import com.example.demo.dto.MatchResultRequest;
import com.example.demo.dto.StandingResponse;
import com.example.demo.entity.Match;
import com.example.demo.entity.Standing;
import com.example.demo.entity.Tournament;
import com.example.demo.enums.MatchStatus;
import com.example.demo.enums.TournamentFormat;
import com.example.demo.repository.MatchRepository;
import com.example.demo.repository.StandingRepository;
import com.example.demo.repository.TournamentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    private final TournamentRepository tournamentRepository;

    /**
     * Maç sonucunu kaydeder ve puan tablosunu günceller.
     * COMPLETED durumundaki maçlar tekrar güncellenemez.
     */
    @Transactional
    public MatchResponse submitResult(MatchResultRequest request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Maç bulunamadı, id: " + request.getMatchId()));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            throw new RuntimeException("Bu maçın sonucu zaten girilmiş, tekrar güncellenemez.");
        }

        if (match.getStatus() == MatchStatus.CANCELLED) {
            throw new RuntimeException("İptal edilmiş bir maça sonuç girilemez.");
        }

        // Skorları kaydet
        match.setHomeScore(request.getHomeScore());
        match.setAwayScore(request.getAwayScore());
        match.setStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);

        // Puan tablosunu güncelle
        updateStandings(match);

        // Single Elimination ise kaybeden takımı eleme kontrolü yapılabilir
        // (Kişi 3'ün oluşturduğu maç listesini bozmamak için sadece standing güncelliyoruz)

        return toMatchResponse(match);
    }

    /**
     * Turnuvaya ait tüm maçları döndürür.
     */
    public List<MatchResponse> getMatchesByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Turnuva bulunamadı, id: " + tournamentId));

        return matchRepository.findByTournament(tournament)
                .stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    /**
     * Turnuvaya ait puan tablosunu puana göre sıralı döndürür.
     */
    public List<StandingResponse> getStandings(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Turnuva bulunamadı, id: " + tournamentId));

        return standingRepository.findByTournamentOrderByPointsDesc(tournament)
                .stream()
                .map(this::toStandingResponse)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // Private Helpers
    // ----------------------------------------------------------------

    /**
     * Maç sonucuna göre hem ev sahibi hem deplasman takımının standing'ini günceller.
     * Round Robin: beraberlik 1 puan, galibiyet 3 puan
     * Single Elimination: beraberlik olmaz; eşitlik durumunda ev sahibi kazanır (opsiyonel kural)
     */
    private void updateStandings(Match match) {
        Tournament tournament = match.getTournament();
        boolean isElimination = tournament.getFormat() == TournamentFormat.SINGLE_ELIMINATION;

        int homeGoals = match.getHomeScore();
        int awayGoals = match.getAwayScore();

        Standing homeStanding = getOrCreateStanding(tournament, match.getHomeTeam());
        Standing awayStanding = getOrCreateStanding(tournament, match.getAwayTeam());

        // Oynanan maç sayısını artır
        homeStanding.setPlayed(homeStanding.getPlayed() + 1);
        awayStanding.setPlayed(awayStanding.getPlayed() + 1);

        // Gol istatistiklerini güncelle
        homeStanding.setGoalsScored(homeStanding.getGoalsScored() + homeGoals);
        homeStanding.setGoalsConceded(homeStanding.getGoalsConceded() + awayGoals);
        awayStanding.setGoalsScored(awayStanding.getGoalsScored() + awayGoals);
        awayStanding.setGoalsConceded(awayStanding.getGoalsConceded() + homeGoals);

        if (homeGoals > awayGoals) {
            // Ev sahibi kazandı
            homeStanding.setWins(homeStanding.getWins() + 1);
            homeStanding.setPoints(homeStanding.getPoints() + 3);
            awayStanding.setLosses(awayStanding.getLosses() + 1);

        } else if (awayGoals > homeGoals) {
            // Deplasman kazandı
            awayStanding.setWins(awayStanding.getWins() + 1);
            awayStanding.setPoints(awayStanding.getPoints() + 3);
            homeStanding.setLosses(homeStanding.getLosses() + 1);

        } else {
            // Beraberlik
            if (isElimination) {
                // Eleme formatında beraberlik olmaz: ev sahibi kazanır (golden goal kuralı)
                homeStanding.setWins(homeStanding.getWins() + 1);
                homeStanding.setPoints(homeStanding.getPoints() + 3);
                awayStanding.setLosses(awayStanding.getLosses() + 1);
            } else {
                // Round Robin: 1'er puan
                homeStanding.setDraws(homeStanding.getDraws() + 1);
                homeStanding.setPoints(homeStanding.getPoints() + 1);
                awayStanding.setDraws(awayStanding.getDraws() + 1);
                awayStanding.setPoints(awayStanding.getPoints() + 1);
            }
        }

        standingRepository.save(homeStanding);
        standingRepository.save(awayStanding);
    }

    /**
     * Turnuva + takım ikilisi için Standing yoksa yeni oluşturur, varsa getirir.
     */
    private Standing getOrCreateStanding(Tournament tournament, com.example.demo.entity.Team team) {
        return standingRepository.findByTournamentAndTeam(tournament, team)
                .orElseGet(() -> {
                    Standing s = new Standing();
                    s.setTournament(tournament);
                    s.setTeam(team);
                    return s;
                });
    }

    private MatchResponse toMatchResponse(Match match) {
        MatchResponse response = new MatchResponse();
        response.setId(match.getId());
        response.setHomeTeamName(match.getHomeTeam().getName());
        response.setAwayTeamName(match.getAwayTeam().getName());
        response.setHomeScore(match.getHomeScore());
        response.setAwayScore(match.getAwayScore());
        response.setRoundNumber(match.getRoundNumber());
        response.setStatus(match.getStatus());
        response.setScheduledTime(match.getScheduledTime());
        return response;
    }

    private StandingResponse toStandingResponse(Standing standing) {
        StandingResponse response = new StandingResponse();
        response.setTeamId(standing.getTeam().getId());
        response.setTeamName(standing.getTeam().getName());
        response.setPlayed(standing.getPlayed());
        response.setWins(standing.getWins());
        response.setDraws(standing.getDraws());
        response.setLosses(standing.getLosses());
        response.setGoalsScored(standing.getGoalsScored());
        response.setGoalsConceded(standing.getGoalsConceded());
        response.setPoints(standing.getPoints());
        return response;
    }
}
