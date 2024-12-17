package cat.itacademy.s05.t01.n01.controller;


import cat.itacademy.s05.t01.n01.dto.PlayGameRequest;
import cat.itacademy.s05.t01.n01.exception.BadRequestException;
import cat.itacademy.s05.t01.n01.exception.ResourceNotFoundException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/game")
@Tag(name = "Games", description = "Endpoints for managing games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/new")
    @Operation(
            summary = "Create a new game",
            description = "Creates a new game with the provided list of player names."
    )
    public Mono<ResponseEntity<Game>> createGame(@RequestBody List<String> playerNames){
        if (playerNames == null || playerNames.isEmpty()) {
            throw new BadRequestException("Player names cannot be null or empty.");
        }
        return gameService.createGame(playerNames)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get game details",
            description = "Retrieve details of a game by its unique ID."
    )
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id){
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Game ID cannot be null or empty.");
        }
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found for ID: " + id)));
    }

    @PostMapping("/{id}/play")
    @Operation(
            summary = "Play a game move",
            description = "Processes a move in the game with the given ID and move details."
    )
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
    @Operation(
            summary = "Delete a game",
            description = "Deletes a game by its unique ID."
    )
    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Game ID cannot be null or empty.");
        }
        return gameService.deleteGame(id)
                .map(deleted -> ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found for ID: " + id)));
    }


}
