package be.kdg.ip3.tttbackend.infrastructure;

import be.kdg.ip3.tttbackend.domain.Game;
import be.kdg.ip3.tttbackend.domain.GameId;
import be.kdg.ip3.tttbackend.domain.GameRepository;
import be.kdg.ip3.tttbackend.infrastructure.game.JpaGameRepository;
import be.kdg.ip3.tttbackend.infrastructure.game.jpa.JpaGameEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DbGameRepository implements GameRepository {
    private final JpaGameRepository jpaGameRepository;

    public DbGameRepository(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Override
    public Optional<Game> findById(GameId gameId) {
        return this.jpaGameRepository.findById(gameId.id()).map(JpaGameEntity::toDomain);
    }

    @Override
    public void save(Game restaurant) {
        JpaGameEntity jpaGameEntity = JpaGameEntity.fromDomain(restaurant);
        this.jpaGameRepository.save(jpaGameEntity);
    }

    @Override
    public List<Game> findAll() {
        return this.jpaGameRepository.findAll().stream().map(JpaGameEntity::toDomain).toList();
    }
}
