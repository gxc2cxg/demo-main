package com.xuchen.demo.work.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xuchen.demo.model.work.constant.WorkConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WorkConfig {

    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange(WorkConstant.DEFAULT_EXCHANGE)
                .delayed()
                .durable(true)
                .build();
    }

    @Bean
    public Queue delayQueue() {
        return new Queue(WorkConstant.DEFAULT_QUEUE);
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(WorkConstant.DEFAULT_KEY);
    }
}