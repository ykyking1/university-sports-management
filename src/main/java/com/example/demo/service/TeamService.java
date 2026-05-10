package com.example.demo.service;

import com.example.demo.dto.TeamRegistrationRequest;
import com.example.demo.dto.TeamResponse;
import com.example.demo.entity.Team;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamResponse createTeam(TeamRegistrationRequest request) {
        // KURAL: Takım ismi eşsiz olmalı
        if (teamRepository.existsByName(request.getName())) {
            throw new RuntimeException("Bu takım ismi zaten alınmış: " + request.getName());
        }

        // KURAL: Kadro 15 kişiyi geçemez
        if (request.getPlayerIds().size() > 15) {
            throw new RuntimeException("Bir takım en fazla 15 oyuncudan oluşabilir.");
        }

        List<User> players = request.getPlayerIds().stream()
                .map(id -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + id));

                    // KURAL: Oyuncu başka bir takımda olamaz
                    if (teamRepository.existsByPlayersId(id)) {
                        throw new RuntimeException("Oyuncu zaten bir takıma kayıtlı: " + user.getFullName());
                    }

                    // KURAL: Sadece öğrenciler katılabilir
                    validateIsStudent(user);
                    return user;
                })
                .collect(Collectors.toList());

        Team team = new Team();
        team.setName(request.getName());
        team.setPlayers(players);

        if (request.getCaptainId() != null) {
            User captain = userRepository.findById(request.getCaptainId())
                    .orElseThrow(() -> new EntityNotFoundException("Kaptan bulunamadı"));
            validateIsStudent(captain);
            team.setCaptain(captain);
        }

        Team savedTeam = teamRepository.save(team);
        return mapToResponse(savedTeam);
    }

    @Transactional
    public void addPlayerToTeam(Long teamId, Long playerId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Takım bulunamadı"));

        User player = userRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Oyuncu bulunamadı"));

        // KURAL: Oyuncu başka bir takımda olamaz
        if (teamRepository.existsByPlayersId(playerId)) {
            throw new RuntimeException("Bu oyuncu zaten başka bir takıma kayıtlı.");
        }

        // KURAL: Kadro sınırı (15 kişi)
        if (team.getPlayers().size() >= 15) {
            throw new RuntimeException("Takım kadrosu dolu (Maksimum 15 kişi).");
        }

        validateIsStudent(player);

        if (!team.getPlayers().contains(player)) {
            team.getPlayers().add(player);
            teamRepository.save(team);
        }
    }
    @Transactional
    public void removePlayerFromTeam(Long teamId, Long playerId) {
        // 1. Takımı bul
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Takım bulunamadı"));

        // 2. Oyuncunun gerçekten o takımda olup olmadığını kontrol et
        boolean isMember = team.getPlayers().stream()
                .anyMatch(p -> p.getId().equals(playerId));

        if (!isMember) {
            throw new RuntimeException("Bu oyuncu zaten bu takımın bir üyesi değil.");
        }

        // 3. "En az bir oyuncu bulunmalı" kuralı (Kadro 1 kişiyse silmeye izin verme)
        // Bu kural TeamRegistrationRequest içindeki @NotEmpty kısıtıyla da uyumludur.
        if (team.getPlayers().size() <= 1) {
            throw new RuntimeException("Takımda en az bir oyuncu bulunmalıdır. Son oyuncuyu çıkaramazsınız.");
        }

        // 4. Oyuncuyu listeden çıkar
        team.getPlayers().removeIf(p -> p.getId().equals(playerId));

        // 5. Ekstra Kontrol: Eğer çıkarılan oyuncu takımı kaptanıysa, kaptanlığı temizle
        if (team.getCaptain() != null && team.getCaptain().getId().equals(playerId)) {
            team.setCaptain(null);
        }

        // 6. Güncel durumu kaydet
        teamRepository.save(team);
    }

    private void validateIsStudent(User user) {
        if (!UserRole.STUDENT.equals(user.getRole())) {
            throw new RuntimeException("Sadece üniversite öğrencileri takıma katılabilir: " + user.getFullName());
        }
    }

    private TeamResponse mapToResponse(Team team) {
        TeamResponse response = new TeamResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setPlayerIds(team.getPlayers().stream().map(User::getId).collect(Collectors.toList()));
        response.setCaptainId(team.getCaptain() != null ? team.getCaptain().getId() : null);
        response.setPlayerCount(team.getPlayers().size());
        return response;
    }
}