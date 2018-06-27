package com.colin.demo.app.utils;

/**
 * 描述：Android 系统工具类
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public class AndroidSystemUtil {
    private AndroidSystemUtil() {
    }


    public static class Holder {
        static AndroidSystemUtil instance = new AndroidSystemUtil();
    }

    public static AndroidSystemUtil getInstance() {
        return AndroidSystemUtil.Holder.instance;
    }

}
