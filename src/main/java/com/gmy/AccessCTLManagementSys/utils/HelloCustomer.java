package com.gmy.AccessCTLManagementSys.utils;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @authon GMY
 * @create 2020-10-22 15:04
 */
//生成hello队列，不持久化，自动删除，非独占
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello", durable = "false", autoDelete = "true"))
public class HelloCustomer {

    //收到消息时的回调方法
    @RabbitHandler
    public void receive1(String message){
        System.out.println("message = " + message);
    }

}
