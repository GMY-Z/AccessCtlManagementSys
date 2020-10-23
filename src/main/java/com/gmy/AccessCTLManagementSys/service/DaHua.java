package com.gmy.AccessCTLManagementSys.service;

import com.gmy.AccessCTLManagementSys.domain.EventAccessInfo;
import com.gmy.AccessCTLManagementSys.module.GateListenModule;
import com.gmy.AccessCTLManagementSys.mq.RabbitMQProducer;
import com.gmy.AccessCTLManagementSys.utils.SpringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @authon GMY
 * @create 2020-10-21 19:33
 */
@Service
public class DaHua {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    private List<GateListenModule> gateListenModuleList = new ArrayList<>();

    public DaHua(){
        GateListenModule gateListenModule = new GateListenModule("10.12.44.21", 37777, "admin", "12345678a");
//        System.out.println(gateListenModule.m_hAttachHandle);
        gateListenModuleList.add(gateListenModule);
        EventAccessInfo eventAccessInfo = new EventAccessInfo();
        eventAccessInfo.setDeviceId("ss");
        Map<String, Object> map = new HashMap<>();
        map.put("1","df");
        map.put("dd", eventAccessInfo);
        //生产者直接发送对象
//        rabbitMQProducer.send();
//        rabbitMQProducer.sendDataToQueue("exchange.direct","dahua",eventAccessInfo);

        System.out.println("dahua");
    }
}
