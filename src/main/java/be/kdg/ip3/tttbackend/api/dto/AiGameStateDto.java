package be.kdg.ip3.tttbackend.api.dto;

import java.util.List;

public record AiGameStateDto(
        String game_id,
        String game_name,
        List<List<String>> board,
        String current_player,
        String ai_player
) {
}
