package com.hyuse98.scheduler.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String USER_REGISTRATION_QUEUE = "core.user.registered.queue";
    public static final String USER_REGISTRATION_EXCHANGE = "user.registered";
    public static final String USER_ACTIVE_QUEUE = "core.user.active.queue";
    public static final String USER_ACTIVE_EXCHANGE = "user.active.exchange";
    public static final String PROVIDER_REGISTRATION_QUEUE = "core.provider.registered.queue";
    public static final String PROVIDER_REGISTRATION_EXCHANGE = "provider.registered";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(USER_REGISTRATION_QUEUE, true);
    }

    @Bean
    public TopicExchange userRegistrationExchange() {
        return new TopicExchange(USER_REGISTRATION_EXCHANGE);
    }

    @Bean
    public Binding userRegistrationBinding(Queue userRegistrationQueue, TopicExchange userRegistrationExchange) {
        return BindingBuilder.bind(userRegistrationQueue).to(userRegistrationExchange).with("#");
    }

    @Bean
    public Queue userActiveQueue() {
        return new Queue(USER_ACTIVE_QUEUE, true);
    }

    @Bean
    public TopicExchange userActiveExchange() {
        return new TopicExchange(USER_ACTIVE_EXCHANGE);
    }

    @Bean
    public Binding userActiveBinding(Queue userActiveQueue, TopicExchange userActiveExchange) {
        return BindingBuilder.bind(userActiveQueue).to(userActiveExchange).with("#");
    }

    @Bean
    public Queue providerRegistrationQueue() {
        return new Queue(PROVIDER_REGISTRATION_QUEUE, true);
    }

    @Bean
    public TopicExchange providerRegistrationExchange() {
        return new TopicExchange(PROVIDER_REGISTRATION_EXCHANGE);
    }

    @Bean
    public Binding providerRegistrationBinding(Queue providerRegistrationQueue, TopicExchange providerRegistrationExchange) {
        return BindingBuilder.bind(providerRegistrationQueue).to(providerRegistrationExchange).with("#");
    }
}

