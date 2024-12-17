package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface PlayerService {
    Mono<Player> createPlayer(String playerName);
    Mono<Void> updatePlayerScore(String playerName, int score);
    Mono<List<Player>> updateRanking();
    Mono<Player> updatePlayerName(String playerId, String newName);

}
