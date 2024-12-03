package cat.itacademy.s05.t01.n01.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Game {
    @Id
    private String id;
    private String playerName;
    private List<Card> cards;
    private String status;
}
