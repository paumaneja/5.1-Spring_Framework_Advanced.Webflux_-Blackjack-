package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GameService {
    Mono<Game> createGame(List<String> playerNames);
    Mono<Game> getGame(String id);
    Mono<Game> updateGame(String id, Game game);
    Mono<Game> deleteGame(String id);
}
