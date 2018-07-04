package com.colin.demo.app.activity.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.service.BinderService;
import com.colin.demo.app.utils.LogUtil;

/**
 * 四大组件服务  基本用法
 */
public class ServiceActivity extends BaseActivity implements View.OnClickListener {
    private BinderService.DemoBinder demoBinder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    @Override
    protected void onDestroy() {
        if (null != demoBinder) {
            demoBinder = null;
        }

        super.onDestroy();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        findViewById(R.id.button_service_start).setOnClickListener(this);
        findViewById(R.id.button_service_stop).setOnClickListener(this);
        findViewById(R.id.button_service_bind).setOnClickListener(this);
        findViewById(R.id.button_service_unbind).setOnClickListener(this);
        findViewById(R.id.button_service_bind_remote).setOnClickListener(this);
        findViewById(R.id.button_service_unbind_remote).setOnClickListener(this);
    }

    @Override
    protected void initAsync() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_service_start:
                intent = new Intent(this, BinderService.class);
                startService(intent);
                break;
//            一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
            case R.id.button_service_stop:
                intent = new Intent(this, BinderService.class);
                stopService(intent);
                break;
            case R.id.button_service_bind:
                intent = new Intent(this, BinderService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
                break;
//            一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
            case R.id.button_service_unbind:
                try {
                    unbindService(connection);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                break;
//            case R.id.button_service_bind_remote:
//                intent = new Intent(this, RemoteService.class);
//                bindService(intent, remote_connection, BIND_AUTO_CREATE);
//                break;
////            一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
//            case R.id.button_service_unbind_remote:
//                try {
//                    unbindService(remote_connection);
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("onServiceDisconnected==" + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("onServiceConnected==" + name);
            demoBinder = (BinderService.DemoBinder) service;
            demoBinder.startDownload();
        }
    };

//    private ServiceConnection remote_connection = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            LogHelp.d("onServiceDisconnected==" + name);
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LogHelp.d("onServiceConnected==" + name);
//            LogHelp.d("Service thread id is " + Thread.currentThread().getName());
//            myAIDLService = MyAIDLService.Stub.asInterface(service);
//            try {
//                int result = myAIDLService.plus(3, 5);
//                String upperStr = myAIDLService.toUpperCase("hello world");
//                LogHelp.d("TAG", "result is " + result);
//                LogHelp.d("TAG", "upperStr is " + upperStr);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    };
}
