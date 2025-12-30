package be.kdg.ip3.tttbackend.portal.messaging.sender;

import be.kdg.ip3.tttbackend.portal.messaging.config.GameGenre;
import be.kdg.ip3.tttbackend.portal.messaging.config.RabbitMQTopology;
import be.kdg.ip3.tttbackend.portal.messaging.config.RegisterGameMessage;
import be.kdg.ip3.tttbackend.portal.messaging.config.TttGameResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class tttMessagePublisher {
    private final RabbitTemplate rabbitTemplate;

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

    @EventListener(ApplicationReadyEvent.class)
    public void publishGameRegister() {
        var message = new RegisterGameMessage(
                "Tic tac toe from publish",
                "tinker tanker toe",
                null,
                "http://localhost:5173/kust-men-klote",
                BigDecimal.valueOf(23.5),
                GameGenre.STRATEGY,
                2
        );
        rabbitTemplate.convertAndSend(
                RabbitMQTopology.REGISTER_GAME_EXCHANGE,
                "register.game.ttt",
                message
        );
        log.info("Published register game message: {}", message);
    }
}
