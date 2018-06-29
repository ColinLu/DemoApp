package com.colin.demo.app.manager;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.colin.demo.app.utils.LogUtil;

/**
 * 描述：蓝牙管理
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
