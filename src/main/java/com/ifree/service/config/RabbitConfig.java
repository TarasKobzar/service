package com.ifree.service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public final static String QUEUE_NAME = "AppQueue";

    @Bean
    public Queue requestWithDurationQueue() {
        return new Queue(QUEUE_NAME);
    }
}
