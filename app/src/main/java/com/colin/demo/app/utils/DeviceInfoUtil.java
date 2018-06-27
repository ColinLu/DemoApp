package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * 获取手机设备信息
 */

public class DeviceInfoUtil {

    @SuppressLint({"WifiManagerPotentialLeak", "MissingPermission"})
    public static String getWifissid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String wifissid = "";
        if (null != wifiManager && null != wifiManager.getConnectionInfo() && null != wifiManager.getConnectionInfo().getSSID()) {
            wifissid = wifiManager.getConnectionInfo().getSSID();
        }
        return wifissid;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getPhone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = "";
        if (null != telephonyManager && null != telephonyManager.getLine1Number()) {
            phone = telephonyManager.getLine1Number();
        }
        return phone;
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
        }
        return "02:00:00:00:00:00";
    }

    public static String getSIMcarrier(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String SIMcarrier = "读取失败";
        if (null != telephonyManager) {
            String operator = telephonyManager.getSimOperator();
            if (!StringUtil.isEmpty(operator)) {
                if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                    SIMcarrier = "中国移动";
                } else if (operator.equals("46001")) {
                    SIMcarrier = "中国联通";
                } else if (operator.equals("46003")) {
                    SIMcarrier = "中国电信";
                } else {
                    SIMcarrier = operator;
                }
            }
        }
        return SIMcarrier;
    }


    /**
     * 获取CPu的名字
     *
     * @return
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Exception->>" + e.getMessage());
        }
        return "";
    }
}
