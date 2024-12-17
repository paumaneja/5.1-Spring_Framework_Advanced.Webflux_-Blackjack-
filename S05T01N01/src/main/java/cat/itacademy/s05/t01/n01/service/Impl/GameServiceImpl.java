package cat.itacademy.s05.t01.n01.service.Impl;

import cat.itacademy.s05.t01.n01.dto.PlayGameRequest;
import cat.itacademy.s05.t01.n01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.enums.Rank;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Deck;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.CardService;
import cat.itacademy.s05.t01.n01.service.DeckService;
import cat.itacademy.s05.t01.n01.service.GameService;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    @Autowired
    private final GameRepository gameRepository;
    private final DeckService deckService;
    private final CardService cardService;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    @Override
    public Mono<Game> createGame(List<String> playerNames){
        return verifyPlayersExist(playerNames)
                .flatMap(allPlayersExist -> {
                    if (!allPlayersExist) {
                        return Mono.error(new IllegalArgumentException("One or more players do not exist in the database."));
                    }
                    Game newGame = new Game();
                    Deck deck = deckService.createDeck();
                    deckService.shuffleDeck(deck);
                    newGame.setDeck(deck);

                    if (!playerNames.isEmpty()) {
                        newGame.setActivePlayer(playerNames.get(0));
                    }
                    playerNames.forEach(player -> {
                        newGame.getPlayerHands().put(player, new ArrayList<>());
                        newGame.getPlayerBets().put(player, 100);
                        newGame.getPlayerHands().get(player).add(deckService.dealCard(deck));
                        newGame.getPlayerHands().get(player).add(deckService.dealCard(deck));
                    });
                    newGame.setPlayerResults(new HashMap<>());
                    newGame.setDealerHand(new ArrayList<>());
                    newGame.getDealerHand().add(deckService.dealCard(deck));
                    return gameRepository.save(newGame);
                });
    }


    private Mono<Boolean> verifyPlayersExist(List<String> playerNames) {
        return Flux.fromIterable(playerNames)
                .flatMap(playerRepository::findByName)
                .collectList()
                .map(foundPlayers -> foundPlayers.size() == playerNames.size());
    }



    private void dealCardToPlayer(Game game, String playerName) {
        Card card = deckService.dealCard(game.getDeck());
        game.getPlayerHands().get(playerName).add(card);
    }


    private void dealCardToDealer(Game game) {
        Card card = deckService.dealCard(game.getDeck());
        game.getDealerHand().add(card);
    }


    private void dealerTurn(Game game) {
        while (calculateHandValue(game.getDealerHand()) < 17) {
            dealCardToDealer(game);
        }
    }


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
    public Mono<Game> playMove(String gameId, PlayGameRequest playGameRequest) {
        return getGame(gameId)
                .flatMap(game -> {
                    game.getPlayerBets().put(playGameRequest.getPlayerName(), playGameRequest.getBet());
                    if (!playGameRequest.getPlayerName().equals(game.getActivePlayer())) {
                        return Mono.error(new IllegalArgumentException("It's not " + playGameRequest.getPlayerName() + "'s turn."));
                    }

                    switch (playGameRequest.getMove().toUpperCase()) {
                        case "HIT":
                            dealCardToPlayer(game, playGameRequest.getPlayerName());
                            if (calculateHandValue(game.getPlayerHands().get(playGameRequest.getPlayerName())) > 21) {
                                game.getPlayerResults().put(playGameRequest.getPlayerName(), "BUST");
                                progressTurn(game);
                            }
                            break;

                        case "STAND":
                            progressTurn(game);
                            break;

                        default:
                            return Mono.error(new IllegalArgumentException("Invalid move: " + playGameRequest.getMove()));
                    }

                    if (game.getActivePlayer() == null) {
                        dealerTurn(game);
                        completeGame(game);
                    }

                    return updateGame(game);
                });
    }



    public void progressTurn(Game game) {

        List<String> players = new ArrayList<>(game.getPlayerHands().keySet());
        String currentPlayer = game.getActivePlayer();

        if (currentPlayer == null || !players.contains(currentPlayer)) {
            throw new IllegalStateException("Active player is invalid or missing.");
        }

        int currentIndex = players.indexOf(currentPlayer);

        if (currentIndex < players.size() - 1) {
            game.setActivePlayer(players.get(currentIndex + 1));
        } else {
            game.setActivePlayer(null);
        }
    }






    private void determineWinner(Game game) {
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


    private void processBets(Game game) {
        Flux.fromIterable(game.getPlayerResults().entrySet())
                .flatMap(entry -> {
                    String playerName = entry.getKey();
                    String result = entry.getValue();
                    int bet = game.getPlayerBets().getOrDefault(playerName, 0);
                    if ("WIN".equals(result)) {
                        return playerService.updatePlayerScore(playerName, bet * 2);
                    } else if ("TIE".equals(result)) {
                        return playerService.updatePlayerScore(playerName, bet);
                    }
                    return Mono.empty();
                })
                .then(playerService.updateRanking())
                .subscribe();
    }




    private void completeGame(Game game) {
        determineWinner(game);
        processBets(game);
        playerService.updateRanking().subscribe();
        game.setStatus(GameStatus.COMPLETED);
    }

    @Override
    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id);
    }


    private Mono<Game> updateGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(gameRepository::delete);
    }

}
