package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.enums.Rank;
import cat.itacademy.s05.t01.n01.enums.Suit;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Card {
    private Suit suit;
    private Rank rank;
    private boolean isFaceUp;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        this.isFaceUp = false;
    }

}
