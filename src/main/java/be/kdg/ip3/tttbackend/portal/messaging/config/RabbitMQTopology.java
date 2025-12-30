package be.kdg.ip3.tttbackend.portal.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String TTT_EXCHANGE_NAME = "ttt-exchange";
    public static final String TTT_QUEUE_NAME = "ttt-queue";

    public static final String REGISTER_GAME_EXCHANGE = "register-exchange";
    public static final String REGISTER_GAME_QUEUE = "register-queue";


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

    // register game topology
    
    @Bean
    TopicExchange registerExchange() {
        return new TopicExchange(REGISTER_GAME_EXCHANGE);
    }

    @Bean
    Queue registerQueue() {
        return QueueBuilder.nonDurable(REGISTER_GAME_QUEUE).build();
    }

    @Bean
    Binding registerQueueToRegisterExchangeBinding() {
        return BindingBuilder.bind(registerQueue()).to(registerExchange()).with("register.game.*");
    }
}