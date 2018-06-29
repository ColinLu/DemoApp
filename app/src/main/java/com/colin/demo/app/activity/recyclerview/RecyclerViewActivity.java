package com.colin.demo.app.activity.recyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;

public class RecyclerViewActivity extends BaseActivity {
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    }

    @Override
    protected void initAsync() {

    }

}
