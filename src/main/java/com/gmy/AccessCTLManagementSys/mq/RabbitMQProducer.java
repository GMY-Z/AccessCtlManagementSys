package com.gmy.AccessCTLManagementSys.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @authon GMY
 * @create 2020-10-22 20:30
 */
public class RabbitMQProducer {
    @Autowired
    private static RabbitTemplate rabbitTemplate;

    public static void sendDataToQueue(String queueKey, Object object) {
        try {
            rabbitTemplate.convertAndSend(queueKey, object);
            System.out.println("------------消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
