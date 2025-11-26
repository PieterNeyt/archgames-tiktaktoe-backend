package be.kdg.ip3.tttbackend.api.dto;

import java.util.UUID;

public record SessionInfo(
        UUID sessionId,
        UUID gameLobbyId,
        UUID playerId,
        UUID gameId
) {}
