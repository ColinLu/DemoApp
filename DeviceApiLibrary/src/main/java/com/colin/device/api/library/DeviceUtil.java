package com.colin.device.api.library;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

public class DeviceUtil {

    /**
     * android  deviceID 与 imei
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceID(Context context) {
        String deviceID = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (!PermissionUtil.getInstance().checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return getDeviceID();
            }else {
                deviceID = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            deviceID = getDeviceID();
        }
        return TextUtils.isEmpty(deviceID) ? getDeviceID() : deviceID;
    }


    /**
     * 生成一个 deviceID
     *
     * @return
     */
    public static String getDeviceID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // 随便一个初始化
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
