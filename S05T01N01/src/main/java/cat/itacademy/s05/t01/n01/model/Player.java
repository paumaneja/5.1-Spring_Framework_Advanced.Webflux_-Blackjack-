package cat.itacademy.s05.t01.n01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("players")
@Schema(description = "Details of a player")
public class Player {
    @Id
    @Schema(description = "Unique identifier of the player", example = "1")
    private Long id;
    @Schema(description = "Name of the player", example = "Pauko")
    private String name;
    @Column("total_score")
    @Schema(description = "Player's current score", example = "100")
    private Integer totalScore;
    @Schema(description = "Ranking position of the player", example = "1")
    private int ranking;

    public Player() {}

    public Player(String name) {
        this.name = name;
        this.totalScore = 0;
        this.ranking = 0;
    }
}
