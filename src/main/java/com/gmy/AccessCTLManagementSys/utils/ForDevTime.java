package com.gmy.AccessCTLManagementSys.utils;

import com.gmy.AccessCTLManagementSys.domain.DevHeartBeat;
import net.sf.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @authon GMY
 * @create 2020-11-03 13:55
 */
public class ForDevTime {

    public static String getCurTimestamp(){
        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//        Timestamp time = Timestamp.valueOf(nowTime);//把时间转换
        return nowTime;
    }

    public static void sendHeartBeat(String devId, int status){
        DevHeartBeat devHeartBeat = new DevHeartBeat();
        devHeartBeat.setDeviceId(devId);
        devHeartBeat.setStatus(status);
        devHeartBeat.setTime(getCurTimestamp());
        String url = "http://localhost:8080/SchoolSecurityService/schoolSafetySupervisor/addDevHeartBeat";
        JSONObject json = JSONObject.fromObject(devHeartBeat);
        String dataSource = json.toString();
        AccessingThirdPartyInterface.httppost(url, dataSource);
    }
}
