package dev.java10x.elifoot.controller;

import dev.java10x.elifoot.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class StadiumControllerTest extends BaseIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser(authorities = {"SCOPE_stadiun:write"})
    @Test
    @DisplayName("should create a stadium")
    void shouldCreateStadium() throws Exception {

    }
}