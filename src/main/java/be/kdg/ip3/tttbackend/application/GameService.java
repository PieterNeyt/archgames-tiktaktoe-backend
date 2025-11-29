package be.kdg.ip3.tttbackend.application;


import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import be.kdg.ip3.tttbackend.domain.*;
import be.kdg.ip3.tttbackend.portal.ai.AiClient;
import be.kdg.ip3.tttbackend.portal.messaging.config.tttGameResultMessage;
import be.kdg.ip3.tttbackend.portal.messaging.sender.tttMessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameService {
    private final GameRepository games;
    private final AiClient aiClient;
    private final tttMessagePublisher messagePublisher;


    public GameService(GameRepository games, AiClient aiClient, tttMessagePublisher messagePublisher) {
        this.games = games;
        this.aiClient = aiClient;
        this.messagePublisher = messagePublisher;
    }
    public Game createGame() {
        return createNewGame(null);
    }

    public Game createNewGame(SessionId sessionId) {
        Game game = Game.newHvHGame(sessionId);
        games.save(game);
        return game;
    }

    public Game createNewGameWithAi(PlayerMark human, PlayerMark ai) {
        return createNewGameWithAi(null, human, ai);
    }

    public Game createNewGameWithAi(SessionId sessionId, PlayerMark human, PlayerMark ai) {
        Game game = Game.newHvAIGame(sessionId, human, ai);
        games.save(game);
        return game;
    }

    public Game findById(GameId gameId) {
        return games.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game with id " + gameId + " not found"));
    }

//    public Game playMove(GameId gameId, int row, int col) {
//        Game game = findById(gameId);
//        Game updatedGame = game.playMove(row, col);
//        games.save(updatedGame);
//        return game;
//    }

    public Game playMove(GameId id, int row, int col) {
        Game game = findById(id);

        Game updatedGame = game.playMove(row, col);

        // 🔹 Als het spel na de human move gedaan is
        if (updatedGame.isFinished()) {
            games.save(updatedGame);
            publishResultIfFinished(updatedGame);
            return updatedGame;
        }

        // 🔹 AI aan de beurt?
        if (updatedGame.getAiPlayer() != null &&
                updatedGame.getCurrentPlayer() == updatedGame.getAiPlayer()) {

            var dto = new AiGameStateDto(
                    updatedGame.getGameId().id().toString(),
                    "TICTACTOE",
                    updatedGame.getBoard().toMatrix(),
                    updatedGame.getCurrentPlayer().name(),
                    updatedGame.getAiPlayer().name()
            );

            var aiResponse = aiClient.requestAiMove(dto);

            int aiRow = aiResponse.get("row");
            int aiCol = aiResponse.get("col");

            Game finalUpdatedGame = updatedGame.playMove(aiRow, aiCol);
            games.save(finalUpdatedGame);
            publishResultIfFinished(finalUpdatedGame);
            return finalUpdatedGame;
        }

        games.save(updatedGame);
        return updatedGame;
    }

    private void publishResultIfFinished(Game game) {
        if (!game.isFinished()) return;
        if (game.getSessionId() == null) return;

        var message = new tttGameResultMessage(
                game.getSessionId().id(),
                game.getGameId().id(),
                game.getWinner() != null
                        ? game.getWinner().name()
                        : "DRAW"
        );

        messagePublisher.publishGameResult(message);
    }
}


