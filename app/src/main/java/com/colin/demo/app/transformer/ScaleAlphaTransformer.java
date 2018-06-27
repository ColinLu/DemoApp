package com.colin.demo.app.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.colin.demo.app.utils.LogUtil;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/25
 */
public class ScaleAlphaTransformer implements ViewPager.PageTransformer {
    public static float MIN_ALPHA = 0.5f;
    public static float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            page.setAlpha(MIN_ALPHA);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            LogUtil.d("info", "缩放：position < -1 || position > 1");
        } else if (position <= 1) { // [-1,1]
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            if (position < 0) {
                float scaleX = 1 + 0.2f * position;
                LogUtil.d("info", "缩放：position < 0");
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
            } else {
                float scaleX = 1 - 0.2f * position;
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
                LogUtil.d("info", "缩放：position <= 1 >=0");
            }
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}
