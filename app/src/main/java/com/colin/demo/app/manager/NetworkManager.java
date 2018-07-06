package com.colin.demo.app.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

public final class NetworkManager {
    private NetworkManager() {
    }


    public static class Holder {
        static NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManager.Holder.instance;
    }

    /**
     * 得到网络管理类
     *
     * @param context
     * @return
     */
    public ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 得到网络信息类
     *
     * @param context
     * @return
     */
    public NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        return null == connectivityManager ? null : connectivityManager.getActiveNetworkInfo();
    }

    public Network getNetwork(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ConnectivityManager connectivityManager = getConnectivityManager(context);
            return null == connectivityManager ? null : connectivityManager.getActiveNetwork();
        }
        return null;
    }

    /**
     * 检测网络是否连接
     */
    public boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (null == connectivityManager) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 精确的网络状态
     */
    public NetworkInfo.DetailedState getDetailedState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return null == networkInfo ? null : networkInfo.getDetailedState();
    }

    /**
     * 粗略的网络状态
     */
    public NetworkInfo.State getState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return null == networkInfo ? null : networkInfo.getState();
    }
}
