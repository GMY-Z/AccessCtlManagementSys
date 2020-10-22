package com.gmy.AccessCTLManagementSys.utils;

import com.gmy.AccessCTLManagementSys.lib.NetSDKLib;
import com.gmy.AccessCTLManagementSys.lib.ToolKits;
import com.gmy.AccessCTLManagementSys.module.LoginModule;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @authon GMY
 * @create 2020-10-21 18:48
 */
public class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack {
    private BufferedImage gateBufferedImage = null;
    private String sSerialNumber;

    public void setsSerialNumber(String sSerialNumber) {
        this.sSerialNumber = sSerialNumber;
    }

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

        }

        return 0;
    }
}
