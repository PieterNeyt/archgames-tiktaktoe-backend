package be.kdg.ip3.tttbackend.portal.messaging.sender;

import be.kdg.ip3.tttbackend.portal.messaging.config.AchievementUnlockedMessage;
import be.kdg.ip3.tttbackend.portal.messaging.config.RabbitMQTopology;
import be.kdg.ip3.tttbackend.portal.messaging.config.RegisterGameMessage;
import be.kdg.ip3.tttbackend.portal.messaging.config.TttGameResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class tttMessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${game.title}")
    private String title;
    @Value("${game.description}")
    private String description;
    @Value("${game.imageUrl}")
    private String imageUrl;
    @Value("${game.gameUrl}")
    private String gameUrl;
    @Value("${game.price}")
    private double price;
    @Value("${game.genre}")
    private String genre;
    @Value("${game.maxlobbysize}")
    private int maxLobbySize;

    public tttMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishGameResult(TttGameResultMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQTopology.TTT_EXCHANGE_NAME,
                "ttt.game.result",
                message
        );
        log.info("Published TTT game result message: {}", message);
    }

    public void publishAchievementUnlock(AchievementUnlockedMessage message) {

        rabbitTemplate.convertAndSend(
                RabbitMQTopology.ACHIEVEMENT_EXCHANGE_NAME,
                "checkers.achievement.unlock",
                message
        );

    }
    @EventListener(ApplicationReadyEvent.class)
    public void publishGameRegister() {
        var message = new RegisterGameMessage(
                title,
                description,
                imageUrl,
                gameUrl,
                BigDecimal.valueOf(price),
                genre,
                maxLobbySize
        );
        rabbitTemplate.convertAndSend(
                RabbitMQTopology.REGISTER_GAME_EXCHANGE,
                "register.game.ttt",
                message
        );
        log.info("Published register game message: {}", message);
    }
}
