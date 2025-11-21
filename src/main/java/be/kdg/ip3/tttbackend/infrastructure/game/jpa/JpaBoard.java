package be.kdg.ip3.tttbackend.infrastructure.game.jpa;

import be.kdg.ip3.tttbackend.domain.Board;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class JpaBoard {

    @Type(JsonType.class)
    @Column(name = "board_json", columnDefinition = "jsonb")
    private String boardJson;

    private static final ObjectMapper mapper = new ObjectMapper();

    public JpaBoard(Board board) {
        try {
            this.boardJson = mapper.writeValueAsString(board.toMatrix());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Board toDomain() {
        try {
            List<List<String>> matrix =
                    mapper.readValue(boardJson, List.class);
            return Board.fromMatrix(matrix);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
