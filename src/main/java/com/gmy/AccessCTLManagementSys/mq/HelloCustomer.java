package com.gmy.AccessCTLManagementSys.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmy.AccessCTLManagementSys.domain.EventAccessInfo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * @authon GMY
 * @create 2020-10-22 15:04
 */
//生成hello队列，持久化，非独占
@Component
@RabbitListener(queues = "dahua")
public class HelloCustomer {

    @RabbitHandler
    public void receive2(EventAccessInfo eventAccessInfo){
        System.out.println(eventAccessInfo);
    }
//@RabbitHandler
//public void receive2(String eventAccessInfo){
//    System.out.println(eventAccessInfo);
//}

    //收到消息时的回调方法
//    @RabbitHandler
//    public void receive1(HashMap map){
//        System.out.println("yyy");
//        System.out.println(map.getClass());
//        System.out.println(map);
//    }

}
