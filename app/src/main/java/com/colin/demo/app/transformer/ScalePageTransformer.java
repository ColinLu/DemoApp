package com.colin.demo.app.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/25
 */
public class ScalePageTransformer extends BasePageTransformer {

    public static final float MIN_SCALE = 0.75f;

    public ScalePageTransformer() {
        this(null);
    }

    public ScalePageTransformer(ViewPager.PageTransformer pageTransformer) {
        this.mPageTransformer = pageTransformer;
    }

    /**
     * 重新计算 page 的位置transformPos，使得
     * 当page居中时 transformPos为0
     * 当page向左偏移时 transformPos为[-Infinity, 0)
     * 当page向右偏移时 transformPos为(0, +Infinity]
     */
    @Override
    protected void pageTransform(View page, float position) {
        ViewPager viewPager = (ViewPager) page.getParent();
        int scrollX = viewPager.getScrollX();
        int clientWidth = viewPager.getMeasuredWidth() -
                viewPager.getPaddingLeft() - viewPager.getPaddingRight();
        int offsetX = page.getLeft() - scrollX;
        int parentWidth = viewPager.getMeasuredWidth();
        int childWidth = page.getWidth();
        float deltaX = (float) (parentWidth - childWidth) / 2;
        float transformPos = (offsetX - deltaX) / clientWidth;

        if (transformPos < -1) { // [-Infinity,-1)
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);

        } else if (transformPos <= 1) { // [-1,1]
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(transformPos));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);

        }
    }
}
