package com.colin.device.api.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Map;

public class DeviceApi {


    private static class Holder {
        public final static DeviceApi instance = new DeviceApi();
    }

    public static DeviceApi getInstance() {
        return Holder.instance;
    }


    public Map<String, String> getBuildList(Context context) {
        return BuildUtil.getBuildList(context);
    }

    @SuppressLint("MissingPermission")
    public String getDeviceID(Context context) {
        return DeviceUtil.getDeviceID(context);
    }

    public void showToast(@NonNull Context context) {
        Toast.makeText(context, "DeviceApi-->>", Toast.LENGTH_SHORT).show();
    }

}
