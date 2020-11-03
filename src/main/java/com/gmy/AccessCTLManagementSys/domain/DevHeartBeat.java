package com.gmy.AccessCTLManagementSys.domain;

import java.sql.Timestamp;

/**
 * @authon GMY
 * @create 2020-11-02 20:59
 */
public class DevHeartBeat {
    int id;
    String deviceId;
    int status;
    String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
