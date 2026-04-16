package dev.java10x.elifoot.service;

import dev.java10x.elifoot.Mapper.StadiumMapper;
import dev.java10x.elifoot.controller.request.CreateStadiumRequest;
import dev.java10x.elifoot.controller.response.StadiumResponse;
import dev.java10x.elifoot.entity.Stadium;
import dev.java10x.elifoot.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateStadiumService {

    private final StadiumRepository stadiumRepository;
    private final StadiumMapper stadiumMapper;

    public StadiumResponse execute (CreateStadiumRequest request) {
        Stadium savedStadium = stadiumRepository.save(stadiumMapper.toStadium(request));
        return stadiumMapper.toStadiumResponse(savedStadium);
    }
}
