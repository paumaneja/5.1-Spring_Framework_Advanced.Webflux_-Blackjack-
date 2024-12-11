package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.enums.GameStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document
public class Game {
    @Id
    private String id;
    private Deck deck;
    private Map<String, List<Card>> playerHands = new LinkedHashMap<>();
    private Map<String, Integer> playerBets = new HashMap<>();
    private Map<String, String> playerResults = new HashMap<>();
    private List<Card> dealerHand = new ArrayList<>();
    private String activePlayer;
    private GameStatus status;

    public Game() {
        this.deck = new Deck();
        this.status = GameStatus.IN_PROGRESS;
    }

}
