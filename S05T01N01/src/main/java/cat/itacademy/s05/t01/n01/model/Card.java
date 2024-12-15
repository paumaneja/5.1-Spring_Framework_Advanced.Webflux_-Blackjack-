package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.enums.Rank;
import cat.itacademy.s05.t01.n01.enums.Suit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Schema(description = "Represents a playing card with a suit and rank.")
public class Card {
    @Schema(description = "Suit of the card (e.g., Hearts, Spades)", example = "HEARTS")
    private Suit suit;
    @Schema(description = "Rank of the card (e.g., Ace, King, Queen)", example = "ACE")
    private Rank rank;
    @Schema(description = "Indicates if the card is facing up", example = "false")
    private boolean isFaceUp;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        this.isFaceUp = false;
    }

}
