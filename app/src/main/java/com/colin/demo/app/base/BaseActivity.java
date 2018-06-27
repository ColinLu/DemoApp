package com.colin.demo.app.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.colin.demo.app.R;

/**
 * 描述：基类Activity 处理界面通用方法  eg:
 * <p>
 * 作者：Colin
 * 时间：2018/6/20
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;                                 //新控件 标题
    protected boolean isDestroy = false;                        //变量 表示当前fragment状态
    protected boolean isResume = false;                         //变量 表示当前fragment状态

    @Override
    protected void onDestroy() {
        isDestroy = true;
        mToolbar = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        //监听返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_finish_in, R.anim.activity_finish_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        isResume = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isResume = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        isResume = false;
        super.onStop();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initBaseView();
        initView();
        initData();
        initListener();
        initAsync();
    }

    private void initBaseView() {
        this.mToolbar = this.findViewById(R.id.toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(!this.getClass().getSimpleName().equals("MainActivity"));
            mToolbar.setNavigationOnClickListener(new OnBackListener());
        }
    }

    /**
     * 方法3：手工处理旋转
     * 一般情况下Configuration的改变会导致Activity被销毁重建，但也有办法让指定的Configuration改变时不重建Activity，方法是在AndroidManifest.xml里通过android:configChanges属性指定需要忽略的Configuration名字
     * <activity
     * 　　android:name=".MyActivity"
     * 　　android:configChanges="orientation|keyboardHidden"
     * 　　android:label="@string/app_name"/>
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "横屏模式", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "竖屏模式", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 界面跳转
     */
    public void startActivity(@NonNull Class<? extends Activity> target) {
        startActivity(target, null, false);
    }

    public void startActivity(@NonNull Class<? extends Activity> target, @Nullable Bundle bundle) {
        startActivity(target, bundle, false);
    }

    public void startActivity(@NonNull Class<? extends Activity> target, boolean closeSelf) {
        startActivity(target, null, closeSelf);
    }

    public void startActivity(@NonNull String target, @Nullable Bundle bundle, boolean closeSelf) {
        Class clazz = null;
        try {
            clazz = Class.forName(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz != null) {
            startActivity(clazz, bundle, closeSelf);
        }
    }

    @SuppressLint("NewApi")
    public void startActivity(@NonNull Class<? extends Activity> target, @Nullable Bundle bundle, boolean closeSelf) {
        if (closeSelf) {
            this.finish();
        }
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        this.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void startActivityForResult(@NonNull Class<? extends Activity> target, int requestCode) {
        startActivityForResult(target, requestCode, null);
    }

    public void startActivityForResult(@NonNull Class<? extends Activity> target, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, target);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        this.startActivityForResult(intent, requestCode);
        this.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }


    public class OnBackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }


    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化网络请求获取数据
     */
    protected abstract void initAsync();

}
