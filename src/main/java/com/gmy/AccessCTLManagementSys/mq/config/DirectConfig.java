package com.gmy.AccessCTLManagementSys.mq.config;

import com.gmy.AccessCTLManagementSys.mq.RabbitMQProducer;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @authon GMY
 * @create 2020-10-23 17:01
 */
@Configuration
public class DirectConfig {

    //队列名称
    private String Direct_DaHua = "hello";

    //交换机名称
    private String EXCHANGE_NAME = "exchange.direct";

    //1.定义队列
    @Bean
    public Queue directDaHuaQueue() {
        return new Queue(Direct_DaHua);
    }

    //2.定义交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean(name = "RabbitMQProducer")
    public RabbitMQProducer send(){
        return new RabbitMQProducer();
    }
}

