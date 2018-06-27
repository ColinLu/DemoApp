package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 字符串工具类
 *
 * @author Colin
 */
public class StringUtil {

    /**
     * View 控件上的文本内容
     *
     * @return
     */
    public static String getText(View view) {
        return getText(view, "");
    }


    /**
     * View 控件上的文本内容
     *
     * @param view
     * @param defaultValues
     * @return
     */
    public static String getText(View view, String defaultValues) {
        String view_string;
        if (view instanceof Button) {
            view_string = ((Button) view).getText().toString().trim();
        } else if (view instanceof AutoCompleteTextView) {
            view_string = ((AutoCompleteTextView) view).getText().toString().trim();
        } else if (view instanceof EditText) {
            view_string = ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView) {
            view_string = ((TextView) view).getText().toString().trim();
        } else {
            view_string = "";
        }
        return StringUtil.isEmpty(view_string) ? defaultValues : view_string;
    }


    public static String getHint(View view) {
        return getHint(view, "");
    }

    private static String getHint(View view, String defaultValues) {
        String view_string;
        if (null != view && view instanceof Button) {
            view_string = ((Button) view).getHint().toString().trim();
        } else if (null != view && view instanceof AutoCompleteTextView) {
            view_string = ((AutoCompleteTextView) view).getHint().toString().trim();
        } else if (null != view && view instanceof EditText) {
            view_string = ((EditText) view).getHint().toString().trim();
        } else if (null != view && view instanceof TextView) {
            view_string = ((TextView) view).getHint().toString().trim();
        } else {
            view_string = "";
        }
        return StringUtil.isEmpty(view_string) ? defaultValues : view_string;
    }

    /**
     * 获取控件描述内容
     *
     * @param view
     * @return
     */
    public static String getContentDescription(View view) {
        return getContentDescription(view, "");
    }

    /**
     * 获取控件描述内容
     *
     * @param view
     * @param defaultValues
     * @return
     */
    public static String getContentDescription(View view, String defaultValues) {
        String view_string;
        if (null != view.getContentDescription()) {
            view_string = view.getContentDescription().toString().trim();
        } else {
            view_string = "";
        }
        return StringUtil.isEmpty(view_string) ? defaultValues : view_string;
    }

    public static boolean isPhone(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String telRegex = "[1]\\d{10}";
        return str.matches(telRegex);
    }


    /**
     * 计算字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    public static long calculateLength(@NonNull CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(@Nullable String input) {
        return null == input || input.trim().length() == 0 || "".equals(input.trim()) || "null".equals(input.trim().toLowerCase());
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 读取 Asset 文件
     *
     * @param context
     * @param assetFileName
     * @return
     */
    public static String getAssetString(Context context, String assetFileName) {
        if (null == context || StringUtil.isEmpty(assetFileName)) {
            return "";
        }
        AssetManager assetManager = context.getAssets();
        if (null == assetManager) {
            return "";
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(assetFileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 得到url 的host
     *
     * @param urlString
     * @return
     */
    public static String getHost(String urlString) {
        if (isEmpty(urlString)) {
            return "";
        }
        URL url;
        try {
            url = new URL(urlString);
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtil.e("url==" + urlString + ":" + e.getMessage());
            return "";
        }
    }

    /**
     * 转化成int
     *
     * @param integerString
     * @return
     */
    public static int toInt(String integerString) {
        return toInt(integerString, 0);
    }

    /**
     * 转化成int
     *
     * @param integerString
     * @return
     */
    public static int toInt(String integerString, int defaultValue) {
        if (isEmpty(integerString)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(integerString);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 转化成int
     *
     * @param doubleString
     * @return
     */
    public static double toDouble(String doubleString) {
        return toDouble(doubleString, 0d);
    }

    /**
     * 转化成int
     *
     * @param doubleString
     * @return
     */
    public static double toDouble(String doubleString, double defaultValue) {
        if (isEmpty(doubleString)) {
            return defaultValue;
        }
        try {
            return Double.valueOf(doubleString);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return defaultValue;
        }
    }


    public static void close(Closeable closeable) {
        if (null == closeable) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            LogUtil.e("close-->>" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void flush(Flushable flushable) {
        if (null == flushable) {
            return;
        }
        try {
            flushable.flush();
        } catch (IOException e) {
            LogUtil.e("flush-->>" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 格式化double
     *
     * @param number
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatLong(double number) {
        try {
            if (number > 100000000) {
                return String.format("%.2f亿", number / 100000000);
            }
            if (number > 10000) {
                return String.format("%.2f万", number / 10000);
            }
            if (number > 0) {
                return String.format("%.0f", number);
            }
            return String.format("%.0f", 0D);
        } catch (Exception e) {
            LogUtil.e("Exception-->>" + e.getMessage());
            return String.format("%.0f", 0D);
        }
    }

    /**
     * 格式化double
     *
     * @param number
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatLongPoint(double number) {
        try {
            if (number > 100000000) {
                return String.format("%.2f亿", number / 100000000);
            }
            if (number > 10000) {
                return String.format("%.2f万", number / 10000);
            }
            if (number > 0) {
                return String.format("%.2f", number);
            }
            return String.format("%.2f", 0D);
        } catch (Exception e) {
            LogUtil.e("Exception-->>" + e.getMessage());
            return String.format("%.2f", 0D);
        }
    }

}
