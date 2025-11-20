package be.kdg.ip3.tttbackend.application;


import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.GameId;
import be.kdg.ip3.tttbackend.domain.GameRepository;
import be.kdg.ip3.tttbackend.domain.PlayerMark;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameService {
    private final GameRepository games;


    public GameService(GameRepository games) {
        this.games = games;
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

    public Game playMove(GameId gameId, int row, int col) {
        Game game = findById(gameId);
        Game updatedGame = game.playMove(row, col);
        games.save(updatedGame);
        return game;
    }

}
