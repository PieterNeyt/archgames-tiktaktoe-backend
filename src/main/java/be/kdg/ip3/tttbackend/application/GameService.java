package be.kdg.ip3.tttbackend.application;


import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.GameId;
import be.kdg.ip3.tttbackend.domain.GameRepository;
import be.kdg.ip3.tttbackend.domain.PlayerMark;
import be.kdg.ip3.tttbackend.infrastructure.ai.AiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameService {
    private final GameRepository games;
    private final AiClient aiClient;


    public GameService(GameRepository games, AiClient aiClient) {
        this.games = games;
        this.aiClient = aiClient;
    }

    public Game createNewGame() {
        Game game = Game.newHvHGame();
        games.save(game);
        return game;
    }

    public Game createNewGameWithAi(PlayerMark human, PlayerMark ai) {
        Game game = Game.newHvAIGame(human, ai);
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

        if (updatedGame.isFinished()) {
            games.save(updatedGame);
            return updatedGame;
        }

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
            return finalUpdatedGame;
        }

        games.save(updatedGame);
        return updatedGame;
    }
}
