package com.colin.demo.app.activity.configuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.dialog.DialogTips;
import com.colin.demo.app.utils.AppUtil;
import com.colin.demo.app.utils.CpuUtil;
import com.colin.demo.app.utils.ImageLoader;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.PermissionUtil;
import com.colin.demo.app.utils.StringUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConfigurationActivity extends BaseActivity {
    private TextView text_configuration_detail;
    private ImageView image_top_bg;
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppUtil.fullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }

    @Override
    protected void initView() {
        this.text_configuration_detail = this.findViewById(R.id.text_configuration_detail);
        this.image_top_bg = this.findViewById(R.id.image_top_bg);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);
        ImageLoader.getInstance().loadImage(image_top_bg, Constants.IMAGE_URL[new Random().nextInt(10)]);
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

    public void system(View view) {
        Map<String, String> map = AppUtil.getBuildInformation();
        if (null == map || map.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(":\t").append(stringStringEntry.getValue()).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString().trim());
    }

    public void cpu(View view) {
        List<String> cpuInfo = CpuUtil.getCpuInfo();
        if (null == cpuInfo || cpuInfo.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : cpuInfo) {
            stringBuilder.append(value).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString().trim());
    }

    public void network(View view) {
        Map<String, String> map = AppUtil.getNetworkInfoInformation();
        if (null == map || map.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(":\t").append(stringStringEntry.getValue()).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString().trim());
    }


    /**
     * 获取WiFi信息
     *
     * @param view
     */
    public void wifi(View view) {
        Map<String, String> map = AppUtil.getWifiInfoInformation();
        if (null == map || map.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(":\t").append(stringStringEntry.getValue()).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString().trim());
    }

    public void phoneMessage(View view) {
        Map<String, String> map = AppUtil.getTelephonyManagerInformation();
        if (null == map || map.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(":\t").append(stringStringEntry.getValue()).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString());

    }


    /**
     * 定位  权限
     *
     * @param view
     */

    public void location(View view) {
        if (!AppUtil.isOpenGps(this)) {
            ToastUtil.showToast("请打开定位功能");
            return;
        }
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
            showMessage(getLocationMessage(location));
        }
        LogUtil.e("PASSIVE_PROVIDER-->>空");
    }

    public void storage(View view) {
        List<String> memInfo = AppUtil.getMemInfo();
        if (null == memInfo || memInfo.size() == 0) {
            ToastUtil.showToast("获取数据失败");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : memInfo) {
            stringBuilder.append(value).append('\n');
        }
        stringBuilder.append("--------------------").append('\n');
        showMessage(stringBuilder.toString().trim());
    }

    public void ipAddress(View view) {
    }

    public String getSimState(int state) {
        switch (state) { //getSimState()取得sim的状态 有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT:
                return "无卡";
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return "未知状态";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return "需要NetworkPIN解锁";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return "需要PIN解锁";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return "需要PUK解锁";
            case TelephonyManager.SIM_STATE_READY:
                return "良好";
        }
        return "";
    }

    private String getLocationMessage(Location location) {
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
                    + "getTime:" + location.getTime() + "\r\n"
                    + "-----------------------" + "\n";
        } else {
            return "getAccuracy:" + location.getAccuracy() + "\r\n"
                    + "getAltitude:" + location.getAltitude() + "\r\n"
                    + "getBearing:" + location.getBearing() + "\r\n"
                    + "getLatitude:" + location.getLatitude() + "\r\n"
                    + "getLongitude:" + location.getLongitude() + "\r\n"
                    + "getProvider:" + location.getProvider() + "\r\n"
                    + "getSpeed:" + location.getSpeed() + "\r\n"
                    + "getTime:" + location.getTime() + "\r\n"
                    + "-----------------------" + "\n";
        }
    }

    @SuppressLint("SetTextI18n")
    public void showMessage(String message) {
        if (StringUtil.isEmpty(message)) {
            return;
        }
        text_configuration_detail.setText('\n' + StringUtil.getText(text_configuration_detail) + message + '\n');
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionUtil.getInstance().onRequestPermissionsResult(PermissionUtil.REQUEST_CODE_LOCATION, grantResults)) {
            location(null);
            return;
        }
        if (PermissionUtil.getInstance().onRequestPermissionsResult(PermissionUtil.REQUEST_CODE_PHONE, grantResults)) {
            phoneMessage(null);
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
