package be.kdg.ip3.tttbackend.portal.messaging.sender;

import be.kdg.ip3.tttbackend.portal.messaging.config.RabbitMQTopology;
import be.kdg.ip3.tttbackend.portal.messaging.config.TttGameResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

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
}
