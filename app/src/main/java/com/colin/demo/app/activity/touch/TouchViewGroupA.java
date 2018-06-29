package com.colin.demo.app.activity.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.MetricsUtil;

/**
 * Created by Colin on 2017/3/2.
 * 实现高度等于宽度 正方形
 */

public class TouchViewGroupA extends RelativeLayout {
    private Context context;

    public TouchViewGroupA(Context context) {
        this(context, null, 0);
    }

    public TouchViewGroupA(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchViewGroupA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupA onTouchEvent----------->>" + event.getAction());
        return super.onTouchEvent(event);
    }

    /**
     * 如果拦截 ViewGroup onTouchEvent 返回true                      ViewGroup 消费
     * 如果不拦截 ViewGroup 的子控件dispatchTouchEvent 返回false      ViewGroup 不消费
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupA dispatchTouchEvent----->>" + event.getAction());
        boolean consume = false;
        if (onInterceptTouchEvent(event)) {
            consume = onTouchEvent(event);
        } else {
            consume = this.getChildAt(0).dispatchTouchEvent(event);
        }
        return consume;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        LogUtil.d("TouchView", "ViewGroupA onInterceptTouchEvent-->>" + event.getAction());
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int viewWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int viewHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        //遍历每个子View
        int childrenWidth = 0;
        int childrenHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //考虑View.GONE
            if (child.getVisibility() != GONE) {
                // 测量每一个child的宽和高
                measureChild(child, widthMeasureSpec, widthMeasureSpec);
                //调用子View的onMeasure，设置他们的大小。childWidthMeasureSpec ， childHeightMeasureSpec ?
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
                childrenWidth = child.getMeasuredWidth();
                childrenHeight = child.getMeasuredHeight();
                childrenWidth += marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                childrenHeight += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                LogUtil.d("childrenWidth==" + String.valueOf(childrenWidth));
                LogUtil.d("childrenHeight==" + String.valueOf(childrenHeight));
            }

            //自定义控件的padding
            viewHeight += getPaddingTop() + getPaddingBottom();
            viewWidth += getPaddingLeft() + getPaddingRight();
            //wrap_content默认值
            int defaultDimension = MetricsUtil.dp2px(context, 100);
            //得到最大值
            defaultDimension = Math.max(defaultDimension, childrenWidth);
            defaultDimension = Math.max(defaultDimension, childrenHeight);
            //处理 wrap_content问题
            if (viewWidthMode == MeasureSpec.AT_MOST && viewHeightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(defaultDimension, defaultDimension);
            } else if (viewWidthMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(defaultDimension, viewHeight);
            } else if (viewHeightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(viewWidth, defaultDimension);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.d("w-->>" + String.valueOf(w));
        LogUtil.d("h-->>" + String.valueOf(h));
    }

    /**
     * 总宽度是多少，这个值可以通过getMeasuredWidth()来得到
     * 子控件的宽度也可以通过子控件对象的getMeasuredWidth()来得到。
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
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
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //居中处理
                left = Math.max(parentLeft + marginLayoutParams.leftMargin, (viewWidth - childViewWidth) / 2);
                top = Math.max(parentTop + marginLayoutParams.topMargin, (viewHeight - childViewHeight) / 2);

                right = Math.min(parentRight + marginLayoutParams.rightMargin, viewWidth - ((viewWidth - childViewWidth) / 2));
                bottom = Math.min(parentBottom + marginLayoutParams.bottomMargin, viewHeight - ((viewHeight - childViewHeight) / 2));
                childView.layout(left, top, right, bottom);
            }
        }
    }

}
