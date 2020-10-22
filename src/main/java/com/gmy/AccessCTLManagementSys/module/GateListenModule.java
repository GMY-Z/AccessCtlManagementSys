package com.gmy.AccessCTLManagementSys.module;

import com.gmy.AccessCTLManagementSys.lib.NetSDKLib;
import com.gmy.AccessCTLManagementSys.lib.ToolKits;
import com.gmy.AccessCTLManagementSys.utils.AnalyzerDataCB;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.File;

/**
 * @authon GMY
 * @create 2020-10-21 18:44
 */
@Service
public class GateListenModule {

    String ip = "10.12.44.21";
    int port = 37777;
    String user = "admin";
    String password = "12345678a";

    //报警回调函数实现
    AnalyzerDataCB analyzerDataCB = new AnalyzerDataCB();

    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;

    // 设备断线通知回调
    private static DisConnect disConnect = new DisConnect();

    // 网络连接恢复
    private static HaveReConnect haveReConnect = new HaveReConnect();

    // 设备信息
    public NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();

    // 登陆句柄
    public NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);

    // 订阅句柄
    public NetSDKLib.LLong m_hAttachHandle = new NetSDKLib.LLong(0);
    private boolean bInit    = false;
    private boolean bLogopen = false;


    public GateListenModule() {
//        this.ip = ip;
//        this.port = port;
//        this.user = user;
//        this.password = password;
        //调用sdk
        System.out.println("sdk");
        sdk();
    }

    private void sdk() {
        this.init(disConnect, haveReConnect);
        if (this.Login()) {
            System.out.println("setonclicklisten");
            this.setOnClickListener();
        }
    }

    private boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
        System.out.println("init");
        bInit = netsdk.CLIENT_Init(disConnect, null);
        if(!bInit) {
            System.out.println("Initialize SDK failed");
            return false;
        }

        //打开日志，可选
        NetSDKLib.LOG_SET_PRINT_INFO setLog = new NetSDKLib.LOG_SET_PRINT_INFO();
        File path = new File("./sdklog/");
        if (!path.exists()) {
            path.mkdir();
        }
        String logPath = path.getAbsoluteFile().getParent() + "\\sdklog\\" + ToolKits.getDate() + ".log";
        setLog.nPrintStrategy = 0;
        setLog.bSetFilePath = 1;
        System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0, logPath.getBytes().length);
        System.out.println(logPath);
        setLog.bSetPrintStrategy = 1;
        bLogopen = netsdk.CLIENT_LogOpen(setLog);
        if(!bLogopen ) {
            System.err.println("Failed to open NetSDK log");
        }

        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);

        //设置登录超时时间和尝试次数，可选
        int waitTime = 5000; //登录请求响应超时时间设置为5S
        int tryTimes = 1;    //登录时尝试建立链接1次
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);

        // // GDPR使能全局开关
        netsdk.CLIENT_SetGDPREnable(true);

        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        netParam.nConnectTime = 10000;      // 登录时尝试建立链接的超时时间
        netParam.nGetConnInfoTime = 3000;   // 设置子连接的超时时间
        netsdk.CLIENT_SetNetworkParam(netParam);

        return true;
    }

    private Boolean Login() {
        System.out.println("login");
        //入参
        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstInParam.nPort = port;
        pstInParam.szIP = ip.getBytes();
        pstInParam.szPassword = password.getBytes();
        pstInParam.szUserName = user.getBytes();
        //出参
        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
        m_hLoginHandle = netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
        if (m_hLoginHandle.longValue() == 0) {
            System.err.printf("Login Device[%s] Port[%d]Failed. %s\n", ip, port, ToolKits.getErrorCodePrint());
        } else {
            System.out.println("Login Success [ " + ip + " ]");
//            System.out.println(new String(m_stDeviceInfo.sSerialNumber).trim());
        }

        return m_hLoginHandle.longValue() == 0 ? false : true;
    }

    private void setOnClickListener() {
        if(analyzerDataCB == null){
            System.out.println("new回调函数");
            analyzerDataCB = new AnalyzerDataCB();
        }
        analyzerDataCB.setsSerialNumber(new String(m_stDeviceInfo.sSerialNumber).trim());
        /**
         * 说明：
         * 	通道数可以在有登录是返回的信息 m_stDeviceInfo.byChanNum 获取
         *  下列仅订阅了0通道的智能事件.
         */
        int bNeedPicture = 1; // 是否需要图片
        if (0 != m_hAttachHandle.longValue()) {
            stopRealLoadPic(m_hAttachHandle);
        }
        m_hAttachHandle = netsdk.CLIENT_RealLoadPictureEx(m_hLoginHandle, 0,  NetSDKLib.EVENT_IVS_ALL,
                bNeedPicture , analyzerDataCB , null , null);
        if( m_hAttachHandle.longValue() != 0  ) {
            System.out.println("CLIENT_RealLoadPictureEx Success  ChannelId : \n" + 0);
        } else {
            System.err.println("CLIENT_RealLoadPictureEx Failed!" + ToolKits.getErrorCodePrint());
        }

    }

    /**
     * 停止上传智能分析数据－图片
     */
    public void stopRealLoadPic(NetSDKLib.LLong m_hAttachHandle) {
        if (0 != m_hAttachHandle.longValue()) {
            netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
            System.out.println("Stop detach IVS event");
            m_hAttachHandle.setValue(0);
        }
    }

    // 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private static class DisConnect implements NetSDKLib.fDisConnect {
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

    // 网络连接恢复，设备重连成功回调
    // 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
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
}
