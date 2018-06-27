package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long startTime = 0;
    private static long endTime = 0;
    private int topPadding = 0;

    private ToastUtil() {
        throw new RuntimeException("ToastHelp cannot be initialized!");
    }

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void showToast(String s) {
        showToast(s, Toast.LENGTH_SHORT);
    }

    public static void showToast(int stringRes) {
        showToast(stringRes, Toast.LENGTH_SHORT);
    }

    public static void showToast(String msg, int duration) {
        //空处理
        if (null == toast ||StringUtil.isEmpty(msg)) {
            return;
        }
        //一开始给第一个时间值赋值  Toast
        startTime = System.currentTimeMillis();

        //第一次与第二次toast 在时间范围类
        if (endTime !=0&& startTime - endTime <duration){
            return;
        }
        oldMsg=msg;
        toast.setText(oldMsg);
        toast.setDuration(duration);
        toast.show();
        endTime = System.currentTimeMillis();

    }

    public static void showToast(int stringRes, int duration) {
        //空处理
        if (null == toast || stringRes < 1||StringUtil.isEmpty(String.valueOf(stringRes))) {
            return;
        }
        //一开始给第一个时间值赋值  Toast
        startTime = System.currentTimeMillis();


        //第一次与第二次toast 在时间范围类
        if (endTime !=0&& startTime - endTime <duration){
            return;
        }
        oldMsg=String.valueOf(stringRes);
        toast.setText(stringRes);
        toast.setDuration(duration);
        toast.show();
        endTime = System.currentTimeMillis();

    }

    public static void cancel() {
        toast.cancel();
        startTime = 0;
        endTime = 0;
        oldMsg = null;
    }


}
