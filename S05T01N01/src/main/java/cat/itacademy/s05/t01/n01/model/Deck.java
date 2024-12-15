package cat.itacademy.s05.t01.n01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Represents a deck of cards used in a game.")
public class Deck {
    @Schema(description = "List of cards in the deck")
    private List<Card> cards;
}
