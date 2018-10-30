package com.colin.device.api.library;

import android.content.Context;
import android.os.Build;

import com.colin.device.api.library.data.DeviceAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * Build信息
 */
public class BuildUtil {

    public static Map<String, String> getBuildList(Context context) {
        Map<String, String> buildMap = new HashMap<>();
        buildMap.put(DeviceAlias.DEVICE_BUILD_BOARD, Build.BOARD);
        buildMap.put(DeviceAlias.DEVICE_BUILD_VERSION, String.valueOf(Build.VERSION.SDK_INT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buildMap.put(DeviceAlias.DEVICE_BUILD_BASE_OS, Build.VERSION.BASE_OS);
        }
        return buildMap;
    }


    public static int getBuildVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

}
