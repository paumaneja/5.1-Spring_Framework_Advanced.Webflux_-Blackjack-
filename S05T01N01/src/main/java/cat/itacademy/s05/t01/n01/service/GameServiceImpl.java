package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.enums.Rank;
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
        int totalValue = hand.stream()
                .mapToInt(cardService::calculateValue)
                .sum();

        long aceCount = hand.stream()
                .filter(card -> card.getRank() == Rank.ACE)
                .count();

        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--;
        }

        return totalValue;
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
        boolean dealerHasBlackJack = dealerValue == 21 && game.getDealerHand().size() == 2;

        game.getPlayerHands().forEach((playerName, playerHand) -> {
            int playerValue = calculateHandValue(playerHand);
            boolean playerHasBlackJack = playerValue == 21 && playerHand.size() == 2;

            if (playerHasBlackJack && dealerHasBlackJack) {
                System.out.println(playerName + " ties with the dealer with a natural Blackjack!");
            } else if (playerHasBlackJack) {
                System.out.println(playerName + " wins with a natural Blackjack!");
            } else if (dealerHasBlackJack) {
                System.out.println(playerName + " loses. The dealer has a natural Blackjack.");
            } else {
                boolean playerBust = playerValue > 21;
                boolean dealerBust = dealerValue > 21;
                boolean playerWins = !playerBust && (dealerBust || playerValue > dealerValue);
                boolean dealerWins = !dealerBust && (playerBust || dealerValue > playerValue);

                if (playerWins) {
                    System.out.println(playerName + " wins with " + playerValue + " points.");
                } else if (dealerWins) {
                    System.out.println(playerName + " loses. The dealer has " + dealerValue + " points.");
                } else {
                    System.out.println(playerName + " ties with the dealer with " + playerValue + " points.");
                }
            }
        });
    }

    @Override
    public void processBets(Game game) {
        int dealerValue = calculateHandValue(game.getDealerHand());

        game.getPlayerHands().forEach((playerName, playerHand) -> {
            int playerValue = calculateHandValue(playerHand);
            int bet = game.getPlayerBets().getOrDefault(playerName, 0);

            if (playerValue > 21 || (dealerValue <= 21 && dealerValue > playerValue)) {
                System.out.println(playerName + " loses the bet of " + bet + ".");
            } else if (dealerValue > 21 || playerValue > dealerValue) {
                System.out.println(playerName + " wins " + (2 * bet) + ".");
            } else {
                System.out.println(playerName + " tie and get your bet back " + bet + ".");
            }
        });
    }

    @Override
    public Mono<Game> playMove(String gameId, String playerName, String move) {
        return getGame(gameId)
                .flatMap(game -> {
                    if ("hit".equalsIgnoreCase(move)) {
                        dealCardToPlayer(game, playerName);
                        int playerValue = calculateHandValue(game.getPlayerHands().get(playerName));
                        if (playerValue > 21) {
                            System.out.println(playerName + ". Bust!");
                        }
                    } else if ("stand".equalsIgnoreCase(move)) {
                        System.out.println(playerName + " STANDS.");
                    } else {
                        return Mono.error(new IllegalArgumentException("Not valid: " + move));
                    }
                    return updateGame(gameId, game);
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
