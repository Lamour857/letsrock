package org.wj.letsrock.infrastructure.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-13:40
 **/
@Getter
@Configuration
@ConditionalOnProperty(name = "rabbitmq.enable",
        havingValue = "true")
public class RabbitmqConfig {

    @Value("${rabbitmq.enable}")
    private Boolean enabled;
    public static final String OPERATE_QUEUE = "operate.queue";
    public static final String OPERATE_EXCHANGE = "operate.exchange";
    public static final String OPERATE_ROUTING_KEY = "operate.routing.key";

    @Bean
    public Queue operateQueue() {
        return new Queue(OPERATE_QUEUE, true);
    }

    @Bean
    public DirectExchange operateExchange() {
        return new DirectExchange(OPERATE_EXCHANGE, true, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(operateQueue()).to(operateExchange()).with(OPERATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 配置多线程消费者
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setConcurrentConsumers(5);   // 初始消费者数量
        factory.setMaxConcurrentConsumers(10); // 最大消费者数量
        return factory;
    }

}
