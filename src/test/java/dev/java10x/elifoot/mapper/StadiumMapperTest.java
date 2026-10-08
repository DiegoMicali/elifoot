package dev.java10x.elifoot.mapper;

import dev.java10x.elifoot.controller.request.CreateStadiumRequest;
import dev.java10x.elifoot.controller.request.CreateUserRequest;
import dev.java10x.elifoot.controller.response.StadiumResponse;
import dev.java10x.elifoot.entity.Stadium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class StadiumMapperTest {

    private final StadiumMapper mapper = Mappers.getMapper(StadiumMapper.class);

    @Test
    @DisplayName("toStadiumResponse should map CreateStadiumRequest to Stadium corretly")
    void toStadiumResponse() {
        // Arrange - Given
        Stadium stadium = Stadium.builder()
                .id(1L)
                .name("")
                .city("Test City")
                .capacity(50000)
                .urlImg("Imagem.com")
                .build();

        // Action - When
        StadiumResponse stadiumResponse = mapper.toStadiumResponse(stadium);

        //Assertion - Then
        assertNotNull(stadiumResponse);

        assertEquals(stadium.getId(), stadiumResponse.getId());
        assertEquals(stadium.getName(), stadiumResponse.getName());
        assertEquals(stadium.getCapacity(), stadiumResponse.getCapacity());
        assertEquals(stadium.getUrlImg(), stadiumResponse.getUrlImg());
    }

    @Test
    @DisplayName("toStadiumRequest should map Stadium to StadiumResponse corretly")
    void toStadium() {

        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .name("Test Stadium")
                .city("Test City")
                .capacity(50000)
                .urlImg("Test URL")
                .build();

        Stadium stadium = mapper.toStadium(request);
        assertEquals(request.getName(), stadium.getName());
        assertEquals(request.getCity(), stadium.getCity());
        assertEquals(request.getCapacity(), stadium.getCapacity());
        assertEquals(request.getUrlImg(), stadium.getUrlImg());

    }
}