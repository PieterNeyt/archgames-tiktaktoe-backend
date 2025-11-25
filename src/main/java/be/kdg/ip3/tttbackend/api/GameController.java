package be.kdg.ip3.tttbackend.api;

import be.kdg.ip3.tttbackend.api.dto.GameDto;
import be.kdg.ip3.tttbackend.api.dto.MoveRequest;
import be.kdg.ip3.tttbackend.api.dto.SessionInfo;
import be.kdg.ip3.tttbackend.application.GameService;
import be.kdg.ip3.tttbackend.application.LauncherClient;
import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.GameId;
import be.kdg.ip3.tttbackend.domain.PlayerMark;
import be.kdg.ip3.tttbackend.domain.SessionId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/ttt/api/games")
@CrossOrigin(origins = "*")
public class GameController {
    private final GameService gameService;
    private final LauncherClient launcherClient;

    public GameController(GameService gameService, LauncherClient launcherClient) {
        this.gameService = gameService;
        this.launcherClient = launcherClient;
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame() {
        Game game = gameService.createNewGame();

        var location = URI.create("/ttt/api/games/" + game.getGameId().id());
        return ResponseEntity.created(location).body(GameDto.FromDomain(game));

    }

    @PostMapping("/ai")
    public ResponseEntity<GameDto> createGameWithAi(@RequestParam PlayerMark human, @RequestParam PlayerMark ai) {
        Game game = gameService.createNewGameWithAi(human, ai);

        var location = URI.create("/ttt/api/games/" + game.getGameId().id());
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

    @PostMapping("/session/{sessionId}/start")
    public ResponseEntity<GameDto> startGameFromSession(
            @PathVariable UUID sessionId,
            @RequestParam(required = false) PlayerMark human,
            @RequestParam(required = false) PlayerMark ai
    ) {
        // 1. Validate session via launcher API
        SessionInfo session = launcherClient.validateSession(new SessionId(sessionId));



        // 2. Create game
        Game game = (human != null && ai != null)
                ? gameService.createNewGameWithAi(human, ai)
                : gameService.createNewGame();

        // 3. Return game
        return ResponseEntity.ok(GameDto.FromDomain(game));
    }

}
