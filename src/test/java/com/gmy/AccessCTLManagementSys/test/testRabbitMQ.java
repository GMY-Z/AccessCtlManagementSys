package com.gmy.AccessCTLManagementSys.test;

import com.gmy.AccessCTLManagementSys.AccessCtlManagementSysApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @authon GMY
 * @create 2020-10-22 14:57
 */
@SpringBootTest(classes = AccessCtlManagementSysApplication.class)
@RunWith(SpringRunner.class)
public class testRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

      @Test
    public void testHello(){
        rabbitTemplate.convertAndSend("hello", "helloWorld");
    }

}
