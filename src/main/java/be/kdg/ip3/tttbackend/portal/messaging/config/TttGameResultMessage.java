package be.kdg.ip3.tttbackend.portal.messaging.config;

import java.time.LocalDateTime;
import java.util.UUID;

public record TttGameResultMessage(UUID sessionId, UUID gameId, String winner, LocalDateTime timestamp) {
}
