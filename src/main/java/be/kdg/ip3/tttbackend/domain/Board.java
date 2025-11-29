package be.kdg.ip3.tttbackend.domain;

import org.jmolecules.ddd.annotation.Entity;

import java.util.Arrays;
import java.util.List;

@Entity
public class Board {
    public static final int SIZE = 3;
    private final PlayerMark[][] board;

    public Board() {
        this.board = new PlayerMark[SIZE][SIZE];
        for (PlayerMark[] row : board) {
            Arrays.fill(row, PlayerMark.EMPTY);
        }
    }

    public Board(PlayerMark[][] board) {
        this.board = board;
    }

    public PlayerMark getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == PlayerMark.EMPTY;
    }

    public Board placeMark(int row, int col, PlayerMark mark) {
        validateCoordinates(row, col);
        if (!isCellEmpty(row, col)) {
            throw new IllegalArgumentException("Cell is already occupied");
        }

        PlayerMark[][] newBoard = deepCopyBoard();
        newBoard[row][col] = mark;

        return new Board(newBoard);
    }

    private PlayerMark[][] deepCopyBoard() {
        PlayerMark[][] newBoard = new PlayerMark[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
        }
        return newBoard;
    }

    public boolean isFull() {
        for (PlayerMark[] row : board) {
            for (PlayerMark mark : row) {
                if (mark == PlayerMark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validateCoordinates(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Coordinates Out of bounds");
        }
    }

    public List<List<String>> toMatrix() {

        return Arrays.stream(board)
                .map(row -> Arrays.stream(row)
                        .map(PlayerMark::toSymbol)
                        .toList())
                .toList();
    }


        public static Board fromMatrix(List<List<String>> matrix) {
        PlayerMark[][] grid = new PlayerMark[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String symbol = matrix.get(row).get(col);
                grid[row][col] = PlayerMark.fromSymbol(symbol);
            }
        }

        return new Board(grid);
    }

}
