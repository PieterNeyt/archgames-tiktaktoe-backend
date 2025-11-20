package be.kdg.ip3.tttbackend.api;

import be.kdg.ip3.tttbackend.api.dto.GameDto;
import be.kdg.ip3.tttbackend.api.dto.MoveRequest;
import be.kdg.ip3.tttbackend.application.GameService;
import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.GameId;
import be.kdg.ip3.tttbackend.domain.PlayerMark;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameDto gameDto) {
        Game game = gameService.createNewGame();

        var location = URI.create("/api/games/" + game.getGameId().id());
        return ResponseEntity.created(location).body(GameDto.FromDomain(game));

    }

    @PostMapping("/ai")
    public ResponseEntity<GameDto> createGameWithAi(@Valid @RequestBody GameDto gameDto) {
        Game game = gameService.createNewGameWithAi(
                gameDto.currentPlayer() != null ? Enum.valueOf(PlayerMark.class, gameDto.currentPlayer()) : null,
                gameDto.aiPlayer() != null ? Enum.valueOf(PlayerMark.class, gameDto.aiPlayer()) : null);

        var location = URI.create("/api/games/" + game.getGameId().id());
        return ResponseEntity.created(location).body(GameDto.FromDomain(game));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDto> getGameById(@PathVariable String gameId) {
        Game game = gameService.findById(new GameId(UUID.fromString(gameId)));
        return ResponseEntity.ok(GameDto.FromDomain(game));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameDto> playMove(@PathVariable String gameId, @Valid @RequestBody MoveRequest moveRequest) {
        Game game = gameService.playMove(new GameId(UUID.fromString(gameId)), moveRequest.row(), moveRequest.col());
        return ResponseEntity.ok(GameDto.FromDomain(game));
    }
}
