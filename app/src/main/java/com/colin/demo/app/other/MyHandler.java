package com.colin.demo.app.other;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 描述：自定义Handler  解决内存溢出
 * <p>
 * 作者：Colin
 * 时间：2018/9/6
 */
public class MyHandler extends Handler {
    private WeakReference<Context> mWeakReference;

    public MyHandler(Context context) {
        mWeakReference = new WeakReference<>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        if (null != mWeakReference && null != mWeakReference.get()) {
            weakHandleMessage(msg);
        }
    }

    /**
     * 调用此方法
     *
     * @param message
     */
    public void weakHandleMessage(Message message) {
    }

}
