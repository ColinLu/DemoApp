package com.colin.demo.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.colin.demo.app.utils.MetricsUtil;

/**
 * 描述：自定义控件
 * <p>
 * 实现方式 ：１集成系统控件　２　组合系统控件　３自定义绘制控件
 * <p>
 * 自定义View
 * void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
 * void onSizeChanged(int w, int h, int oldw, int oldh)
 * void onDraw(Canvas canvas)
 * <p>
 * 自定义ViewGroup
 * void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
 * void onSizeChanged(int w, int h, int oldw, int oldh)
 * void onLayout(boolean changed, int left, int top, int right, int bottom)
 * void onDraw(Canvas canvas)
 * <p>
 * <p>
 * (1)invalidate方法会执行onDraw过程，只能在UI线程调用
 * (2)postInvalidate 可以在非UI线程调用，省去了Handler消息调用
 * (3)RequestLayout 会执行onMeasure，onLayout ，onDraw
 * <p>
 * 作者：Colin
 * 时间：2018/8/10
 */
public class CustomView extends View {
    public CustomView(Context context) {
        this(context, null, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {

    }

    /**
     * 第一步：View宽高模式 计算宽度、高度
     * <p>
     * <p>
     * UNSPECIFIED	父容器没有对当前View有任何限制，当前View可以任意取尺寸（match_parent）
     * EXACTLY	当前的尺寸就是当前View应该取的尺寸（xml配置固定值）
     * AT_MOST	当前尺寸是当前View能取的最大尺寸（wrap_content）
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = MetricsUtil.dp2px(getContext(), 400f);
        int height = MetricsUtil.dp2px(getContext(), 400f);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                //相当于match_parent或者一个具体值
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                // 相当于wrap_content ，需要手动测量大小，这里先写死大小
                width = MetricsUtil.dp2px(getContext(), 400f);
                break;
            case MeasureSpec.UNSPECIFIED:
                //很少会用到
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                //相当于match_parent或者一个具体值
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                // 相当于wrap_content ，需要手动测量大小，这里先写死大小
                height = MetricsUtil.dp2px(getContext(), 400f);
                break;
            case MeasureSpec.UNSPECIFIED:
                //很少会用到
                break;
            default:
                break;
        }
        //存储测量好的宽和高
        setMeasuredDimension(width, height);
    }

    /**
     * 第二步：绘制自己的形态
     * 在onMeasure()后执行，只有大小发生了变化才会执行onSizeChange
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh q
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        diameter = Math.min(mRWidth, mRHeight);
    }

    /**
     * 第三步
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
