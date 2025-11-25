package be.kdg.ip3.tttbackend.domain;

import java.util.UUID;

public record SessionId(UUID id) {
    public static SessionId generate() {
        return new SessionId(UUID.randomUUID());
    }

}
