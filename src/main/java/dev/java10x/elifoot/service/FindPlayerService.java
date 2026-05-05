package dev.java10x.elifoot.service;

import dev.java10x.elifoot.Mapper.PlayerMapper;
import dev.java10x.elifoot.controller.response.PlayerDetailResponse;
import dev.java10x.elifoot.controller.response.PlayerResponse;
import dev.java10x.elifoot.exceptions.ResourceNotFoundException;
import dev.java10x.elifoot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public Page<PlayerResponse> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable)
                .map(playerMapper::toPlayerResponse);
    }

    public PlayerDetailResponse findById(Long id) {
        return playerRepository.findById(id)
                .map(playerMapper::toPlayerDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found for id: " + id));
    }
}
