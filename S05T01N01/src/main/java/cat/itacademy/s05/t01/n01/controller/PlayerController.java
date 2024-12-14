package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.exception.BadRequestException;
import cat.itacademy.s05.t01.n01.exception.ResourceNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/new")
    public Mono<ResponseEntity<Player>> createPlayer(@RequestBody String playerName){
        if (playerName == null || playerName.isBlank()) {
            throw new BadRequestException("Player name cannot be null or empty.");
        }
        return playerService.createPlayer(playerName)
                .map(player -> ResponseEntity.status(HttpStatus.CREATED).body(player));
    }

    @GetMapping("/ranking")
    public Mono<ResponseEntity<List<Player>>> getPlayerRanking() {
        return playerService.updateRanking()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @PutMapping("/{playerName}")
    public Mono<ResponseEntity<Player>> updatePlayerName(
            @PathVariable String playerName,
            @RequestBody String newName
    ) {
        if (playerName == null || playerName.isBlank() || newName == null || newName.isBlank()) {
            throw new BadRequestException("Player ID and new name cannot be null or empty.");
        }
        return playerService.updatePlayerName(playerName, newName)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player not found for name: " + playerName)));
    }
    
}
