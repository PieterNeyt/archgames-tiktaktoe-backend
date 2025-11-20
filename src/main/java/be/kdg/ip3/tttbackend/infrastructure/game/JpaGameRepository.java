package be.kdg.ip3.tttbackend.infrastructure.game;

import be.kdg.ip3.tttbackend.infrastructure.game.jpa.JpaGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGameRepository extends JpaRepository<JpaGameEntity, UUID> {
}
