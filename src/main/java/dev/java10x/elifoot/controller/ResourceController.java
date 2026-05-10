package dev.java10x.elifoot.controller;

import dev.java10x.elifoot.controller.response.PositionResponse;
import dev.java10x.elifoot.entity.Position;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/resources")
@RestController
public class ResourceController {

    @GetMapping("/positions")
    @ResponseStatus(HttpStatus.OK)
    public List<PositionResponse> getPosition() {
        return Arrays.stream(Position.values())
                .map(position -> new PositionResponse(position.name(), position.getLabel()))
                .toList();
    }
}
