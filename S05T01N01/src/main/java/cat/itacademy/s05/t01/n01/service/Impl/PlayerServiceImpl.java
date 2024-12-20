package cat.itacademy.s05.t01.n01.service.Impl;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Mono<Player> createPlayer(String playerName){
        return verifyPlayerExists(playerName)
                .flatMap(playerExists -> {
                    if (playerExists) {
                        return Mono.error(new IllegalArgumentException("Player already exists in the database."));
                    }
                    Player newPlayer = new Player();
                    newPlayer.setName(playerName);
                    newPlayer.setTotalScore(0);
                    newPlayer.setRanking(0);
                    return playerRepository.save(newPlayer);
                });
    }

    public Mono<Boolean> verifyPlayerExists(String playerName) {
        return playerRepository.findByName(playerName)
                .map(player -> true)
                .switchIfEmpty(Mono.just(false));
    }

    @Override
    public Mono<List<Player>> updateRanking() {
        return playerRepository.findAll()
                .collectList()
                .map(players -> {
                    AtomicInteger rankCounter = new AtomicInteger(1);
                    return players.stream()
                            .sorted(Comparator.comparingInt(Player::getTotalScore).reversed())
                            .peek(player -> player.setRanking(rankCounter.getAndIncrement()))
                            .toList();
                })
                .flatMap(this::saveAllPlayers);
    }


    private Mono<List<Player>> saveAllPlayers(List<Player> players) {
        return Flux.fromIterable(players)
                .flatMap(playerRepository::save)
                .collectList();
    }

    @Override
    public Mono<Void> updatePlayerScore(String playerName, int score) {
        return playerRepository.findByName(playerName)
                .flatMap(player -> {
                    player.setTotalScore(player.getTotalScore() + score);
                    return playerRepository.save(player);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Player not found: " + playerName))).then();
    }


    @Override
    public Mono<Player> updatePlayerName(String playerName, String newName) {
        return playerRepository.findByName(playerName)
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }



}
