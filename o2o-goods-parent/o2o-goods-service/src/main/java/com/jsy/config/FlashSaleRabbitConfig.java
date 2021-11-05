package com.jsy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FlashSaleRabbitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlashSaleRabbitConfig.class);
    public static final String DIRECT_QUEUE ="DIRECT_QUEUE";
    public static final String DIRECT_EXCHANGE ="DIRECT_EXCHANGE";
    public static final String DIRECT_KEY ="DIRECT_ROUTING_KEY";


    @Resource
    private RabbitTemplate rabbitTemplate;


    @Bean
    public AmqpTemplate amqpTemplate() {


        /**
         * 定义消息转换实例 ，转化成 JSON传输
         *
         * @return Jackson2JsonMessageConverter
         */
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //rabbitTemplate.setEncoding("UTF-8");
        // 消息发送失败,返回到队列中yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        /**
         * 消息发送到交换器Exchange后触发回调。
         * 使用该功能需要开启确认，spring-boot中配置如下：
         * spring.rabbitmq.publisher-confirms = true
         */
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean b, String s)->{
                if (b) {
                    LOGGER.info("消息已确认 cause:{}",correlationData.getId());
                } else {
                    LOGGER.info("消息未确认 cause:{}", s);
                }
        });
        /**
         * 通过实现ReturnCallback接口，
         * 如果消息从交换器发送到对应队列失败时触发
         * 比如根据发送消息时指定的routingKey找不到队列时会触发
         * 使用该功能需要开启确认，spring-boot中配置如下：
         * spring.rabbitmq.publisher-returns = true
         */
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey)-> {
                LOGGER.error("消息被退回:{}", message);
                LOGGER.error("消息使用的交换机:{}", exchange);
                LOGGER.error("消息使用的路由键:{}", routingKey);
                LOGGER.error("描述:{}",replyText);
        });
        return rabbitTemplate;
    }
    /**
     * 声明Direct交换机 支持持久化.
     * @return the exchange
     */
    @Bean("directExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).build();
    }
    /**
     * 声明一个队列 支持持久化.
     *
     * @return the queue
     */
    @Bean("directQueue")
    public Queue directQueue() {
        return QueueBuilder.durable(DIRECT_QUEUE).build();
    }
    /**
     * 通过绑定键 将指定队列绑定到一个指定的交换机 .
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding directBinding(@Qualifier("directQueue") Queue queue,
                                 @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DIRECT_KEY).noargs();
    }
}