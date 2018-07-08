package com.colin.demo.app.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.colin.demo.app.utils.LogUtil;

import java.util.Set;

/**
 * 描述：蓝牙管理：Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
 * <p>
 * 作者：Colin
 * 时间：2018/6/29
 */
public class BlueManager {
    private Application context;

    //    private BluetoothManager bluetoothManager;
//    private BluetoothAdapter bluetoothAdapter;
//    private BleScanRuleConfig bleScanRuleConfig;
//    private MultipleBluetoothController multipleBluetoothController;
    private BlueManager() {
    }

    public static class Holder {
        static BlueManager instance = new BlueManager();
    }

    public static BlueManager getInstance() {
        return BlueManager.Holder.instance;
    }

    /**
     * 初始化蓝牙管理
     *
     * @param app
     */
    public void init(Application app) {
        if (null == context && null != app) {
            this.context = app;
        }
    }

    /**
     * is support ble?
     *
     * @return
     */
    public boolean isSupportBle() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 需要版本判断 4.3
     *
     * @param context
     * @return
     */
    public BluetoothManager getBluetoothManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        return null;
    }

    /**
     * 需要版本判断 4.3
     *
     * @param context
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter(Context context) {
        BluetoothManager bluetoothManager = getBluetoothManager(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && null != bluetoothManager) {
            return bluetoothManager.getAdapter();
        }
        return BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 获取蓝牙名称
     *
     * @param context
     * @return
     */
    public String getBluetoothName(Context context) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
        return null == bluetoothAdapter ? null : bluetoothAdapter.getName();
    }
    /**
     * 获取蓝牙名称
     *
     * @param context
     * @return
     */
    public Set<BluetoothDevice> getBluetoothBondedDevices(Context context) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
        return null == bluetoothAdapter ? null : bluetoothAdapter.getBondedDevices();
    }
    /**
     * 获取蓝牙地址
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public String getBluetoothAddress(Context context) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
        return null == bluetoothAdapter ? null : bluetoothAdapter.getAddress();
    }

    /**
     * 获取蓝牙开关状态
     * STATE_OFF            蓝牙已经关闭
     * STATE_ON             蓝牙已经打开
     * STATE_TURNING_OFF    蓝牙处于关闭过程中 ，关闭ing
     * STATE_TURNING_ON     蓝牙处于打开过程中 ，打开ing
     *
     * @param context
     * @return
     */
    public int getBluetoothState(Context context) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
        return null == bluetoothAdapter ? BluetoothAdapter.STATE_OFF : bluetoothAdapter.getState();
    }

    public String getBluetoothState(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                return "关闭";
            case BluetoothAdapter.STATE_ON:
                return "打开";
            case BluetoothAdapter.STATE_TURNING_OFF:
                return "关闭过程中";
            case BluetoothAdapter.STATE_TURNING_ON:
                return "打开过程中";
            default:
                return "关闭";
        }
    }

    public String getBluetoothStateString(Context context) {
        return getBluetoothState(getBluetoothState(context));
    }

    /**
     * 获取蓝牙扫描状态
     * SCAN_MODE_CONNECTABLE                可以扫描其他蓝牙设备
     * SCAN_MODE_CONNECTABLE_DISCOVERABLE   可以扫描其他蓝牙设备，可以被其他蓝牙设备扫描。
     * SCAN_MODE_NONE                       不能扫描以及被扫描
     *
     * @param context
     * @return
     */
    public int getBluetoothScanMode(Context context) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
        return null == bluetoothAdapter ? BluetoothAdapter.SCAN_MODE_NONE : bluetoothAdapter.getScanMode();
    }

    public String getBluetoothScanMode(int state) {
        switch (state) {
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                return "可以扫描其他蓝牙设备";
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                return "可以扫描其他蓝牙设备，可以被其他蓝牙设备扫描";
            case BluetoothAdapter.SCAN_MODE_NONE:
                return "不能扫描以及被扫描";
            default:
                return "不能扫描以及被扫描";
        }
    }

    public String getBluetoothScanModeString(Context context) {
        return getBluetoothScanMode(getBluetoothScanMode(context));
    }

    /**
     * 打开蓝牙 是否用暴力方式
     *
     * @param requestCode 决定是否暴力法打开  <=0暴力法打开
     */
    public void openBluetooth(Activity activity, int requestCode) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(activity);
        if (null == bluetoothAdapter) {
            LogUtil.e("蓝牙设备未发现");
            return;
        }
        //暴力法打开
        if (requestCode <= 0 && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            return;
        }
        //隐式启动Intent:
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, requestCode);
        }

    }

    /**
     * 扫描设备
     */
    private void scanLeDevice(final boolean enable) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            if (enable) {
//                devices.clear();//清空集合
//                // Stops scanning after a pre-defined scan period.
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        }
//                    }
//                }, INTERVAL_TIME);
//                mBluetoothAdapter.startLeScan(mLeScanCallback);
//            } else {
//                try {
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                } catch (Exception e) {
//                }
//            }
//        }
    }
}
