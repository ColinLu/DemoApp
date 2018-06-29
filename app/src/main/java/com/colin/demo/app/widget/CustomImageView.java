package com.colin.demo.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.colin.demo.app.R;

/**
 * 描述：自定义ImageView
 * 实现正方形 效果
 * <p>
 * 作者：Colin
 * 时间：2018/6/29
 */
public class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    private boolean isSquare = false;
    private boolean isCircle = false;

    public CustomImageView(Context context) {
        this(context, null, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
    }

    private void initAttrs(AttributeSet attributeSet, int defStyleAttr) {
        if (null == attributeSet) {
            return;
        }
        //load styled attributes.
        final TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomImageView, defStyleAttr, 0);
        isSquare = attributes.getBoolean(R.styleable.CustomImageView_image_isSquare, false);
        isCircle = attributes.getBoolean(R.styleable.CustomImageView_image_isCircle, false);
        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, isSquare || isCircle ? widthMeasureSpec : heightMeasureSpec);
    }
}
