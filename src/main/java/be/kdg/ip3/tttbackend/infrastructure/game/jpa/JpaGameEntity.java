package be.kdg.ip3.tttbackend.infrastructure.game.jpa;

import be.kdg.ip3.tttbackend.domain.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "games")
public class JpaGameEntity {
    @Id
    private UUID gameId;
    private UUID lobbyId;
    private UUID gameTypeId;
    private UUID sessionIdX;
    private UUID sessionIdO;

    @Embedded
    private JpaBoard board;

    @Enumerated(EnumType.STRING)
    private PlayerMark currentPlayer;

    @Enumerated(EnumType.STRING)
    private PlayerMark aiPlayer;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Enumerated(EnumType.STRING)
    private PlayerMark winner;

    public static JpaGameEntity fromDomain(Game game) {
        JpaGameEntity entity = new JpaGameEntity();
        entity.gameId = game.getGameId().id();
        entity.lobbyId = game.getLobbyId();
        entity.gameTypeId = game.getGameTypeId();
        entity.sessionIdX = game.getSessionIdX();
        entity.sessionIdO = game.getSessionIdO();
        entity.board = new JpaBoard(game.getBoard());
        entity.currentPlayer = game.getCurrentPlayer();
        entity.aiPlayer = game.getAiPlayer();
        entity.gameStatus = game.getGameStatus();
        entity.winner = game.getWinner();
        return entity;
    }

    public Game toDomain() {
        return new Game(new GameId(gameId), lobbyId, gameTypeId, board.toDomain(),
                currentPlayer, gameStatus, winner, sessionIdX, sessionIdO, aiPlayer);
    }
}