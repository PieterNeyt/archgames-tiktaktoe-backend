package be.kdg.ip3.tttbackend.api.dto;

import java.util.UUID;

public record MoveRequest(UUID sessionId, int row, int col) {}