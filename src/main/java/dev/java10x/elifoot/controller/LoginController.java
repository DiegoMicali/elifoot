package dev.java10x.elifoot.controller;

import dev.java10x.elifoot.controller.request.LoginRequest;
import dev.java10x.elifoot.controller.response.LoginResponse;
import dev.java10x.elifoot.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }

}
