package be.kdg.ip3.tttbackend.infrastructure.game.jpa;

import be.kdg.ip3.tttbackend.domain.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class JpaBoard {

    @Column(name = "board_json", columnDefinition = "jsonb")
    private String boardJson;

    private static final ObjectMapper mapper = new ObjectMapper();

    public JpaBoard(Board board) {
        try {
            this.boardJson = mapper.writeValueAsString(board.toMatrix());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize board", e);
        }
    }

    public Board toDomain() {
        try {
            List<List<String>> matrix = mapper.readValue(boardJson, List.class);
            return Board.fromMatrix(matrix);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize board", e);
        }
    }
}
