package be.kdg.ip3.tttbackend.domain;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(GameId gameId);
    void save(Game restaurant);
    List<Game> findAll();

}
