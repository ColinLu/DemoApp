package com.colin.demo.app.activity.handler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.utils.ImageLoader;

import java.lang.ref.WeakReference;

/**
 * Handler + looper + message
 * <p>
 * <p>
 * handler是什么？
 * handler是android给我们提供用来更新UI的一套机制，也是一套消息处理机制，我们可以发送消息，也可以通过它处理消息
 * <p>
 * 更新UI、发送消息、处理消息
 */
public class HandlerActivity extends BaseActivity {
    private ItemBean mItemBean;

    private MyHandler mMyHandler = null;
    private ImageView image_handler_show;
    private int index = 0;
    private static final int IMAGE_INDEX = 0;
    private static final int IMAGE_TIME = 3000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //true 截获消息
            return false;
        }
    }) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        mMyHandler = null;
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
    }

    @Override
    protected void initView() {
        image_handler_show = this.findViewById(R.id.image_handler_show);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);
        mMyHandler = new MyHandler(this);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.button_remove_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyHandler.removeMessages(IMAGE_INDEX);
            }
        });
    }

    @Override
    protected void initAsync() {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showIndexImage(index);
    }

    private void showIndexImage(int index) {
        index = index % Constants.IMAGE_URL.length;
        ImageLoader.getInstance().loadImage(image_handler_show, Constants.IMAGE_URL[index]);
        mMyHandler.sendEmptyMessageDelayed(IMAGE_INDEX, IMAGE_TIME);
    }

    /**
     * 自定义 避免内存溢出
     */
    private class MyHandler extends Handler {
        private WeakReference<Activity> mWeakReference;

        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            //空值处理 避免内存溢出
            if (null == mWeakReference || null == mWeakReference.get()) {
                return;
            }

            switch (msg.what) {
                case IMAGE_INDEX://定时循环处理
                    index += 1;
                    showIndexImage(index);
                    break;
            }
        }

    }
}
