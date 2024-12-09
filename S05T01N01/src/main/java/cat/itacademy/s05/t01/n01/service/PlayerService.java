package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PlayerService {
    Mono<Player> createPlayer(String playerName);
    Mono<List<Player>> updateRanking();
    Mono<List<Player>> saveAllPlayers(List<Player> players);
    Mono<Void> updatePlayerScore(String playerId, int additionalScore);
}
