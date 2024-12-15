package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.enums.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document
@Schema(description = "Represents a game entity with its current state, players, and dealer information.")
public class Game {
    @Id
    @Schema(description = "Unique identifier of the game", example = "61234abcde12345f67890")
    private String id;
    @Schema(description = "Deck of cards used in the game")
    private Deck deck;
    @Schema(description = "Mapping of player names to their respective hands")
    private Map<String, List<Card>> playerHands = new LinkedHashMap<>();
    @Schema(description = "Mapping of player names to their respective bets", example = "{\"John\": 50, \"Jane\": 100}")
    private Map<String, Integer> playerBets = new HashMap<>();
    @Schema(description = "Mapping of player names to their results (e.g., Win, Lose, Draw)", example = "{\"John\": \"Win\", \"Jane\": \"Lose\"}")
    private Map<String, String> playerResults = new HashMap<>();
    @Schema(description = "Dealer's hand of cards")
    private List<Card> dealerHand = new ArrayList<>();
    @Schema(description = "Name of the player currently active", example = "John")
    private String activePlayer;
    @Schema(description = "Current status of the game", example = "IN_PROGRESS")
    private GameStatus status;

    public Game() {
        this.deck = new Deck();
        this.status = GameStatus.IN_PROGRESS;
    }

}
