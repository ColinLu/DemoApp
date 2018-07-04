package com.colin.demo.app.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * 任何一个Service在整个应用程序范围内都是通用的，即BinderService不仅可以和MainActivity建立关联，
 * 还可以和任何一个Activity建立关联，而且在建立关联时它们都可以获取到相同的MyBinder实例。
 * <p/>
 * 一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
 * <p/>
 * Service其实是运行在主线程里的:在Service里编写了非常耗时的代码，程序必定会出现ANR的。
 * <p/>
 * Android的后台就是指，它的运行是完全不依赖UI的。即使Activity被销毁，或者程序被关闭，只要进程还在，Service就可以继续运行。
 * 比如说一些应用程序，始终需要与服务器之间始终保持着心跳连接，就可以使用Service来实现。
 * 你可能又会问，前面不是刚刚验证过Service是运行在主线程里的么？在这里一直执行着心跳连接，
 * 难道就不会阻塞主线程的运行吗？当然会，但是我们可以在Service中再创建一个子线程，然后在这里去处理耗时逻辑就没问题了。
 * <p/>
 * 创建前台Service
 * Service几乎都是在后台运行的，一直以来它都是默默地做着辛苦的工作。但是Service的系统优先级还是比较低的，
 * 当系统出现内存不足情况时，就有可能会回收掉正在后台运行的Service。
 * 如果你希望Service可以一直保持运行状态，而不会由于系统内存不足的原因导致被回收，就可以考虑使用前台Service。
 * 前台Service和普通Service最大的区别就在于，它会一直有一个正在运行的图标在系统的状态栏显示，下拉状态栏后可以看到更加详细的信息，非常类似于通知的效果。
 * 当然有时候你也可能不仅仅是为了防止Service被回收才使用前台Service，有些项目由于特殊的需求会要求必须使用前台Service，
 * 比如说墨迹天气，它的Service在后台更新天气数据的同时，还会在系统状态栏一直显示当前天气的信息，
 * <p/>
 * 一个普通的Service转换成远程Service其实非常简单，只需要在注册Service的时候将它的android:process属性指定成:remote就可以了
 * 远程Service较为难用。一般情况下如果可以不使用远程Service，就尽量不要使用它
 * Activity与一个远程Service建立关联呢？这就要使用AIDL来进行跨进程通信了（IPC）
 */
public class BinderService extends Service {
    private DemoBinder mBinder = new DemoBinder();
    private Messenger mMessenger = null;
    private static final int MSG_SERVICE_HELLO = 1;

    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        private WeakReference<Context> mWeakReference;

        public ServiceHandler(Context context) {
            mWeakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == mWeakReference || null == mWeakReference.get()) {
                return;
            }
            switch (msg.what) {
                case MSG_SERVICE_HELLO:
                    //当收到客户端的message时，显示hello
                    ToastUtil.showToast("hello!");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public BinderService() {
    }

    /**
     * 由于onCreate()方法只会在Service第一次被创建的时候调用，
     * 如果当前Service已经被创建过了，不管怎样调用startService()方法，
     * onCreate()方法都不会再执行。因此你可以再多点击几次Start Service按钮试一次，
     * 每次都只会有onStartCommand()方法中的打印日志。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mMessenger) {
            mMessenger = new Messenger(new ServiceHandler(this));
        }
    }

    /**
     * onBind()方法用于和Activity建立关联的
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        mMessenger.getBinder();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("onStartCommand()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("执行具体的下载任务");
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * MyBinder类继承自Binder类，然后在MyBinder中添加了一个startDownload()方法用于在后台执行任务，
     * 做个测试:打印了一行日志
     */
    public class DemoBinder extends Binder {
        public void startDownload() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体的下载任务
                    LogUtil.e("执行具体的下载任务");
                }
            }).start();
            LogUtil.e("startDownload()");
            // 执行具体的下载任务
        }

    }
}
