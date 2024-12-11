package cat.itacademy.s05.t01.n01.controller;


import cat.itacademy.s05.t01.n01.dto.PlayGameRequest;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody List<String> playerNames){
        return gameService.createGame(playerNames)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id){
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<Game>> playGame(
            @PathVariable String id,
            @RequestBody PlayGameRequest playGameRequest) {
        String playerName = playGameRequest.getPlayerName();
        String move = playGameRequest.getMove();
        int bet = playGameRequest.getBet();

        return gameService.playMove(id, playerName, move, bet)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
