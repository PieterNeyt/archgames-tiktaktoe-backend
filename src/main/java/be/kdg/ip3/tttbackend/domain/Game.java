package be.kdg.ip3.tttbackend.domain;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.Random;
import java.util.UUID;

@Getter
@AggregateRoot
public class Game {
    private final GameId gameId;
    private final UUID lobbyId;
    private final UUID gameTypeId;

    private Board board;
    private PlayerMark currentPlayer; // Wie is er aan de beurt (X begint altijd)
    private GameStatus gameStatus;
    private PlayerMark winner;

    private UUID sessionIdX; // Sessie-ID van de speler die X heeft
    private UUID sessionIdO; // Sessie-ID van de speler die O heeft
    private final PlayerMark aiPlayer; // Welk teken is de AI (indien van toepassing)

    public Game(GameId gameId, UUID lobbyId, UUID gameTypeId, Board board,
                PlayerMark currentPlayer, GameStatus gameStatus, PlayerMark winner,
                UUID sessionIdX, UUID sessionIdO, PlayerMark aiPlayer) {
        this.gameId = gameId;
        this.lobbyId = lobbyId;
        this.gameTypeId = gameTypeId;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.gameStatus = gameStatus;
        this.winner = winner;
        this.sessionIdX = sessionIdX;
        this.sessionIdO = sessionIdO;
        this.aiPlayer = aiPlayer;
    }

    public static Game createWaitingMultiplayer(UUID sessionId, UUID lobbyId, UUID gameTypeId) {
        boolean playerIsX = new Random().nextBoolean();
        return new Game(
                GameId.generate(),
                lobbyId,
                gameTypeId,
                new Board(),
                PlayerMark.X,
                GameStatus.WAITING_FOR_PLAYER,
                null,
                playerIsX ? sessionId : null, // Als player X is, zet sessionId hier
                playerIsX ? null : sessionId, // Anders zet sessionId bij O
                null
        );
    }

    public static Game createSinglePlayer(UUID sessionId, UUID lobbyId, UUID gameTypeId, String humanMark) {
        boolean playerIsX = humanMark.equals("X");
        PlayerMark aiMark = playerIsX ? PlayerMark.O : PlayerMark.X;
        return new Game(
                GameId.generate(),
                lobbyId,
                gameTypeId,
                new Board(),
                PlayerMark.X,
                GameStatus.IN_PROGRESS,
                null,
                playerIsX ? sessionId : null,
                playerIsX ? null : sessionId,
                aiMark
        );
    }

    public void join(UUID sessionId) {
        if (this.gameStatus != GameStatus.WAITING_FOR_PLAYER) {
            throw new IllegalStateException("Game is niet in wachtstand.");
        }
        // Vul het lege vakje in
        if (this.sessionIdX == null) this.sessionIdX = sessionId;
        else this.sessionIdO = sessionId;

        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    public void makeMove(int row, int col, UUID sessionId) {
        if (gameStatus != GameStatus.IN_PROGRESS) throw new IllegalStateException("Game is over.");

        // Bepaal de verwachte sessionId voor de huidige speler
        UUID expectedSession = (currentPlayer == PlayerMark.X) ? sessionIdX : sessionIdO;

        // Validatie: komt de sessionId overeen met de huidige beurt?
        // Als sessionId null is, gaan we ervan uit dat het een AI-zet is
        // Als expectedSession null is, dan is de huidige speler de AI (toegestaan)
        if (sessionId != null && !sessionId.equals(expectedSession)) {
            throw new IllegalArgumentException("Het is niet jouw beurt!");
        }

        // Extra validatie: als sessionId null is (AI-zet), moet de huidige speler ook de AI zijn
        if (sessionId == null && !currentPlayer.equals(aiPlayer)) {
            throw new IllegalArgumentException("Alleen de AI mag een zet doen zonder sessionId!");
        }

        this.board = board.placeMark(row, col, currentPlayer);
        updateStatus();

        if (gameStatus == GameStatus.IN_PROGRESS) {
            this.currentPlayer = (currentPlayer == PlayerMark.X) ? PlayerMark.O : PlayerMark.X;
        }
    }

    private void updateStatus() {
        PlayerMark winnerFound = detectWinner(board);
        if (winnerFound != null) {
            this.winner = winnerFound;
            this.gameStatus = GameStatus.FINISHED;
        } else if (board.isFull()) {
            this.gameStatus = GameStatus.DRAW;
        }
    }

    public boolean isFinished() {
        return gameStatus == GameStatus.FINISHED || gameStatus == GameStatus.DRAW;
    }

    private PlayerMark detectWinner(Board board) {
        // Logica zoals je die al had (rijen, kolommen, diagonalen checken)
        for (int i = 0; i < 3; i++) {
            if (checkThree(board.getCell(i, 0), board.getCell(i, 1), board.getCell(i, 2))) return board.getCell(i, 0);
            if (checkThree(board.getCell(0, i), board.getCell(1, i), board.getCell(2, i))) return board.getCell(0, i);
        }
        if (checkThree(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2))) return board.getCell(1, 1);
        if (checkThree(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0))) return board.getCell(1, 1);
        return null;
    }

    private boolean checkThree(PlayerMark p1, PlayerMark p2, PlayerMark p3) {
        return p1 != PlayerMark.EMPTY && p1 == p2 && p2 == p3;
    }
}