package be.kdg.ip3.tttbackend.domain;

public enum PlayerMark {
    X, O, EMPTY;

    public String toSymbol() {
        return switch (this) {
            case X -> "X";
            case O -> "O";
            case EMPTY -> " ";
        };
    }

    public static PlayerMark fromSymbol(String s) {
        return switch (s) {
            case "X" -> X;
            case "O" -> O;
            case " " -> EMPTY;
            default -> throw new IllegalArgumentException("Invalid symbol: " + s);
        };
    }

}
