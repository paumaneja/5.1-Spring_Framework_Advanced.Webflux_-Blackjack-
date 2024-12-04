package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> createPlayer(String playerName);
}
