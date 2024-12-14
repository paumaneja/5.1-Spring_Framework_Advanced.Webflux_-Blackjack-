package cat.itacademy.s05.t01.n01.controller;


import cat.itacademy.s05.t01.n01.dto.PlayGameRequest;
import cat.itacademy.s05.t01.n01.exception.BadRequestException;
import cat.itacademy.s05.t01.n01.exception.ResourceNotFoundException;
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
        if (playerNames == null || playerNames.isEmpty()) {
            throw new BadRequestException("Player names cannot be null or empty.");
        }
        return gameService.createGame(playerNames)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id){
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Game ID cannot be null or empty.");
        }
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found for ID: " + id)));
    }

    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<Game>> playGame(
            @PathVariable String id,
            @RequestBody PlayGameRequest playGameRequest) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Game ID cannot be null or empty.");
        }
        return gameService.playMove(id, playGameRequest)
                .map(game -> ResponseEntity.ok(game))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found for ID: " + id)));
    }



    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Game ID cannot be null or empty.");
        }
        return gameService.deleteGame(id)
                .map(deleted -> ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found for ID: " + id)));
    }


}
