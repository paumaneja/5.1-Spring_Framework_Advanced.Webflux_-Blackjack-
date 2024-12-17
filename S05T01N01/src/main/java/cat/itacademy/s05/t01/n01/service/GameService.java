package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayGameRequest;
import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface GameService {
    Mono<Game> createGame(List<String> playerNames);
    Mono<Game> playMove(String gameId, PlayGameRequest playGameRequest);
    Mono<Game> getGame(String id);
    Mono<Void> deleteGame(String id);
}
