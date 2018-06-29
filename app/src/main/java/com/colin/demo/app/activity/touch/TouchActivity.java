package com.colin.demo.app.activity.touch;

import android.os.Bundle;
import android.view.MotionEvent;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;

/**
 * Android事件分发流程 = Activity -> ViewGroup -> View
 * 即：1个点击事件发生后，事件先传到Activity、再传到ViewGroup、最终再传到 View
 * <p>
 * Activity对点击事件的分发机制
 * ViewGroup对点击事件的分发机制
 * View对点击事件的分发机制
 */
public class TouchActivity extends BaseActivity {
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
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
     * 源码分析：Activity.dispatchTouchEvent（）
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // 一般事件列开始都是DOWN事件 = 按下事件，故此处基本是true
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            onUserInteraction();
            // ->>分析1

        }

        // ->>分析2
        if (getWindow().superDispatchTouchEvent(ev)) {

            return true;
            // 若getWindow().superDispatchTouchEvent(ev)的返回true
            // 则Activity.dispatchTouchEvent（）就返回true，则方法结束。即 ：该点击事件停止往下传递 & 事件传递过程结束
            // 否则：继续往下调用Activity.onTouchEvent

        }
        // ->>分析4
        return onTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
