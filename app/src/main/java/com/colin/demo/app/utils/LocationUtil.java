package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 描述：原生系统 定位实现获取经纬度
 * <p>
 * 作者：Colin
 * 时间：2018/5/8
 */
public class LocationUtil {


    public static class Holder {
        static LocationUtil instance = new LocationUtil();
    }

    private LocationUtil() {
    }


    public static LocationUtil getInstance() {
        return Holder.instance;
    }


    public LocationManager mLocationManager;


    @SuppressLint("ServiceCast")
    public LocationManager getLocationManager(Context context) {
        if (null == mLocationManager) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        return mLocationManager;
    }

    /**
     * 得到LocationManager实例后，再结合LocationProvider就能够得到经纬度了，LocationProvider分为两种：
     * 1.GPS_PROVIDER       通过GPS定位，较精确。也比較耗电
     * 2.NETWORK_PROVIDER   通过网络定位。对定位精度度不高或省点情况可考虑使用
     *
     * @param context
     * @return
     */
    public List<LocationProvider> getLocationProvider(Context context) {
        List<LocationProvider> locationProviderList = new ArrayList<>();
        LocationManager locationManager = getLocationManager(context);

        if (null == locationManager) {
            return locationProviderList;
        }
        List<String> providers = locationManager.getProviders(true);
        if (null == providers || providers.size() == 0) {
            return locationProviderList;
        }
        for (String provider : providers) {
            LogUtil.e("provider-->>" + String.valueOf(provider));
            locationProviderList.add(locationManager.getProvider(provider));
        }
        return locationProviderList;
    }

    public boolean isGpsProvider(Context context) {
        LocationManager locationManager = getLocationManager(context);
        List<String> stringList = locationManager.getProviders(true);
        if (null == stringList || stringList.size() == 0) {
            return false;
        }
        return stringList.contains(LocationManager.GPS_PROVIDER);
    }

    /**
     * 是否有可用的定位提供其
     *
     * @param context
     * @return
     */
    public boolean isProvider(Context context) {
        LocationManager locationManager = getLocationManager(context);
        List<String> stringList = locationManager.getProviders(true);
        return stringList.size() > 0;
    }

    /**
     * 原生系统 获取定位信息
     * 1.GPS_PROVIDER       通过GPS定位，较精确。也比較耗电
     * 2.NETWORK_PROVIDER   通过网络定位。对定位精度度不高或省点情况可考虑使用
     *
     * @param context
     * @param locationListener
     * @return
     */
    @SuppressLint("MissingPermission")
    public void getLocationData(Context context, LocationListener locationListener) {
        LocationManager locationManager = getLocationManager(context);
        LocationProvider gpsLocationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (null != gpsLocationProvider && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            return;
        }

        LocationProvider netLocationProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        if (null != netLocationProvider && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            return;
        }

        LogUtil.e("GPS_PROVIDER------" + String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        LogUtil.e("NETWORK_PROVIDER--" + String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
        toLocationView(context);
    }

    private void toLocationView(Context context) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
