package org.wj.letsrock.infrastructure.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
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

    private static final String CANAL_EXCHANGE = "canal.exchange";
    public static final String CANAL_QUEUE = "canal.queue";
    public static final String CANAL_ROUTING_KEY = "canal.routing.key";


    @Bean
    public Queue operateQueue() {
        return new Queue(OPERATE_QUEUE, true);
    }

    @Bean
    public DirectExchange operateExchange() {
        return new DirectExchange(OPERATE_EXCHANGE, true, false);
    }

    @Bean
    public Binding operateBinding() {
        return BindingBuilder.bind(operateQueue()).to(operateExchange()).with(OPERATE_ROUTING_KEY);
    }
    @Bean
    public DirectExchange canalExchange() {
        return new DirectExchange(CANAL_EXCHANGE, true, false); // 持久化直连交换机
    }

    @Bean
    public Queue canalQueue() {
        return new Queue(CANAL_QUEUE, true); // 持久化队列
    }

    @Bean
    public Binding canalBinding() {
        return BindingBuilder.bind(canalQueue())
                .to(canalExchange())
                .with(CANAL_ROUTING_KEY); // 路由键
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
