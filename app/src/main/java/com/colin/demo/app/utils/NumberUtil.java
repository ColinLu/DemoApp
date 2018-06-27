package com.colin.demo.app.utils;


import android.support.annotation.NonNull;


/**
 * Created by Colin on 2016/12/29.
 */
public class NumberUtil {
    public static class Holder {
        static NumberUtil instance = new NumberUtil();
    }

    public static NumberUtil getInstance() {
        return Holder.instance;
    }

    private NumberUtil() {
    }

    /**
     * 默认返回 整形值
     *
     * @param value
     * @return
     */
    public Object getIntValue(@NonNull Object value) {
        return getValue(value, 0);
    }

    public Object getDoubleValue(@NonNull Object value) {
        return getValue(value, 0D);
    }

    public Object getFloatValue(@NonNull Object value) {
        return getValue(value, 0F);
    }

    public Object getLongValue(@NonNull Object value) {
        return getValue(value, 0L);
    }

    /**
     * @param value        输入值
     * @param defaultValue 确定返回值类型
     * @return
     */
    public Object getValue(@NonNull Object value, @NonNull Object defaultValue) throws NumberFormatException{
        if (!RegulatorUtil.getInstance().isNumber(String.valueOf(value))) {
            return defaultValue;
        }
        if (defaultValue instanceof Integer && RegulatorUtil.getInstance().isInteger(String.valueOf(value))) {
            return Integer.valueOf(String.valueOf(value));
        } else if (defaultValue instanceof Float) {
            return Float.valueOf(String.valueOf(value));
        } else if (defaultValue instanceof Double) {
            return Double.valueOf(String.valueOf(value));
        } else if (defaultValue instanceof Long && RegulatorUtil.getInstance().isInteger(String.valueOf(value))) {
            return Long.valueOf(String.valueOf(value));
        } else {
            return defaultValue;
        }
    }
}
