package com.xuchen.demo.work.config;

import com.xuchen.demo.model.work.constant.WorkConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ErrorConfig {

    private final RabbitTemplate rabbitTemplate;

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(WorkConstant.ERROR_EXCHANGE);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(WorkConstant.ERROR_QUEUE);
    }

    @Bean
    public Binding errorQueueBinding(Queue errorQueue, DirectExchange errorExchange) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(WorkConstant.ERROR_KEY);
    }

    @Bean
    public RepublishMessageRecoverer republishMessageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, WorkConstant.ERROR_EXCHANGE, WorkConstant.ERROR_KEY);
    }
}
