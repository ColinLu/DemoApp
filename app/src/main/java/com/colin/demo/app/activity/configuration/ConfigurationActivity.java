package com.colin.demo.app.activity.configuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.dialog.DialogTips;
import com.colin.demo.app.utils.AppUtil;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.PermissionUtil;
import com.colin.demo.app.utils.StringUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.util.Arrays;

public class ConfigurationActivity extends BaseActivity {
    private TextView text_configuration_detail;
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }

    @Override
    protected void initView() {
        this.text_configuration_detail = this.findViewById(R.id.text_configuration_detail);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);
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

    /**
     * 获取WiFi信息
     *
     * @param view
     */
    public void wifi(View view) {
        WifiInfo wifiInfo = AppUtil.getWifiInfo(this);
        showMessage(null == wifiInfo ? "" : wifiInfo.toString());

    }

    public void phoneMessage(View view) {
        TelephonyManager telephonyManager = AppUtil.getTelephonyManager(this);
        if (null == telephonyManager) {
            ToastUtil.showToast("获取手机信息失败");
            return;
        }
        if (!PermissionUtil.getInstance().checkPermission(this, Arrays.asList(PermissionUtil.PERMISSIONS_PHONE), PermissionUtil.REQUEST_CODE_PHONE)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String IMSI = telephonyManager.getSubscriberId();
        //返回一个常数表示当前数据连接状态
        int callState = telephonyManager.getCallState();
        int simState = telephonyManager.getSimState();
        //返回1号线的电话号码，例如，MSISDN用于GSM电话。
        String line1Number = telephonyManager.getLine1Number();
        //返回注册的网络运营商的国家代码
        String networkCountryIso = telephonyManager.getNetworkCountryIso();
        //返回注册的网络运营商的名字
        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        //返回一个常数，表示目前在设备上使用的无线电技术（网络类型）。
        int networkType = telephonyManager.getNetworkType();
        //返回设备的类型（手机制式）。
        int phoneType = telephonyManager.getPhoneType();
        //返回SIM卡运营商的国家代码
        String simCountryIso = telephonyManager.getSimCountryIso();

        stringBuilder.append("唯一的用户ID-->>").append(IMSI).append('\n');
        stringBuilder.append("sim的状态-->>").append(getSimState(simState)).append('\n');
        stringBuilder.append("callState-->>").append(callState).append('\n');
        stringBuilder.append("line1Number-->>").append(line1Number).append('\n');
        stringBuilder.append("networkCountryIso-->>").append(networkCountryIso).append('\n');
        stringBuilder.append("networkOperatorName-->>").append(networkOperatorName).append('\n');
        stringBuilder.append("networkType-->>").append(networkType).append('\n');
        stringBuilder.append("phoneType-->>").append(phoneType).append('\n');
        stringBuilder.append("simCountryIso-->>").append(simCountryIso).append('\n');


        stringBuilder.append("Product: " + android.os.Build.PRODUCT + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("CPU_ABI: " + android.os.Build.CPU_ABI + System.getProperty("line.separator")).append('\n');
        if (Build.VERSION.SDK_INT > 21 && null != Build.SUPPORTED_ABIS && Build.SUPPORTED_ABIS.length > 0) {
            for (String supportedAbi : Build.SUPPORTED_ABIS) {
                stringBuilder.append("CPU指令集-->>").append(supportedAbi).append('\n');
            }
        }
        stringBuilder.append("TAGS: " + android.os.Build.TAGS + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("MODEL: " + android.os.Build.MODEL + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SDK: " + android.os.Build.VERSION.SDK + System.getProperty("line.separator"));
        stringBuilder.append("VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("DEVICE: " + android.os.Build.DEVICE + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("DISPLAY: " + android.os.Build.DISPLAY + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("BRAND: " + android.os.Build.BRAND + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("BOARD: " + android.os.Build.BOARD + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("FINGERPRINT: " + android.os.Build.FINGERPRINT + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("ID: " + android.os.Build.ID + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("MANUFACTURER: " + android.os.Build.MANUFACTURER + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("USER: " + android.os.Build.USER + System.getProperty("line.separator")).append('\n');


        String[] abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        StringBuilder abiStr = new StringBuilder();
        for (String abi : abis) {
            abiStr.append(abi);
            abiStr.append(',');
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        stringBuilder.append("DeviceId(IMEI) = " + tm.getDeviceId() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("Line1Number = " + tm.getLine1Number() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("NetworkCountryIso = " + tm.getNetworkCountryIso() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("NetworkOperator = " + tm.getNetworkOperator() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("NetworkOperatorName = " + tm.getNetworkOperatorName() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("NetworkType = " + tm.getNetworkType() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("PhoneType = " + tm.getPhoneType() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SimCountryIso = " + tm.getSimCountryIso() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SimOperator = " + tm.getSimOperator() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SimOperatorName = " + tm.getSimOperatorName() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SimSerialNumber = " + tm.getSimSerialNumber() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SimState = " + tm.getSimState() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("SubscriberId(IMSI) = " + tm.getSubscriberId() + System.getProperty("line.separator")).append('\n');
        stringBuilder.append("VoiceMailNumber = " + tm.getVoiceMailNumber() + System.getProperty("line.separator"));
        showMessage(stringBuilder.toString());

    }

    public void cpu(View view) {

    }

    public void system(View view) {
    }

    public void storage(View view) {
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

    @SuppressLint("SetTextI18n")
    public void showMessage(String message) {
        if (StringUtil.isEmpty(message)) {
            return;
        }
        text_configuration_detail.setText(StringUtil.getText(text_configuration_detail) + message + '\n');
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
