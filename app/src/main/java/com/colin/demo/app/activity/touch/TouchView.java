package com.colin.demo.app.activity.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.colin.demo.app.utils.LogUtil;

/**
 * Created by Colin on 2017/3/2.
 */

public class TouchView extends View {
    private Context context;

    public TouchView(Context context) {
        this(context, null, 0);
    }

    public TouchView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "View onTouchEvent----------------->>" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "View dispatchTouchEvent----------->>" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        int defaultDimension = MetricsUtil.dp2px(context, 100);
//        //自定义控件的padding
//        viewHeight += getPaddingTop() + getPaddingBottom();
//        viewWidth += getPaddingLeft() + getPaddingRight();
//        //处理 wrap_content问题
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(defaultDimension, defaultDimension);
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(defaultDimension, viewHeight);
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(viewWidth, defaultDimension);
//        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }
}