package dev.java10x.elifoot.service;

import dev.java10x.elifoot.Mapper.ClubMapper;
import dev.java10x.elifoot.controller.request.CreateClubRequest;
import dev.java10x.elifoot.controller.response.ClubDetailResponse;
import dev.java10x.elifoot.entity.Club;
import dev.java10x.elifoot.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    private final FindStadiumService findStadiumService;

    public ClubDetailResponse execute(CreateClubRequest request) {
        Club newClub = clubMapper.toEntity(request);
        if(Objects.nonNull(newClub.getStadium())) {
            newClub.setStadium(findStadiumService.findById(newClub.getStadium().getId()));
        }
        Club clubSaved = clubRepository.save(newClub);
        return clubMapper.toClubDetailResponse(clubSaved);
    }
}
