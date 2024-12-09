package cat.itacademy.s05.t01.n01.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("player")
public class Player {
    @Id
    private Long id;
    private String name;
    private Integer totalScore;
    private int ranking;

    public Player() {}

    public Player(String name) {
        this.name = name;
        this.totalScore = 0;
        this.ranking = 0;
    }
}
