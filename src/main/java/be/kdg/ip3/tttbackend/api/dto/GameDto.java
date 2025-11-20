package be.kdg.ip3.tttbackend.api.dto;

import be.kdg.ip3.tttbackend.domain.Game;

import java.util.List;
import java.util.UUID;

public record GameDto(
        UUID gameId,
        List<List<String>> board,
        String currentPlayer,
        String aiPlayer,
        String gameStatus,
        String winner
) {
    public static GameDto FromDomain(Game game) {
        return  new GameDto(
                game.getGameId().id(),
                game.getBoard().toMatrix(),
                game.getCurrentPlayer() != null ? game.getCurrentPlayer().name() : null,
                game.getAiPlayer() != null ? game.getAiPlayer().name() : null,
                game.getGameStatus().name(),
                game.getWinner() != null ? game.getWinner().name() : null
        );

    }

}
