package dev.java10x.elifoot.service;

import dev.java10x.elifoot.controller.request.LoginRequest;
import dev.java10x.elifoot.controller.response.LoginResponse;
import dev.java10x.elifoot.entity.Scope;
import dev.java10x.elifoot.entity.User;
import dev.java10x.elifoot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginResponse login(LoginRequest request) {
        Optional<User> optUser = userRepository.findByEmail(request.getEmail());

        if(optUser.isEmpty() || !isPasswordCorrect(request.getPassword(), optUser.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User savedUser = optUser.get();
        List<String> scopes = savedUser.getScopes().stream()
                .map(Scope::getName)
                .toList();
        long expiresIn = 600L;

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("eliffot-api")
                .subject(savedUser.getName())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .issuedAt(Instant.now())
                .claim("email", savedUser.getEmail())
                .claim("scope", scopes)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

        return LoginResponse.builder()
                .accessToken(token)
                .expiresIn(expiresIn)
                .build();

    }

    private boolean isPasswordCorrect(String password, String savedPassword) {
       return passwordEncoder.matches(password, savedPassword);
    }
}
