package com.colin.demo.app.base;

import android.app.Application;
import android.content.Context;

import com.colin.demo.app.utils.ToastUtil;

/**
 * 描述：全局信息处理
 * <p>
 * 作者：Colin
 * 时间：2018/6/20
 */
public class AppApplication extends Application {
    private static Context sContext;

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        sContext = this;
        super.onCreate();
        //Init第三方
        ToastUtil.init(this.getApplicationContext());
    }
}
