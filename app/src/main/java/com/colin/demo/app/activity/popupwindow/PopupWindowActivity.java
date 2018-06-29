package com.colin.demo.app.activity.popupwindow;

import android.os.Bundle;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;

public class PopupWindowActivity extends BaseActivity implements View.OnClickListener {
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
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
        findViewById(R.id.text_pop_right).setOnClickListener(this);
        findViewById(R.id.text_pop_left).setOnClickListener(this);
        findViewById(R.id.text_pop_top).setOnClickListener(this);
        findViewById(R.id.text_pop_bottom).setOnClickListener(this);
    }

    @Override
    protected void initAsync() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_pop_right:
                break;
            case R.id.text_pop_left:
                break;
            case R.id.text_pop_top:
                break;
            case R.id.text_pop_bottom:
                break;
            default:
                break;
        }
    }
}
