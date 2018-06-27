package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Colin on 2017/12/10.
 */

public final class TimeUtil {
    /**
     * 获取当前时间戳格式化字符串
     * yyyy-MM-dd HH:mm:ss:SSS
     *
     * @return
     */
    public static String getTimeString() {
        return getTimeString(System.currentTimeMillis());
    }

    /**
     * 时间戳字符串格式化
     * yyyy-MM-dd HH:mm:ss:SSS
     *
     * @param time
     * @return
     */
    public static String getTimeString(long time) {
        return getTimeString(time, "yyyy-MM-dd HH:mm:ss:SSS");
    }

    /**
     * 当前时间制定格式化成字符串
     *
     * @param formatString
     * @return
     */
    public static String getTimeString(String formatString) {
        return getTimeString(System.currentTimeMillis(), formatString);
    }

    public static String getTimeString(long time, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.CHINA);
        return sdf.format(new Date(time));
    }


    /**
     *
     * @param format    eg:MM、 yy、 dd
     * @return
     */
    public static String getDateString(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static Date getDateByString(String timeString, String fromatString) {
        if (StringUtil.isEmpty(timeString)) {
            return new Date();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromatString, Locale.CHINA);
        try {
            return simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDateFormat(Date date, String fromatString) {
        if (null == date || StringUtil.isEmpty(fromatString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromatString, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 倒计时
     *
     * @param time 毫秒
     * @return
     */
    public static String formatCountTimer(long time) {
        return getTimeString(time, "dd 天 HH: mm: ss");
    }

    /**
     * @param dateString
     * @param fromatString
     * @return
     */
    public static Calendar getCalendar(String dateString, @NonNull String fromatString) {
        Date date = getDateByString(dateString, fromatString);
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 倒计时  秒转化为天小时分秒字符串
     *
     * @param seconds
     * @return String
     */
    @SuppressLint("DefaultLocale")
    public static String formatSeconds(long seconds) {
        String timeStr = String.valueOf(seconds);
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = String.format("%02d:\t\t%02d:\t\t%02d", 0, min, second);
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = String.format("%02d:\t\t%02d:\t\t%02d", hour, min, second);
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = String.format("%d天\t\t%02d:\t\t%02d:\t\t%02d", day, hour, min, second);
                }
            }
        }
        return timeStr;
    }


}
