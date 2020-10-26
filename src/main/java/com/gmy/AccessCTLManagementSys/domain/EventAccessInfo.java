package com.gmy.AccessCTLManagementSys.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @authon GMY
 * @create 2020-10-22 15:49
 */
public class EventAccessInfo implements Serializable {
    private String studentId;
    private String studentName;
    private String time;
    private byte[] imageBuffer;
    private String deviceId;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getImageBuffer() {
        return imageBuffer;
    }

    public void setImageBuffer(byte[] imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "EventAccessInfo{" +
                ", studentId=" + studentId +
                ", time='" + time + '\'' +
                ", imageBuffer=" + Arrays.toString(imageBuffer) +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
