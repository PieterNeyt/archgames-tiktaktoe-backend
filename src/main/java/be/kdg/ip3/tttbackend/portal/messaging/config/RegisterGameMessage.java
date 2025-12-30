package be.kdg.ip3.tttbackend.portal.messaging.config;

import java.math.BigDecimal;

public record RegisterGameMessage(
        String title,
        String description,
        String imageUrl,
        String gameUrl,
        BigDecimal price,
        GameGenre genre,
        int maxlobbysize
) {
}