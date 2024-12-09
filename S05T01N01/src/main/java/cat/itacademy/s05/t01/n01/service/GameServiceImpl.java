package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Deck;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    @Autowired
    private final GameRepository gameRepository;
    private final DeckService deckService;

    @Override
    public Mono<Game> createGame(List<String> playerNames){
        Game newGame = new Game();
        Deck deck = new Deck();
        newGame.setDeck(deck);
        playerNames.forEach(player -> {
            newGame.getPlayerHands().put(player, new ArrayList<>());
            newGame.getPlayerBets().put(player, 0);
        });
        deckService.shuffleDeck(deck);
        newGame.setDealerHand(new ArrayList<>());

        return gameRepository.save(newGame);
    }

    @Override
    public Mono<Game> getGame(String id){
        return gameRepository.findById(id);
    }

    @Override
    public Mono<Game> updateGame(String id, Game game) {
        return null;
    }

    @Override
    public Mono<Game> deleteGame(String id) {
        return null;
    }
}
