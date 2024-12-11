package cat.itacademy.s05.t01.n01.dto;

import lombok.Data;

@Data
public class PlayGameRequest {
    private String playerName;
    private String move;
    private int bet;

    // Constructors
    public PlayGameRequest() {
    }

    public PlayGameRequest(String playerName, String move, int bet) {
        this.playerName = playerName;
        this.move = move;
        this.bet = bet;
    }

}