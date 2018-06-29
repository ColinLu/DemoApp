package com.colin.demo.app.activity.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.manager.BlueManager;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.ToastUtil;

/**
 * 开发的步骤：
 * <p>
 * 2.1首先获取BluetoothManager
 * 2.2获取BluetoothAdapter
 * 2.3创建BluetoothAdapter.LeScanCallback
 * 2.4.开始搜索设备。
 * 2.5.BluetoothDevice 描述了一个蓝牙设备 提供了getAddress()设备Mac地址,getName()设备的名称。
 * 2.6开始连接设备
 * 2.7连接到设备之后获取设备的服务(Service)和服务对应的Characteristic。
 * 2.8获取到特征之后，找到服务中可以向下位机写指令的特征，向该特征写入指令。
 * 2.9写入成功之后，开始读取设备返回来的数据。
 * 2.10、断开连接
 * 2.11、数据的转换方法
 */
public class BluetoothActivity extends BaseActivity {
    private ItemBean mItemBean;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        mBluetoothAdapter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }

    @Override
    protected void initView() {

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

    }

    @Override
    protected void initAsync() {

    }

    /**
     * 扫描蓝牙设备
     *
     * @param view
     */
    public void scanBluetooth(View view) {
        BluetoothAdapter bluetoothAdapter = BlueManager.getInstance().getBluetoothAdapter(this);
        //检查是否拥有蓝牙功能
        if (null == bluetoothAdapter) {
            ToastUtil.showToast("不支持蓝牙设备");
            return;
        }
        //检查是否打开
        if (!bluetoothAdapter.isEnabled()) {
            BlueManager.getInstance().openBluetooth(this, Constants.REQUEST_CODE_OPEN_BLUETOOTH);
            return;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("onActivityResult");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_OPEN_BLUETOOTH) {
            scanBluetooth(null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
