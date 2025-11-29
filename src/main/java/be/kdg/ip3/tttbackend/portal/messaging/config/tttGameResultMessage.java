package be.kdg.ip3.tttbackend.portal.messaging.config;

import java.util.UUID;

public record tttGameResultMessage(UUID sessionId, UUID gameId, String winner) {
}
