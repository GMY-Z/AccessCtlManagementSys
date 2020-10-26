package com.gmy.AccessCTLManagementSys;


import com.gmy.AccessCTLManagementSys.mq.RabbitMQProducer;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.MessageHeader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @authon GMY
 * @create 2020-10-22 21:18
 */
@SpringBootTest(classes = AccessCtlManagementSysApplication.class)
@RunWith(SpringRunner.class)
public class testMq {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

//    @Test
//    public void test() throws IOException {
//        EventAccessInfo eventAccessInfo = new EventAccessInfo();
//        eventAccessInfo.setDeviceId("ss");
//        Map<String, Object> map = new HashMap<>();
//        map.put("1","df");
//        map.put("dd", eventAccessInfo);
//        //生产者直接发送对象
//        rabbitTemplate.convertAndSend("exchange.direct","hello",map);
//    }
//
//
//    @Test
//    public void test1() {
//        EventAccessInfo eventAccessInfo = new EventAccessInfo();
//        eventAccessInfo.setDeviceId("ss");
//        Map<String, Object> map = new HashMap<>();
//        map.put("1","df");
//        map.put("dd", eventAccessInfo);
//        //生产者直接发送对象
//        rabbitTemplate.convertAndSend("exchange.direct","dahua",eventAccessInfo);
//    }

    @Test
    public void test2(){
//        rabbitTemplate.convertAndSend("dahua","sd");
//        rabbitMQProducer.send();
    }

}
