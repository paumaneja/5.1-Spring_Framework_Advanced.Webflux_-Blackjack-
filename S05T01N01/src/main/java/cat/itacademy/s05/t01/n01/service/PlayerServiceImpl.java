package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private final PlayerRepository playerRepository;


    @Override
    public Mono<Player> createPlayer(String playerName){
        Player newPlayer = new Player();
        newPlayer.setName(playerName);
        newPlayer.setTotal_score(0);
        return playerRepository.save(newPlayer);
    }

    @Override
    public Mono<List<Player>> updateRanking() {
        return getAllPlayers()
                .map(players -> players.stream()
                        .sorted(Comparator.comparingInt(Player::getTotalScore).reversed())
                        .peek(player -> {
                            int rank = players.indexOf(player) + 1;
                            player.setRanking(rank);
                        })
                        .toList())
                .flatMap(players -> saveAllPlayers(players));
    }

    @Override
    private Mono<List<Player>> saveAllPlayers(List<Player> players) {
        return Flux.fromIterable(players)
                .flatMap(playerRepository::save)
                .collectList();
    }

    @Override
    public Mono<Void> updatePlayerScore(String playerId, int additionalScore) {
        return getPlayerById(playerId)
                .flatMap(player -> {
                    player.setTotalScore(player.getTotalScore() + additionalScore);
                    return playerRepository.save(player);
                })
                .then();
    }


}
