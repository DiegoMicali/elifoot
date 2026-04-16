package dev.java10x.elifoot.controller;

import dev.java10x.elifoot.controller.request.CreateStadiumRequest;
import dev.java10x.elifoot.controller.response.StadiumResponse;
import dev.java10x.elifoot.service.CreateStadiumService;
import dev.java10x.elifoot.service.FindStadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadiums")
@RequiredArgsConstructor
public class StadiumController {

    private final FindStadiumService findStadiumService;
    private final CreateStadiumService createStadiumService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<StadiumResponse> findAll(Pageable pageable) {
        return findStadiumService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StadiumResponse create(@RequestBody CreateStadiumRequest request) {
        return createStadiumService.execute(request);
    }
}
