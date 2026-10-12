package dev.java10x.elifoot.service;

import dev.java10x.elifoot.controller.request.CreateClubRequest;
import dev.java10x.elifoot.entity.Club;
import dev.java10x.elifoot.entity.Stadium;
import dev.java10x.elifoot.mapper.ClubMapper;
import dev.java10x.elifoot.repository.ClubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CreateClubServiceTest {

    @InjectMocks
    CreateClubService createClubService;

    @Mock
    ClubRepository clubRepository;
    @Mock
    ClubMapper clubMapper;
    @Mock
    FindStadiumService findStadiumService;
    @Captor
    ArgumentCaptor<Club> clubCaptor;

    @Test
    @DisplayName("Should create a new club successfully")
    void shouldCreateNewClubWithStadium() {
        // Arrange
        CreateClubRequest request = CreateClubRequest.builder()
                .name("Test Stadium")
                .founded(LocalDate.of(2020, 1, 1))
                .urlImg("http://exemple.com/imagem.png")
                .stadiumId(10L)
                .build();
        Club club = Club.builder()
                .id(1L)
                .name("Test Club")
                .founded(LocalDate.of(2020, 1, 1))
                .urlImg("http://exemple.com/imagem.png")
                .stadium(Stadium.builder().id(10L).build())
                .build();

        Stadium stadium = Stadium.builder()
                .id(10L)
                .name("Test Stadium")
                .city("Test City")
                .capacity(50000)
                .urlImg(null)
                .build();

        Mockito.when(clubMapper.toEntity(request)).thenReturn(club);
        Mockito.when(findStadiumService.findById(stadium.getId())).thenReturn(stadium);

        // Actions
        createClubService.execute(request);

        // Assertions
        Mockito.verify(clubRepository).save(Mockito.any(Club.class));
        Mockito.verify(clubMapper).toEntity(request);
        Mockito.verify(findStadiumService).findById(stadium.getId());

        Mockito.verify(clubRepository).save(clubCaptor.capture());
        Club savedClub = clubCaptor.getValue();
        assertEquals(stadium, savedClub.getStadium());
        assertEquals(10L, savedClub.getStadium().getId());

    }

    @Test
    @DisplayName("Should create a new club without stadium successfully")
    void shouldCreateNewClubWithoutStadium() {
        // Arrange
        CreateClubRequest request = CreateClubRequest.builder()
                .name("Test Stadium")
                .founded(LocalDate.of(2020, 1, 1))
                .urlImg("http://exemple.com/imagem.png")
                .stadiumId(1L)
                .build();
        Club club = Club.builder()
                .id(1L)
                .name("Test Club")
                .founded(LocalDate.of(2020, 1, 1))
                .urlImg("http://exemple.com/imagem.png")
                .build();

        Stadium stadium = Stadium.builder()
                .id(10L)
                .name("Test Stadium")
                .city("Test City")
                .capacity(50000)
                .urlImg(null)
                .build();

        Mockito.when(clubMapper.toEntity(request)).thenReturn(club);

        // Actions
        createClubService.execute(request);

        // Assertions
        Mockito.verify(clubRepository).save(Mockito.any(Club.class));
        Mockito.verify(clubMapper).toEntity(request);
        Mockito.verify(findStadiumService, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(clubRepository).save(clubCaptor.capture());
        Club savedClub = clubCaptor.getValue();
        assertNotNull(savedClub);
        assertNull(savedClub.getStadium());
    }
}