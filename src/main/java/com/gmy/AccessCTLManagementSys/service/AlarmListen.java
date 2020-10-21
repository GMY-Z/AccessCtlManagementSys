package com.gmy.AccessCTLManagementSys.service;

import com.gmy.AccessCTLManagementSys.lib.NetSDKLib;
import com.gmy.AccessCTLManagementSys.lib.ToolKits;
import com.gmy.AccessCTLManagementSys.module.AlarmListenModule;
import com.gmy.AccessCTLManagementSys.module.LoginModule;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Service;

import javax.swing.*;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Vector;

import static com.gmy.AccessCTLManagementSys.lib.ToolKits.getErrorCodePrint;

/**
 * @authon GMY
 * @create 2020-10-16 14:39
 */

//@Service
public class AlarmListen {

    // device disconnect callback instance
    private DisConnect disConnect = new DisConnect();

    // device reconnect callback instance
    private static HaveReConnect haveReConnect = new HaveReConnect();

    static String ip = "10.12.44.21";
    static int port = 37777;
    static String user = "admin";
    static String password = "12345678a";

    // alarm event info list
    Vector<AlarmEventInfo> data = new Vector<AlarmEventInfo>();

    public AlarmListen() {
        LoginModule.init(disConnect, haveReConnect);
        if (login()) {
            //布防
            if (AlarmListenModule.startListen(cbMessage)) {
                //
                System.out.println("布防成功");
            }
        }

    }

    /////////////////function///////////////////
    // device disconnect callback class
    // set it's instance by call CLIENT_Init, when device disconnect sdk will call it.
    private class DisConnect implements NetSDKLib.fDisConnect {
        public DisConnect() {
        }

        @Override
        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("disConnect");
                }
            });
        }
    }

    // device reconnect(success) callback class
    // set it's instance by call CLIENT_SetAutoReconnect, when device reconnect success sdk will call it.
    private static class HaveReConnect implements NetSDKLib.fHaveReConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("reConnect");
                }
            });
        }
    }

    public boolean login() {
        if (LoginModule.login(ip, port, user, password)) {
            System.out.println("登录成功");
        } else {
            System.out.println(getErrorCodePrint());
            return false;
        }
        return true;
    }

    public void logout() {
        AlarmListenModule.stopListen();
        LoginModule.logout();
    }


    private fAlarmDataCB cbMessage = new fAlarmDataCB();

    // alarm listen data callback
    private class fAlarmDataCB implements NetSDKLib.fMessCallBack {
        @Override
        public boolean invoke(int lCommand, NetSDKLib.LLong lLoginID,
                              Pointer pStuEvent, int dwBufLen, String strDeviceIP,
                              NativeLong nDevicePort, Pointer dwUser) {
            System.out.println("fMessCallBack回调的lCommand：" + Integer.toHexString(lCommand));
            switch (lCommand) {
                case NetSDKLib.NET_ALARM_ACCESS_CTL_STATUS: {
                    System.out.println("门禁状态");
                    NetSDKLib.ALARM_ACCESS_CTL_STATUS_INFO status_info = new NetSDKLib.ALARM_ACCESS_CTL_STATUS_INFO();
//                    System.out.println(status_info);
                    System.out.println("emStatus"+status_info.emStatus);

                    NetSDKLib.ALARM_ACCESS_CTL_EVENT_INFO msg = new
                            NetSDKLib.ALARM_ACCESS_CTL_EVENT_INFO();
                    ToolKits.GetPointerData(pStuEvent, msg);
                    String url = null;
                    try {
                        url = new String(msg.szSnapURL, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    System.out.println(msg.szSnapURL);
                    System.out.println(url);
                    System.out.println("【门禁事件】 " + msg);
                    break;
                }
                case NetSDKLib.NET_ALARM_ACCESS_CTL_EVENT: // 门禁事件
                {
                    NetSDKLib.ALARM_ACCESS_CTL_EVENT_INFO msg = new
                            NetSDKLib.ALARM_ACCESS_CTL_EVENT_INFO();
                    ToolKits.GetPointerData(pStuEvent, msg);
                    System.out.println("【门禁事件】 " + msg);
                    break;
                }
                case NetSDKLib.NET_DISKERROR_ALARM_EX: {
                    byte[] alarm = new byte[dwBufLen];
                    pStuEvent.read(0, alarm, 0, dwBufLen);
                    for (int i = 0; i < dwBufLen; i++) {
                        if (alarm[i] == 1) {
                            AlarmEventInfo alarmEventInfo = new AlarmEventInfo(i, lCommand, AlarmStatus.ALARM_START);
                            if (!data.contains(alarmEventInfo)) {
                                data.add(alarmEventInfo);
                                System.out.println("add" + alarmEventInfo);
                            }
                        } else {
                            AlarmEventInfo alarmEventInfo = new AlarmEventInfo(i, lCommand, AlarmStatus.ALARM_STOP);
                            if (data.remove(alarmEventInfo)) {
                                System.out.println("remove");
                            }
                        }
                    }
                    break;
                }
                default:
                    break;

            }

            return true;
        }

    }

    enum AlarmStatus {
        ALARM_START, ALARM_STOP
    }

    // struct of alarm event
    static class AlarmEventInfo {
        public static long index = 0;
        public long id;
        public int chn;
        public int type;
        public Date date;
        public AlarmStatus status;

        public AlarmEventInfo(int chn, int type, AlarmStatus status) {
            this.chn = chn;
            this.type = type;
            this.status = status;
            this.date = new Date();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AlarmEventInfo showInfo = (AlarmEventInfo) o;
            return chn == showInfo.chn && type == showInfo.type;
        }
    }
}
