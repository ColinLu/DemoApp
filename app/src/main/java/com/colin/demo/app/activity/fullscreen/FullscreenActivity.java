package com.colin.demo.app.activity.fullscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.colin.demo.app.R;

import java.lang.ref.WeakReference;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private static final int STATE_HIDE_VIEW = 0;       //隐藏状态栏 隐藏标题 隐藏下方控件
    private static final int STATE_SHOW_CONTENT = 1;    //展示界面  全屏显示 与 STATE_HIDE_VIEW 相对出现  需要延迟

    private static final int STATE_SHOW_VIEW = 2;       //展示界面  不全屏显示与STATE_SHOW_BUTTON 相对出现  需要延迟 显示状态栏 显示标题 显示下方控件
    private static final int STATE_SHOW_BUTTON = 3;     //展示界面  不全屏显示与STATE_SHOW_VIEW 相对出现  需要延迟

    public static final int DELAY_MILLIS = 3000;
    public static final int UI_DELAY_MILLIS = 300;
    //线程处理 显示隐藏效果
    private final MyHandler mMyHandler = new MyHandler(this);

    private View mContentView;
    private View mControlsView;

    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.layou_button);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        findViewById(R.id.dummy_button).setOnTouchListener(mBottomButtonTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mMyHandler.sendEmptyMessageDelayed(STATE_HIDE_VIEW, UI_DELAY_MILLIS);
    }

    /**
     * 显示与隐藏 状态切换
     */
    private void toggle() {
        mMyHandler.sendEmptyMessage(mVisible ? STATE_HIDE_VIEW : STATE_SHOW_VIEW);
    }


    @SuppressLint("HandlerLeak")
    private final class MyHandler extends Handler {
        private WeakReference<Activity> mWeakReference;

        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            if (null == mWeakReference || null == mWeakReference.get() || null == msg) {
                return;
            }
            switch (msg.what) {
                case STATE_HIDE_VIEW:  //暴露处理
                    showView(false);
                    //延迟展示界面 全屏显示
                    mMyHandler.sendEmptyMessageDelayed(STATE_SHOW_CONTENT, UI_DELAY_MILLIS);
                    break;
                case STATE_SHOW_CONTENT:
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    break;
                case STATE_SHOW_VIEW://暴露处理
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                    //延迟展示标题与下方按钮
                    mMyHandler.sendEmptyMessageDelayed(STATE_SHOW_BUTTON, UI_DELAY_MILLIS);
                    break;
                case STATE_SHOW_BUTTON:
                    showView(true);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 空间显示隐藏
     *
     * @param show
     */
    private void showView(boolean show) {
        if (getSupportActionBar() != null && !show) {
            getSupportActionBar().hide();
        } else if (null != getSupportActionBar() && show) {
            getSupportActionBar().show();
        }
        mControlsView.setVisibility(show ? View.VISIBLE : View.GONE);
        this.mVisible = show;
    }

    /**
     * 触摸下方按钮 3s 自动隐藏
     */
    private final View.OnTouchListener mBottomButtonTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMyHandler.sendEmptyMessageDelayed(STATE_HIDE_VIEW, DELAY_MILLIS);
            return false;
        }
    };
}
