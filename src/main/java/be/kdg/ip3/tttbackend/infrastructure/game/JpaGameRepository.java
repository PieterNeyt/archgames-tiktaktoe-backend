package be.kdg.ip3.tttbackend.infrastructure.game;

import be.kdg.ip3.tttbackend.infrastructure.game.jpa.JpaGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaGameRepository extends JpaRepository<JpaGameEntity, UUID> {
    @Query("SELECT g FROM JpaGameEntity g WHERE g.lobbyId = :lobbyId AND g.gameStatus IN ('IN_PROGRESS', 'WAITING_FOR_PLAYER')")
    Optional<JpaGameEntity> findActiveGameByLobbyId(@Param("lobbyId") UUID lobbyId);
}