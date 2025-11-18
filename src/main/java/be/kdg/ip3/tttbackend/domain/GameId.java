package be.kdg.ip3.tttbackend.domain;

import java.util.UUID;

public record GameId(UUID id) {
    public static GameId generate() {
        return new GameId(UUID.randomUUID());
    }
}
