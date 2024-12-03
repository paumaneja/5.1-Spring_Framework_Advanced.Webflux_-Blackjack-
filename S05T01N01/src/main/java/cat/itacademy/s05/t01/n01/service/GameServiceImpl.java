package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    @Autowired
    private final GameRepository gameRepository;

    @Override
    public Mono<Game> createGame(String playerName){
        Game newGame = new Game();
        newGame.setPlayerName(playerName);
        newGame.setCards(new ArrayList<>());
        newGame.setStatus("Active");
        return gameRepository.save(newGame);
    }

    public Mono<Game> getGame(String id){
        return gameRepository.findById(id);
    }

}
