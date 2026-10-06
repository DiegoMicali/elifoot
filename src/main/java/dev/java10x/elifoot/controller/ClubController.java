package dev.java10x.elifoot.controller;

import dev.java10x.elifoot.config.security.annotation.club.CanReadClub;
import dev.java10x.elifoot.config.security.annotation.club.CanWriteClub;
import dev.java10x.elifoot.controller.response.PlayerResponse;
import dev.java10x.elifoot.mapper.ClubMapper;
import dev.java10x.elifoot.controller.request.CreateClubRequest;
import dev.java10x.elifoot.controller.response.ClubDetailResponse;
import dev.java10x.elifoot.controller.response.ClubResponse;
import dev.java10x.elifoot.entity.Club;
import dev.java10x.elifoot.service.CreateClubService;
import dev.java10x.elifoot.service.FindClubService;
import dev.java10x.elifoot.service.FindPlayerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/clubs")
@RestController
@RequiredArgsConstructor
public class ClubController {

    private final FindClubService findClubService;
    private final FindPlayerService findPlayerService;
    private final CreateClubService createClubService;
    private final ClubMapper clubMapper;

    @CanReadClub
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ClubResponse> findAll(Pageable pageable) {
        return findClubService.findAll(pageable);
    }

    @CanReadClub

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClubDetailResponse findById(@PathVariable Long id) {
        Club club =  findClubService.findById(id);
        return clubMapper.toClubDetailResponse(club);
    }

    @CanWriteClub
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClubDetailResponse creat(@Valid @RequestBody CreateClubRequest request) {
        return createClubService.execute(request);
    }

    @CanWriteClub
    @GetMapping("/{id}/players")
    public List<PlayerResponse> findPlayerByClubId(@PathVariable long id) {
        return findPlayerService.findByClubId(id);
    }
}
