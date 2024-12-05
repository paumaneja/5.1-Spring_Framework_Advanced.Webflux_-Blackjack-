package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.enums.GameStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document
public class Game {
    @Id
    private String id;
    private Deck deck;

    private Map<String, List<Card>> playerHands = new HashMap<>();
    private List<Card> dealerHand;

    private Map<String, Integer> playerBets = new HashMap<>();

    private GameStatus status;

    public Game() {
        this.deck = new Deck();
        this.status = GameStatus.IN_POGRESS;
    }

}
