package cat.itacademy.s05.t01.n01.controller;

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
        return playerService.createPlayer(playerName)
                .map(savedPlayer -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer));
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
            @RequestBody Map<String, String> request
    ) {
        String newName = request.get("name");
        return playerService.updatePlayerName(playerName, newName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
