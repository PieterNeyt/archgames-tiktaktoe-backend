package be.kdg.ip3.tttbackend.portal.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String TTT_EXCHANGE_NAME = "ttt-exchange";
    public static final String TTT_QUEUE_NAME = "ttt-queue";

    public static final String REGISTER_GAME_EXCHANGE = "register-exchange";
    public static final String ACHIEVEMENT_EXCHANGE_NAME = "achievement-exchange";


    // ttt topology

    @Bean
    TopicExchange tttExchange() {
        return new TopicExchange(TTT_EXCHANGE_NAME);
    }

    @Bean
    Queue tttQueue() {
        return QueueBuilder.nonDurable(TTT_QUEUE_NAME).build();
    }

    @Bean
    Binding tttQueueToTttExchangeBinding() {
        return BindingBuilder.bind(tttQueue()).to(tttExchange()).with("ttt.game.*");
    }

    @Bean
    TopicExchange achievementExchange() {
        return new TopicExchange(ACHIEVEMENT_EXCHANGE_NAME);
    }
    @Bean
    Queue achievementQueue() {
        return QueueBuilder.nonDurable("achievement-queue").build();
    }

    @Bean
    Binding achievementQueueToAchievementExchangeBinding() {
        return BindingBuilder.bind(achievementQueue())
                .to(achievementExchange())
                .with("*.achievement.unlock");
    }
}