package com.colin.demo.app.activity.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.colin.demo.app.utils.LogUtil;

/**
 * Created by Colin on 2017/3/2.
 */

public class TouchViewGroupB extends ViewGroup {
    private Context context;

    public TouchViewGroupB(Context context) {
        this(context, null, 0);
    }

    public TouchViewGroupB(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchViewGroupB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupB onTouchEvent----------->>" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupB dispatchTouchEvent----->>" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupB onInterceptTouchEvent-->>" + event.getAction());
        return super.onInterceptTouchEvent(event);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = r - l - getPaddingRight();
        final int parentBottom = b - t - getPaddingBottom();

        final int viewWidth = getMeasuredWidth();
        final int viewHeight = getMeasuredHeight();
        LogUtil.d("viewWidth = " + viewWidth + ", viewHeight = " + viewHeight);
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                final int childViewWidth = childView.getMeasuredWidth();
                final int childViewHeight = childView.getMeasuredHeight();
                LogUtil.d("childViewWidth = " + childViewWidth + ", childViewHeight = " + childViewHeight);
                //居中处理
                left =(viewWidth - childViewWidth) / 2;
                top = (viewHeight - childViewHeight) / 2;

                right = viewWidth - ((viewWidth - childViewWidth) / 2);
                bottom = viewHeight - ((viewHeight - childViewHeight) / 2);
                childView.layout(left, top, right, bottom);
            }
        }
    }

    /**
     * 停止线程或动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
