package com.gmy.AccessCTLManagementSys.module;

import com.gmy.AccessCTLManagementSys.lib.NetSDKLib;
import com.gmy.AccessCTLManagementSys.lib.ToolKits;

/**
 * @authon GMY
 * @create 2020-10-16 15:07
 */
public class AlarmListenModule {

    private static boolean bListening = false;


    /**
     * \if ENGLISH_LANG
     * start alarm listen
     * \else
     * 向设备订阅报警
     * \endif
     */
    public static boolean startListen(NetSDKLib.fMessCallBack cbMessage) {

        if (bListening) {
			System.out.println("Had Subscribe Alarm.");
            return true;
        }

        LoginModule.netsdk.CLIENT_SetDVRMessCallBack(cbMessage, null); // set alarm listen callback

        if (!LoginModule.netsdk.CLIENT_StartListenEx(LoginModule.m_hLoginHandle)) {
            System.err.printf("CLIENT_StartListenEx Failed!" + ToolKits.getErrorCodePrint());
            return false;
        } else {
            System.out.println("CLIENT_StartListenEx success.");
        }

        bListening = true;
        return true;
    }

    /**
     * \if ENGLISH_LANG
     * stop alarm listen
     * \else
     * 停止订阅报警
     * \endif
     */
    public static boolean stopListen() {

        if (!bListening) {
            return true;
        }

        if (!LoginModule.netsdk.CLIENT_StopListen(LoginModule.m_hLoginHandle)) {
            System.err.printf("CLIENT_StopListen Failed!" + ToolKits.getErrorCodePrint());
            return false;
        } else {
            System.out.println("CLIENT_StopListen success.");
        }

        bListening = false;
        return true;
    }

}
