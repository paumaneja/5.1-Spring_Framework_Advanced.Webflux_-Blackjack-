package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetGameDetailsOK() {
        String gameId = "12345";
        Game mockGame = new Game();
        mockGame.setId(gameId);
        when(gameService.getGame(gameId)).thenReturn(Mono.just(mockGame));

        ResponseEntity<Game> response = gameController.getGameDetails(gameId).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockGame, response.getBody());
        verify(gameService, times(1)).getGame(gameId);
    }


    @Test
    public void testCreateGame() {
        // Given
        List<String> playerNames = List.of("Player1", "Player2");
        Game savedGame = new Game();
        savedGame.setId("12345");
        when(gameService.createGame(playerNames)).thenReturn(Mono.just(savedGame));

        // When
        ResponseEntity<Game> response = gameController.createGame(playerNames).block();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedGame, response.getBody());
        verify(gameService, times(1)).createGame(playerNames);
    }

}
