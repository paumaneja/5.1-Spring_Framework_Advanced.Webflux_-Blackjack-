package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.enums.GameStatus;
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
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    @Autowired
    private final GameRepository gameRepository;
    private final DeckService deckService;
    private final CardService cardService;
    private final PlayerService playerService;

    @Override
    public Mono<Game> createGame(List<String> playerNames){
        Game newGame = new Game();
        Deck deck = deckService.createDeck();
        deckService.shuffleDeck(deck);
        newGame.setDeck(deck);
        if (!playerNames.isEmpty()) {
            newGame.setActivePlayer(playerNames.get(0)); // Set the first player as active
        }
        playerNames.forEach(player -> {
            newGame.getPlayerHands().put(player, new ArrayList<>());
            newGame.getPlayerBets().put(player, 0);
            newGame.getPlayerHands().get(player).add(deckService.dealCard(deck));
            newGame.getPlayerHands().get(player).add(deckService.dealCard(deck));
        });
        newGame.setPlayerResults(new HashMap<>());
        newGame.setDealerHand(new ArrayList<>());
        newGame.getDealerHand().add(deckService.dealCard(deck));
        return gameRepository.save(newGame);
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
    public void dealerTurn(Game game) {
        while (calculateHandValue(game.getDealerHand()) < 17) {
            dealCardToDealer(game);
        }
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
    public Mono<Game> playMove(String gameId, String playerName, String move) {
        return getGame(gameId)
                .flatMap(game -> {
                    if (!playerName.equals(game.getActivePlayer())) {
                        return Mono.error(new IllegalArgumentException("It's not " + playerName + "'s turn."));
                    }

                    switch (move.toUpperCase()) {
                        case "HIT":
                            dealCardToPlayer(game, playerName);
                            if (calculateHandValue(game.getPlayerHands().get(playerName)) > 21) {
                                game.getPlayerResults().put(playerName, "BUST");
                                progressTurn(game);
                            }
                            break;

                        case "STAND":
                            progressTurn(game);
                            break;

                        default:
                            return Mono.error(new IllegalArgumentException("Invalid move: " + move));
                    }

                    // Check if all players have finished and start dealer's turn
                    if (game.getActivePlayer() == null) {
                        dealerTurn(game);
                        completeGame(game);
                    }

                    return updateGame(gameId, game);
                });
    }



    private void progressTurn(Game game) {
        // Ensure playerHands has a valid order of players
        List<String> players = new ArrayList<>(game.getPlayerHands().keySet());
        String currentPlayer = game.getActivePlayer();

        // Validate that the active player exists
        if (currentPlayer == null || !players.contains(currentPlayer)) {
            throw new IllegalStateException("Active player is invalid or missing.");
        }

        // Find the current player's index
        int currentIndex = players.indexOf(currentPlayer);

        // Transition to the next player, or to dealer if no players remain
        if (currentIndex < players.size() - 1) {
            game.setActivePlayer(players.get(currentIndex + 1)); // Move to the next player
        } else {
            game.setActivePlayer(null); // No more players, dealer's turn
        }
    }





    @Override
    public void determineWinner(Game game) {
        int dealerValue = calculateHandValue(game.getDealerHand());
        boolean dealerHasBlackJack = dealerValue == 21 && game.getDealerHand().size() == 2;

        game.getPlayerHands().forEach((playerName, playerHand) -> {
            int playerValue = calculateHandValue(playerHand);
            boolean playerHasBlackJack = playerValue == 21 && playerHand.size() == 2;

            if (playerHasBlackJack && dealerHasBlackJack) {
                game.getPlayerResults().put(playerName, "TIE");
            } else if (playerHasBlackJack) {
                game.getPlayerResults().put(playerName, "WIN");
            } else if (dealerHasBlackJack) {
                game.getPlayerResults().put(playerName, "LOSE");
            } else {
                boolean playerBust = playerValue > 21;
                boolean dealerBust = dealerValue > 21;
                if (playerBust || (!dealerBust && dealerValue > playerValue)) {
                    game.getPlayerResults().put(playerName, "LOSE");
                } else if (dealerBust || playerValue > dealerValue) {
                    game.getPlayerResults().put(playerName, "WIN");
                } else {
                    game.getPlayerResults().put(playerName, "TIE");
                }
            }
        });
    }

    @Override
    public void processBets(Game game) {
        game.getPlayerResults().forEach((playerName, result) -> {
            int bet = game.getPlayerBets().getOrDefault(playerName, 0);
            if ("WIN".equals(result)) {
                playerService.updatePlayerScore(playerName, bet * 2).subscribe();
            } else if ("TIE".equals(result)) {
                playerService.updatePlayerScore(playerName, bet).subscribe();
            } else {
                System.out.println(playerName + " loses the bet of " + bet + ".");
            }
        });
    }



    @Override
    public void completeGame(Game game) {
        determineWinner(game);
        processBets(game);
        playerService.updateRanking().subscribe();
        game.setStatus(GameStatus.COMPLETED);
    }

    @Override
    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id);
    }

    @Override
    public Mono<Game> updateGame(String id, Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(gameRepository::delete);
    }

}
