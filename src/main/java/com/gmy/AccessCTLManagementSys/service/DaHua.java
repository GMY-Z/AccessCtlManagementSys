package com.gmy.AccessCTLManagementSys.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gmy.AccessCTLManagementSys.domain.EventAccessInfo;
import com.gmy.AccessCTLManagementSys.module.GateListenModule;
import com.gmy.AccessCTLManagementSys.mq.RabbitMQProducer;
import com.gmy.AccessCTLManagementSys.utils.AccessingThirdPartyInterface;
import com.gmy.AccessCTLManagementSys.utils.SpringUtil;
import jdk.nashorn.internal.scripts.JS;
import net.sf.json.JSONObject;
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

    private List<GateListenModule> gateListenModuleList = new ArrayList<>();

    public DaHua(){

        String url = "http://localhost:8080/SchoolSecurityService/schoolSafetySupervisor/getAllFaceRecognitionDevice";
        //使用JSject
        JSONObject json =  new JSONObject();
        json.put("department", 40);
        String dataSource = json.toString();
        String jsonString = AccessingThirdPartyInterface.httppost(url, dataSource);
        System.out.println(jsonString);
        JSONArray jsonArray = JSON.parseArray(jsonString);
        for (Object obj : jsonArray) {
            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) obj;
//            System.out.println(jsonObject.getString("deviceIP")+":"+jsonObject.getString("deviceUserName"));
            GateListenModule gateListenModule = new GateListenModule(jsonObject.getString("deviceIP"), 37777, jsonObject.getString("deviceUserName"), jsonObject.getString("devicePassword"));
            gateListenModuleList.add(gateListenModule);
        }

//        EventAccessInfo eventAccessInfo = new EventAccessInfo();
//        eventAccessInfo.setDeviceId("ss");
//        Map<String, Object> map = new HashMap<>();
//        map.put("1","df");
//        map.put("dd", eventAccessInfo);
        //生产者直接发送对象
//        rabbitMQProducer.send();
//        rabbitMQProducer.sendDataToQueue("exchange.direct","dahua",eventAccessInfo);
        System.out.println("dahua设备启动");
    }
}
