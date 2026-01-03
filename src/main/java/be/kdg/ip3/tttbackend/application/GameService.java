package be.kdg.ip3.tttbackend.application;

import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import be.kdg.ip3.tttbackend.domain.*;
import be.kdg.ip3.tttbackend.portal.ai.AiClient;
import be.kdg.ip3.tttbackend.portal.messaging.config.AchievementUnlockedMessage;
import be.kdg.ip3.tttbackend.portal.messaging.config.TttGameResultMessage;
import be.kdg.ip3.tttbackend.portal.messaging.sender.tttMessagePublisher;
import be.kdg.ip3.tttbackend.portal.rest.LauncherClient;
import be.kdg.ip3.tttbackend.api.dto.SessionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class GameService {
    private final GameRepository games;
    private final AiClient aiClient;
    private final tttMessagePublisher messagePublisher;
    private final LauncherClient launcherClient;

    public GameService(GameRepository games, AiClient aiClient, tttMessagePublisher messagePublisher, LauncherClient launcherClient) {
        this.games = games;
        this.aiClient = aiClient;
        this.messagePublisher = messagePublisher;
        this.launcherClient = launcherClient;
    }

    public Game startSinglePlayer(UUID sessionId, UUID lobbyId, String humanMark) {
        SessionInfo session = launcherClient.validateSession(new SessionId(sessionId));

        games.findActiveGameByLobbyId(lobbyId).ifPresent(g -> {
            throw new IllegalStateException("There is already a game with the same lobby ID");
        });

        Game game = Game.createSinglePlayer(sessionId, session.playerId(), lobbyId, session.gameId(), humanMark);
        games.save(game);

        if (game.getAiPlayer() == PlayerMark.X) {
            triggerAiMove(game);
            games.save(game);
        }

        return game;
    }


    public Game joinOrCreateMultiplayer(UUID sessionId, UUID lobbyId) {
        SessionInfo session = launcherClient.validateSession(new SessionId(sessionId));
        var activeGameOpt = games.findActiveGameByLobbyId(lobbyId);

        if (activeGameOpt.isPresent()) {
            Game game = activeGameOpt.get();
            if (game.getAiPlayer() != null) throw new IllegalStateException("Wacht tot AI spel klaar is.");

            if (game.getGameStatus() == GameStatus.WAITING_FOR_PLAYER) {
                if (sessionId.equals(game.getSessionIdX()) || sessionId.equals(game.getSessionIdO())) return game;
                game.join(sessionId, session.playerId());
                games.save(game);
                return game;
            }
            return game; // Reconnect voor bestaande speler
        }

        Game newGame = Game.createWaitingMultiplayer(sessionId, session.playerId(), lobbyId, session.gameId());
        games.save(newGame);
        return newGame;
    }

    public Game playMove(GameId id, UUID sessionId, int row, int col) {
        Game game = findById(id);
        game.makeMove(row, col, sessionId);

        if (game.isFinished()) {
            publishResult(game);
        } else if (game.getAiPlayer() != null && game.getCurrentPlayer() == game.getAiPlayer()) {
            triggerAiMove(game);
        }

        games.save(game);
        return game;
    }

    private void triggerAiMove(Game game) {
        var dto = new AiGameStateDto(game.getGameId().id().toString(), "TICTACTOE", game.getBoard().toMatrix(),
                game.getCurrentPlayer().name(), game.getAiPlayer().name());
        var response = aiClient.requestAiMove(dto);
        game.makeMove(response.get("row"), response.get("col"), null); // AI heeft geen sessionId nodig in domain
        if (game.isFinished()) publishResult(game);
    }

    private void publishResult(Game game) {
        // Publish achievement events alleen voor niet-null player IDs
        if (game.getPlayerXId() != null) {
            messagePublisher.publishAchievementUnlock(
                    new AchievementUnlockedMessage("EXT-TT-01", game.getPlayerXId(), game.getGameTypeId())
            );
        }
        if (game.getPlayerOId() != null) {
            messagePublisher.publishAchievementUnlock(
                    new AchievementUnlockedMessage("EXT-TT-01", game.getPlayerOId(), game.getGameTypeId())
            );
        }

        // Bepaal winnerSession (kan null zijn bij draw)
        UUID winnerSession = null;
        if (game.getWinner() == PlayerMark.X) {
            winnerSession = game.getSessionIdX();
        } else if (game.getWinner() == PlayerMark.O) {
            winnerSession = game.getSessionIdO();
        }

        if (winnerSession!=null){
            messagePublisher.publishGameResult(new TttGameResultMessage(
                    winnerSession,
                    game.getWinner().name(),
                    LocalDateTime.now()
            ));
        }

    }

    public Game findById(GameId gameId) {
        return games.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game niet gevonden"));
    }
}