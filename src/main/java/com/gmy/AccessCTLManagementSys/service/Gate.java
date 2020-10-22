package com.gmy.AccessCTLManagementSys.service;

import com.gmy.AccessCTLManagementSys.lib.NetSDKLib;
import com.gmy.AccessCTLManagementSys.lib.ToolKits;
import com.gmy.AccessCTLManagementSys.module.LoginModule;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import static com.gmy.AccessCTLManagementSys.lib.ToolKits.getErrorCodePrint;

/**
 * @authon GMY
 * @create 2020-10-21 09:31
 */
//@Service
public class Gate {

    private AnalyzerDataCB analyzerCallback = new AnalyzerDataCB();
    // 设备断线通知回调
    private static DisConnect disConnect = new DisConnect();

    // 网络连接恢复
    private static HaveReConnect haveReConnect = new HaveReConnect();

    // 订阅句柄
    public static NetSDKLib.LLong m_hAttachHandle = new NetSDKLib.LLong(0);

    private Vector<String> chnList = new Vector<String>();

    static String ip = "10.12.44.21";
    static int port = 37777;
    static String user = "admin";
    static String password = "12345678a";
    static String sSerialNumber;

    public Gate() {
        LoginModule.init(disConnect, haveReConnect);
        if (login()) {
            setOnClickListener();
        }
    }

    // 监听
    private void setOnClickListener() {
        // 订阅
        m_hAttachHandle = realLoadPic(0, analyzerCallback);

        if (m_hAttachHandle.longValue() != 0) {

        } else {

        }
    }
    public static NetSDKLib.LLong realLoadPic(int ChannelId, NetSDKLib.fAnalyzerDataCallBack m_AnalyzerDataCB) {
        /**
         * 说明：
         * 	通道数可以在有登录是返回的信息 m_stDeviceInfo.byChanNum 获取
         *  下列仅订阅了0通道的智能事件.
         */
        int bNeedPicture = 1; // 是否需要图片

        NetSDKLib.LLong m_hAttachHandle = LoginModule.netsdk.CLIENT_RealLoadPictureEx(LoginModule.m_hLoginHandle, ChannelId,  NetSDKLib.EVENT_IVS_ALL,
                bNeedPicture , m_AnalyzerDataCB , null , null);
        if( m_hAttachHandle.longValue() != 0  ) {
            System.out.println("CLIENT_RealLoadPictureEx Success  ChannelId : \n" + ChannelId);
        } else {
            System.err.println("CLIENT_RealLoadPictureEx Failed!" + ToolKits.getErrorCodePrint());
            return null;
        }

        return m_hAttachHandle;
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

    public boolean login() {
        if (LoginModule.login(ip, port, user, password)) {
//            for(int i = 1; i < LoginModule.m_stDeviceInfo.byChanNum + 1; i++) {
//                chnList.add(Res.string().getChannel() + " " + String.valueOf(i));
//            }
            System.out.println("登录成功");
        } else {
            System.out.println(getErrorCodePrint());
            return false;
        }
        return true;
    }



    private class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack {
        private BufferedImage gateBufferedImage = null;

        public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType,
                          Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize,
                          Pointer dwUser, int nSequence, Pointer reserved) {
            if (lAnalyzerHandle.longValue() == 0 || pAlarmInfo == null) {
                return -1;
            }

            File path = new File("./GateSnapPicture/");
            if (!path.exists()) {
                path.mkdir();
            }

            System.out.println(dwAlarmType);

            ///< 门禁事件
            if (dwAlarmType == NetSDKLib.EVENT_IVS_ACCESS_CTL) {
                NetSDKLib.DEV_EVENT_ACCESS_CTL_INFO msg = new NetSDKLib.DEV_EVENT_ACCESS_CTL_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                System.out.println("channelid" + msg.nChannelID);
                System.out.println("userid" + new String(msg.szUserID).trim());
                System.out.println("url" + new String(msg.szSnapURL).trim());
                String cardName = null;
                try {
                    cardName = new String(msg.szCardName, "GBK").trim();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("开门状态" + msg.bStatus);
                System.out.println("开门方式" + msg.emOpenMethod);
                System.out.println("卡号" + new String(msg.szCardNo).trim());
                System.out.println("用户姓名" + cardName);
                sSerialNumber = new String(LoginModule.m_stDeviceInfo.sSerialNumber).trim();
                System.out.println("序列号" + sSerialNumber);
                // 保存图片，获取图片缓存
                String snapPicPath = path + "\\" + System.currentTimeMillis() + "GateSnapPicture.jpg";  // 保存图片地址
                byte[] buffer = pBuffer.getByteArray(0, dwBufSize);
                ByteArrayInputStream byteArrInputGlobal = new ByteArrayInputStream(buffer);

                try {
                    gateBufferedImage = ImageIO.read(byteArrInputGlobal);
                    if (gateBufferedImage != null) {
                        ImageIO.write(gateBufferedImage, "jpg", new File(snapPicPath));
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

                // 图片以及门禁信息界面显示
//                EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
//                if (eventQueue != null) {
//                    eventQueue.postEvent( new AccessEvent(target,
//                            gateBufferedImage,
//                            msg));
//                }
            }

            return 0;
        }
    }
}
