package dev.java10x.elifoot.service;

import dev.java10x.elifoot.Mapper.PlayerMapper;
import dev.java10x.elifoot.controller.request.CreatePlayerRequest;
import dev.java10x.elifoot.controller.response.PlayerResponse;
import dev.java10x.elifoot.entity.Player;
import dev.java10x.elifoot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePlayerService {

    private final FindClubService findClubService;

    private final PlayerMapper playerMapper;

    private final PlayerRepository playerRepository;

    public PlayerResponse execute(CreatePlayerRequest request) {
        Player newPlayer = playerMapper.toPlayer(request);
        newPlayer.setClub(findClubService.findById(request.getClubId()));
        Player savedPlayer = playerRepository.save(newPlayer);
        return playerMapper.toPlayerResponse(savedPlayer);
    }

}
