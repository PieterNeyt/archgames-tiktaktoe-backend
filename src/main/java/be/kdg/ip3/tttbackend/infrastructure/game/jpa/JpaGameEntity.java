package be.kdg.ip3.tttbackend.infrastructure.game.jpa;


import be.kdg.ip3.tttbackend.domain.*;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "games")
public class JpaGameEntity {
    @Id
    private UUID gameId;
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
        entity.board = new JpaBoard(game.getBoard());
        entity.currentPlayer = game.getCurrentPlayer();
        entity.aiPlayer = game.getAiPlayer();
        entity.gameStatus = game.getGameStatus();
        entity.winner = game.getWinner();

        return entity;
    }

    public Game toDomain() {
        return new Game(
                new GameId(gameId),
                board.toDomain(),
                currentPlayer,
                aiPlayer,
                gameStatus,
                winner
        );
    }

}
