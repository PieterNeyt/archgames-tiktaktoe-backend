package be.kdg.ip3.tttbackend.api.dto;

import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.PlayerMark;

import java.util.List;
import java.util.UUID;

public record GameDto(
        UUID gameId,
        List<List<String>> board,
        String currentPlayer,
        String aiPlayer,
        String gameStatus,
        String winner,
        String myMark
) {
    public static GameDto FromDomain(Game game, UUID sessionId) {
        String myMark = null;

        if (sessionId != null) {
            if (sessionId.equals(game.getSessionIdX())) {
                myMark = PlayerMark.X.name();
            } else if (sessionId.equals(game.getSessionIdO())) {
                myMark = PlayerMark.O.name();
            }
        }

        return new GameDto(
                game.getGameId().id(),
                game.getBoard().toMatrix(),
                game.getCurrentPlayer() != null ? game.getCurrentPlayer().name() : null,
                game.getAiPlayer() != null ? game.getAiPlayer().name() : null,
                game.getGameStatus().name(),
                game.getWinner() != null ? game.getWinner().name() : null,
                myMark
        );
    }


}
