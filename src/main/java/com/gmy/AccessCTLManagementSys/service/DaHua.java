package com.gmy.AccessCTLManagementSys.service;

import com.gmy.AccessCTLManagementSys.module.GateListenModule;
import org.springframework.stereotype.Service;

/**
 * @authon GMY
 * @create 2020-10-21 19:33
 */
@Service
public class DaHua {

    public DaHua(){
        GateListenModule gateListenModule = new GateListenModule("10.12.44.21", 37777, "admin", "12345678a"),
                gateListenModule1 = new GateListenModule("10.12.44.21", 37777, "admin", "12345678a");

        System.out.println("dahua");
    }
}
