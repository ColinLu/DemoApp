package com.colin.demo.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * 本地存储数据工具类
 *
 * @author Colin
 */
public class SpUtil {
    private String name = "app_sp";
    private int mode = Context.MODE_PRIVATE;
    private static SharedPreferences.Editor editor = null;
    private static SharedPreferences sharedPreferences = null;

    private SpUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("--SharedPreferencesUtil--cannot be instantiated--");
    }

    private static SharedPreferences getSharedPreferencesObject(Context context) {
        if (sharedPreferences == null) {
            context = context.getApplicationContext();//在 application中使用  报 null
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }

    private static SharedPreferences.Editor getEditorObject(Context context) {
        if (editor == null) {
            context = context.getApplicationContext();
            editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        }
        return editor;
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int get(Context context, String key, int defValue) {
        return getSharedPreferencesObject(context).getInt(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long get(Context context, String key, long defValue) {
        return getSharedPreferencesObject(context).getLong(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Boolean get(Context context, String key, Boolean defValue) {
        return getSharedPreferencesObject(context).getBoolean(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static Boolean get(Context context, String key) {
        return getSharedPreferencesObject(context).getBoolean(key, false);
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String get(Context context, String key, String defValue) {
        return getSharedPreferencesObject(context).getString(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static <T> T get(Context context, String key, Class<T> classOfT) {
        return new Gson().fromJson(getSharedPreferencesObject(context).getString(key, ""), classOfT);
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static Boolean containKey(Context context, String key) {
        return getSharedPreferencesObject(context).contains(key);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, int value) {
        getEditorObject(context).putInt(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, long value) {
        getEditorObject(context).putLong(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Boolean value) {
        getEditorObject(context).putBoolean(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, String value) {
        getEditorObject(context).putString(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @param classOfT
     */
    public static void put(Context context, String key, Object classOfT) {
        if (null == classOfT) {
            return;
        }
        getEditorObject(context).putString(key, new Gson().toJson(classOfT)).apply();
    }

    /**
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        getEditorObject(context).remove(key).apply();
    }

}
