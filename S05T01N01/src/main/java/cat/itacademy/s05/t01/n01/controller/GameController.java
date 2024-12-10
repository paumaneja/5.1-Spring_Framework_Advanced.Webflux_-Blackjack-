package cat.itacademy.s05.t01.n01.controller;


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
            @RequestBody Map<String, String> action) {
        return gameService.getGame(id)
                .flatMap(game -> {
                    String playerName = action.get("playerName");
                    String move = action.get("move");
                    if ("hit".equalsIgnoreCase(move)) {
                        gameService.dealCardToPlayer(game, playerName);
                    } else if ("stand".equalsIgnoreCase(move)) {
                        System.out.println(playerName + " ha decidit plantar-se.");
                    }
                    return gameService.updateGame(id, game);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
