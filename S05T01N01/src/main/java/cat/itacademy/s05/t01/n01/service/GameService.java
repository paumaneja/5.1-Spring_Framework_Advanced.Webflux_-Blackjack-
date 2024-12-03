package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(String playerName);
    Mono<Game> getGame(String id);
}
