package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
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
    private final CardService cardService;

    @Override
    public Mono<Game> createGame(List<String> playerNames){
        Game newGame = new Game();
        Deck deck = deckService.createDeck();
        newGame.setDeck(deck);
        playerNames.forEach(player -> {
            newGame.getPlayerHands().put(player, new ArrayList<>());
            newGame.getPlayerBets().put(player, 0);
        });
        newGame.setDealerHand(new ArrayList<>());
        return gameRepository.save(newGame);
    }

    @Override
    public int calculateHandValue(List<Card> hand) {
        return hand.stream()
                .mapToInt(cardService::calculateValue)
                .sum();
    }

    @Override
    public void dealCardToPlayer(Game game, String playerName) {
        Card card = deckService.dealCard(game.getDeck());
        game.getPlayerHands().get(playerName).add(card);
    }

    @Override
    public void dealCardToDealer(Game game) {
        Card card = deckService.dealCard(game.getDeck());
        game.getDealerHand().add(card);
    }

    @Override
    public void determineWinner(Game game) {
        int dealerValue = calculateHandValue(game.getDealerHand());

        game.getPlayerHands().forEach((playerName, playerHand) -> {
            int playerValue = calculateHandValue(playerHand);

            if (playerValue > 21) {
                System.out.println(playerName + " loses (has surpassed 21).");
            } else if (dealerValue > 21 || playerValue > dealerValue) {
                System.out.println(playerName + " win against the dealer.");
            } else if (playerValue < dealerValue) {
                System.out.println(playerName + " loses to the dealer.");
            } else {
                System.out.println(playerName + " tie with the dealer.");
            }
        });
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
