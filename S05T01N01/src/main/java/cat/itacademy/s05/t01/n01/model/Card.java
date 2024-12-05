package cat.itacademy.s05.t01.n01.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Card {
    private String suit;
    private String rank;
    private int value;
    private boolean isFaceUp;

    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
        this.value = assignValue(rank);
        this.isFaceUp = false;
    }

    private int assignValue(String rank){
        return switch (rank) {
            case "A" -> 11;
            case "J", "Q", "K" -> 10;
            default -> Integer.parseInt(rank);
        };
    }

    private void flip(){
        this.isFaceUp = !this.isFaceUp;
    }
}
