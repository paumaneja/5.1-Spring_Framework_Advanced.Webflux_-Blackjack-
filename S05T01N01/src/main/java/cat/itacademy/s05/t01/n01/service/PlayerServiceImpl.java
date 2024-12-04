package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
}
