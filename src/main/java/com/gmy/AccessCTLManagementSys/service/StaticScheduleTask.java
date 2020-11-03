package com.gmy.AccessCTLManagementSys.service;

/**
 * @authon GMY
 * @create 2020-11-03 16:55
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.gmy.AccessCTLManagementSys.utils.ForDevTime.sendHeartBeat;
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class StaticScheduleTask {
    /**\
     *  每隔5min触发一次
     */
    @Scheduled(cron = "0 */5 * * * ?" )
    public void sendProgramHeartBeat(){
        sendHeartBeat("40",1);
    }
}