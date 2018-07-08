package com.colin.demo.app.manager;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 管理无线网工具类
 */
public class WiFiManager {

    private WiFiManager() {
    }

    public static class Holder {
        static WiFiManager instance = new WiFiManager();
    }

    public static WiFiManager getInstance() {
        return WiFiManager.Holder.instance;
    }

    public WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        return null == wifiManager ? null : wifiManager.getConnectionInfo();
    }

    /**
     * WiFi是否打开
     *
     * @param context
     * @return
     */
    public boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        return null != wifiManager && wifiManager.isWifiEnabled();
    }

    /**
     * WIFI_STATE_DISABLING WIFI网卡正在关闭
     * WIFI_STATE_DISABLED  WIFI网卡不可用
     * WIFI_STATE_ENABLING  WIFI网卡正在打开
     * WIFI_STATE_ENABLED   WIFI网卡可用
     * WIFI_STATE_UNKNOWN   WIFI网卡状态不可知
     *
     * @param context
     * @return
     */
    public int getWifiState(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        return null == wifiManager ? WifiManager.WIFI_STATE_UNKNOWN : wifiManager.getWifiState();
    }

    /**
     * @param context
     * @return
     */
    public String getWifiStateString(Context context) {
        return getWifiState(getWifiState(context));
    }

    /**
     * @param state
     * @return
     */
    public String getWifiState(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_DISABLING:
                return "WIFI网卡正在关闭";
            case WifiManager.WIFI_STATE_DISABLED:
                return "WIFI网卡不可用";
            case WifiManager.WIFI_STATE_ENABLING:
                return "WIFI网卡正在打开";
            case WifiManager.WIFI_STATE_ENABLED:
                return "WIFI网卡可用";
            case WifiManager.WIFI_STATE_UNKNOWN:
                return "WIFI网卡状态不可知";
            default:
                return "WIFI网卡状态不可知";
        }
    }


}
