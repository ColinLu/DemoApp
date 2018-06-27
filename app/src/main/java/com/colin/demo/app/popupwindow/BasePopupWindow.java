package com.colin.demo.app.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.MetricsUtil;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/25
 */
public abstract class BasePopupWindow extends PopupWindow {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected int mLayoutRes = -1;                  //布局文件资源ID
    protected View mContentView;                    //PopupWindow显示的View

    public BasePopupWindow(Context context) {
        super(context);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        loadView();
        initView();
        initData();
        initListener();

    }


    private void loadView() {
        if (getLayoutRes() == -1) {
            throw new RuntimeException("PopupWindow布局文件资源ID没有");
        }
        mContentView = mLayoutInflater.inflate(getLayoutRes(), null);

        if (null == mContentView) {
            throw new RuntimeException("PopupWindow布局mContentView没有");
        }
        // 设置SelectPicPopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(android.R.style.Animation_InputMethod);
        // 设置背景色
        Resources res = mContentView.getResources();
        Drawable drawable = res.getDrawable(android.R.color.transparent);
        this.setBackgroundDrawable(drawable);

        mContentView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                int height = mContentView.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }



    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = MetricsUtil.getWindowsHeight(anchorView.getContext());
        final int screenWidth = MetricsUtil.getWindowsWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        LogUtil.e("isNeedShowUp-->>" + String.valueOf(isNeedShowUp));

        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    public int getLayoutRes() {
        return mLayoutRes;
    }
    public void show(View view) {
        int windowPos[] = calculatePopWindowPos(view, mContentView);
        int xOff = view.getMeasuredWidth() / 2;// 可以自己调整偏移
        windowPos[0] -= xOff;
        this.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }


    protected abstract void initView() ;
    protected abstract void initData() ;
    protected abstract void initListener() ;

}
