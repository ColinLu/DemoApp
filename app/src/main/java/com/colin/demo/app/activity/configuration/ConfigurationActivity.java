package com.colin.demo.app.activity.configuration;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.dialog.DialogTips;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.PermissionUtil;

import java.util.Arrays;

public class ConfigurationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogTips(ConfigurationActivity.this).setMessage(R.string.large_text).show();
            }
        });
    }

    @Override
    protected void initAsync() {

    }

    /**
     * 定位  权限
     *
     * @param view
     */

    public void location(View view) {
        if (!PermissionUtil.getInstance().checkPermission(this, Arrays.asList(PermissionUtil.PERMISSIONS_LOCATION), PermissionUtil.REQUEST_CODE_LOCATION)) {
            return;
        }
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (null == mLocationManager) {
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            LogUtil.e("GPS_PROVIDER-->>空");
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location == null) {
            LogUtil.e("NETWORK_PROVIDER-->>空");
            location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (null != location) {
            new DialogTips(ConfigurationActivity.this).setMessage(getLocationMessage(location)).show();
        }
        LogUtil.e("PASSIVE_PROVIDER-->>空");
    }

    private CharSequence getLocationMessage(Location location) {
        if (null == location) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return "getAccuracy:" + location.getAccuracy() + "\r\n"
                    + "getAltitude:" + location.getAltitude() + "\r\n"
                    + "getBearing:" + location.getBearing() + "\r\n"
                    + "getElapsedRealtimeNanos:" + String.valueOf(location.getElapsedRealtimeNanos()) + "\r\n"
                    + "getLatitude:" + location.getLatitude() + "\r\n"
                    + "getLongitude:" + location.getLongitude() + "\r\n"
                    + "getProvider:" + location.getProvider() + "\r\n"
                    + "getSpeed:" + location.getSpeed() + "\r\n"
                    + "getTime:" + location.getTime() + "\r\n";
        }
        return "getAccuracy:" + location.getAccuracy() + "\r\n"
                + "getAltitude:" + location.getAltitude() + "\r\n"
                + "getBearing:" + location.getBearing() + "\r\n"
                + "getLatitude:" + location.getLatitude() + "\r\n"
                + "getLongitude:" + location.getLongitude() + "\r\n"
                + "getProvider:" + location.getProvider() + "\r\n"
                + "getSpeed:" + location.getSpeed() + "\r\n"
                + "getTime:" + location.getTime() + "\r\n";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionUtil.getInstance().onRequestPermissionsResult(PermissionUtil.REQUEST_CODE_LOCATION, grantResults)) {
            location(null);
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
