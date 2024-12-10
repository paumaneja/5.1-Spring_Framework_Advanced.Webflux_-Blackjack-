package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface GameService {
    Mono<Game> createGame(List<String> playerNames);
    int calculateHandValue(List<Card> hand);
    void dealCardToPlayer(Game game, String playerName);
    void dealCardToDealer(Game game);
    void determineWinner(Game game);
    void processBets(Game game);
    Mono<Game> playMove(String gameId, String playerName, String move);
    Mono<Game> getGame(String id);
    Mono<Game> updateGame(String id, Game game);
    Mono<Void> deleteGame(String id);
    void dealerTurn(Game game);
    void completeGame(Game game);
}
