package com.colin.demo.app.activity.constraintlayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;

public class ConstraintLayoutActivity extends BaseActivity {
    private ItemBean mItemBean;
    private ImageView image_size_ratio_w;
    private TextView text_size_ratio_w;
    private ImageView image_size_ratio_h;
    private TextView text_size_ratio_h;
    private TextView text_constraint_barrier_3;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);
    }

    @Override
    protected void initView() {
        image_size_ratio_w = this.findViewById(R.id.image_size_ratio_w);
        text_size_ratio_w = this.findViewById(R.id.text_size_ratio_w);
        image_size_ratio_h = this.findViewById(R.id.image_size_ratio_h);
        text_size_ratio_h = this.findViewById(R.id.text_size_ratio_h);
        text_constraint_barrier_3 = this.findViewById(R.id.text_constraint_barrier_3);
        text_constraint_barrier_3.setMovementMethod(ScrollingMovementMethod.getInstance());
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        image_size_ratio_h.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        },300);



    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        int wh = image_size_ratio_w.getMeasuredHeight();
        int ww = image_size_ratio_w.getMeasuredWidth();
        int hh = image_size_ratio_h.getMeasuredHeight();
        int hw = image_size_ratio_h.getMeasuredWidth();

        text_size_ratio_w.setText(String.format("宽度：%d;高度：%d", ww, wh));
        text_size_ratio_h.setText(String.format("宽度：%d;高度：%d", hw, hh));

    }
}
