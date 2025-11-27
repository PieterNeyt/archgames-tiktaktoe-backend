package be.kdg.ip3.tttbackend.portal.messaging.sender;

import be.kdg.ip3.tttbackend.portal.messaging.config.RabbitMQTopology;
import be.kdg.ip3.tttbackend.portal.messaging.config.tttGameResultMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class tttMessagePublisher {
    private final RabbitTemplate rabbitTemplate;
    public tttMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishGameResult(tttGameResultMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQTopology.TTT_EXCHANGE_NAME,
                "ttt.game.result",
                message
        );
    }
}
