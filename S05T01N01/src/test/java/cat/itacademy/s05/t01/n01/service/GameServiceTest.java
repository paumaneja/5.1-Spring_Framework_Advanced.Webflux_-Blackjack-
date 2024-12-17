package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.enums.Rank;
import cat.itacademy.s05.t01.n01.enums.Suit;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.Impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private PlayerService playerService;
    @Mock
    private DeckService deckService;
    @Mock
    private CardService cardService;


    @InjectMocks
    private GameServiceImpl gameService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameService = new GameServiceImpl(gameRepository, deckService, cardService, playerService, playerRepository);
    }

    @Test
    public void testCalculateHandValue() {

        Card aceCard = new Card(Suit.HEARTS, Rank.ACE);
        Card tenCard = new Card(Suit.CLUBS, Rank.TEN);
        Card fiveCard = new Card(Suit.SPADES, Rank.FIVE);

        List<Card> hand = List.of(aceCard, tenCard, fiveCard);

        when(cardService.calculateValue(aceCard)).thenReturn(11);
        when(cardService.calculateValue(tenCard)).thenReturn(10);
        when(cardService.calculateValue(fiveCard)).thenReturn(5);

        int handValue = gameService.calculateHandValue(hand);

        assertEquals(16, handValue, "The total hand value 16, the ACE 11 -> 1 (1 + 10 + 5)");

        handValue = gameService.calculateHandValue(List.of(aceCard, tenCard, tenCard));
        assertEquals(21, handValue, "The ACE value should be reduced to 1 to keep total <= 21");

        verify(cardService, times(2)).calculateValue(aceCard);
        verify(cardService, times(3)).calculateValue(tenCard);
        verify(cardService, times(1)).calculateValue(fiveCard);
    }

    @Test
    public void testProgressTurnWithJUnit() {

        Game currentGame = new Game();
        currentGame.setId("12345");

        Map<String, List<Card>> playerHands = new LinkedHashMap<>();
        playerHands.put("player1", new ArrayList<>());
        playerHands.put("player2", new ArrayList<>());

        currentGame.setPlayerHands(playerHands);
        currentGame.setActivePlayer("player1");

        gameService.progressTurn(currentGame);

        assertNotNull(currentGame);
        assertEquals("player2", currentGame.getActivePlayer()); // Should move to next player

        gameService.progressTurn(currentGame);

        assertNull(currentGame.getActivePlayer()); // No more players, active player should be null
    }
}
