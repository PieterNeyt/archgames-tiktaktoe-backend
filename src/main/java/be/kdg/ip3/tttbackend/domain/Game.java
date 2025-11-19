package be.kdg.ip3.tttbackend.domain;

import java.util.Objects;

public class Game {
    private final GameId gameId;
    private final Board board;
    private final PlayerMark currentPlayer;
    private final PlayerMark aiPlayer;
    private final GameStatus gameStatus;
    private final PlayerMark winner;

    public Game(GameId gameId, Board board, PlayerMark currentPlayer, PlayerMark aiPlayer, GameStatus gameStatus, PlayerMark winner) {
        this.gameId = gameId;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.aiPlayer = aiPlayer;
        this.gameStatus = gameStatus;
        this.winner = winner;
    }

    public static Game newHvHGame() {
        return new Game(
                GameId.generate(),
                new Board(),
                PlayerMark.X,
                null,
                GameStatus.IN_PROGRESS,
                null
        );
    }

    public static Game newHvAIGame(PlayerMark aiPlayer, PlayerMark humanPlayer) {
        Objects.requireNonNull(aiPlayer);
        Objects.requireNonNull(humanPlayer);

        if (humanPlayer.equals(aiPlayer)) {
            throw new IllegalArgumentException("AI player and human player must be different");
        }

        return new Game(
                GameId.generate(),
                new Board(),
                humanPlayer, // h begint
                aiPlayer,
                GameStatus.IN_PROGRESS,
                null
        );
    }

    public Game playMove(int row, int col) {
        if (gameStatus != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is already over");
        }

        Board newBoard = board.placeMark(row, col, currentPlayer);
        PlayerMark winnerAfterMove = detectWinner(newBoard);
        GameStatus newGameStatus;
        PlayerMark newWinner = null;

        if (winnerAfterMove != null) {
            newGameStatus = GameStatus.FINISHED;
            newWinner = winnerAfterMove;
        } else if (newBoard.isFull()) {
            newGameStatus = GameStatus.DRAW;
        } else {
            newGameStatus = GameStatus.IN_PROGRESS;
        }

        PlayerMark nextPlayer =  (newGameStatus == GameStatus.IN_PROGRESS) ? toggle(currentPlayer) : currentPlayer;

        return new Game(
                this.gameId,
                newBoard,
                nextPlayer,
                this.aiPlayer,
                newGameStatus,
                newWinner
        );
    }

    private PlayerMark toggle(PlayerMark player) {
        return (player == PlayerMark.X) ? PlayerMark.O : PlayerMark.X;
    }

    private PlayerMark detectWinner(Board board) {
        // Rijen
        for (int row = 0; row < Board.SIZE; row++) {
            PlayerMark first = board.getCell(row, 0);
            if (first != PlayerMark.EMPTY &&
                    first == board.getCell(row, 1) &&
                    first == board.getCell(row, 2)) {
                return first;
            }
        }
        // Kolommen
        for (int col = 0; col < Board.SIZE; col++) {
            PlayerMark first = board.getCell(0, col);
            if (first != PlayerMark.EMPTY &&
                    first == board.getCell(1, col) &&
                    first == board.getCell(2, col)) {
                return first;
            }
        }

        // Diagonalen - linksboven naar rechtsonder
        PlayerMark center = board.getCell(0, 0);
        if (center != PlayerMark.EMPTY) {
            if (center == board.getCell(1, 1) && center == board.getCell(2, 2)) {
                return center;
            }
        }

        // Diagonalen - rechtsboven naar linksonder
        PlayerMark topRight = board.getCell(0, 2);
        if (topRight != PlayerMark.EMPTY &&
                topRight == board.getCell(1, 1) &&
                topRight == board.getCell(2, 0)) {
            return topRight;
        }

        return null;
    }
}
