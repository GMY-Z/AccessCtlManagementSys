package com.gmy.AccessCTLManagementSys.mq;

import com.gmy.AccessCTLManagementSys.domain.EventAccessInfo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @authon GMY
 * @create 2020-10-22 20:30
 */
@Component
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(EventAccessInfo eventAccessInfo) {
//        String context = "Hello " + "Rabbit MQ！";
        System.out.println("发送MQ消息 : " + eventAccessInfo);
        this.rabbitTemplate.convertAndSend(queue.getName(), eventAccessInfo);
    }
}
