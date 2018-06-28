package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.colin.demo.app.BuildConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/10/12.
 */

public abstract class AppUtil {
    /**
     * android  deviceID 与 imei
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        return getDeviceID(context);
    }

    /**
     * android  deviceID 与 imei
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceID(Context context) {
        String deviceID = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != telephonyManager && null != telephonyManager.getDeviceId()) {
                deviceID = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            LogUtil.e("getImei-->>" + e.getMessage());
            deviceID = getDeviceID();
        }
        return StringUtil.isEmpty(deviceID) ? getDeviceID() : deviceID;
    }

    /**
     * android  deviceID 与 imei
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getSimOperatorName(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null == telephonyManager) {
                return "";
            }
            String simOperatorName = telephonyManager.getSimOperatorName();
            return TextUtils.isEmpty(simOperatorName) ? "" : simOperatorName;
        } catch (Exception e) {
            LogUtil.e("getImei-->>" + e.getMessage());
            return "";
        }
    }

    /**
     * 生成一个 deviceID
     *
     * @return
     */
    public static String getDeviceID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // 随便一个初始化
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取版本名
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        String versionbName = "";
        try {
            PackageManager manager = context.getPackageManager();
            if (null != manager) {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                if (null != info) {
                    versionbName = info.versionName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        }
        return StringUtil.isEmpty(versionbName) ? "" : versionbName;
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager manager = context.getPackageManager();
            if (null != manager) {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                if (null != info) {
                    versionCode = info.versionCode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        }
        return versionCode;
    }

    /**
     * 获取CPu的名字
     *
     * @return
     */
    public static String getCpuName() {
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String text = bufferedReader.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isOpenGps(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static void openGps(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFileSize(File file) {
        long size = 0;
        if (null == file || !file.exists()) {
            return "0KB";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (Exception e) {
            LogUtil.e("Exception-->>" + e.getMessage());
            e.printStackTrace();
        }
        return formatFileSize(size);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 安装app 适配权限7.0
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        if (null == file) {
            LogUtil.e("file为空");
            return;
        }
        if (!file.exists() || null == getApkInfo(context, file.getPath())) {
            ToastUtil.showToast("App安装文件不存在!");
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogUtil.e("高版本版本安装");
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file), "application/vnd.android.package-archive");
        } else {
            LogUtil.e("低版本安装");
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    public static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }

    /**
     * 设置控件是否可以点击
     *
     * @param view
     * @param clickable
     */
    public static void viewClickable(View view, boolean clickable) {
        if (null == view || null == view.getContext()) {
            return;
        }
        view.setClickable(clickable);
        view.setEnabled(clickable);
    }

    public static void setFocusable(View view, boolean focusable) {
        if (null == view || null == view.getContext()) {
            return;
        }
        view.setFocusable(focusable);
        view.setFocusableInTouchMode(focusable);
        if (focusable) {
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    /**
     * 关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 是否主线程
     *
     * @return
     */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 判断进程是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                LogUtil.d("app-->>", appProcess.processName + "前台");
                return false;
            } else {
                LogUtil.d("app-->>", appProcess.processName + "后台");
                return true;
            }
        }
        return false;
    }

    /**
     * 获取WiFI的SSID
     *
     * @return
     */
    @SuppressLint("WifiManagerPotentialLeak")
    public static String getSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (null == wifiManager) {
            return "";
        }
        @SuppressLint("MissingPermission") WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (null == connectionInfo) {
            return "";
        }
        String ssid = connectionInfo.getSSID();
        return TextUtils.isEmpty(ssid) ? "" : ssid;
    }

    /**
     * 获取WiFI的MAC地址
     *
     * @return
     */
    @SuppressLint({"WifiManagerPotentialLeak", "HardwareIds"})
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (null == wifiManager) {
            return "";
        }
        @SuppressLint("MissingPermission") WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (null == connectionInfo) {
            return "";
        }
        String macAddress = connectionInfo.getMacAddress();
        return TextUtils.isEmpty(macAddress) ? "" : macAddress;
    }


    /**
     * 获取网络信息
     *
     * @param context
     * @return
     */

    public static String getNetworkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return "";
        }
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (null == networkInfo || !networkInfo.isConnected()) {
            return "";
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        // TD-SCDMA   networkType is 17
        String strNetworkType = "";
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            String _strSubTypeName = networkInfo.getSubtypeName();
            LogUtil.e("cocos2d-x-->>Network getSubtypeName : " + _strSubTypeName);
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    strNetworkType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    strNetworkType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    strNetworkType = "4G";
                    break;
                default:
                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                        strNetworkType = "3G";
                    } else {
                        strNetworkType = _strSubTypeName;
                    }
                    break;
            }
        }
        return strNetworkType;
    }

    /**
     * 获取屏幕的大小
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getScreenSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return String.format("%d x %d", displayMetrics.heightPixels, displayMetrics.widthPixels);
    }

    /**
     * 获取有哪些app列表
     *
     * @param context
     * @return
     */
    public static List<String> getInstalledApps(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<String> list = new ArrayList<>(packages.size());
        for (int j = 0; j < packages.size(); j++) {
            PackageInfo packageInfo = packages.get(j);
            //显示非系统软件
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                list.add(packageInfo.packageName);
            }
        }
        return list;
    }

    /**
     * 判断app是否存在于手机中
     *
     * @param context
     * @param intent
     * @param packageName
     */
    public static boolean checkApkExist(Context context, Intent intent, String packageName) {
        if (null == context) {
            return false;
        }
        if (null == intent && StringUtil.isEmpty(packageName)) {
            return false;
        }
        //通过Intent进行判断：
        if (null != intent) {
            return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
        }
        //通过应用的包名进行判断
        if (!StringUtil.isEmpty(packageName)) {
            try {
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 得到某一个app
     *
     * @param context
     * @param packageName
     */
    public static ApplicationInfo getApp(Context context, String packageName) {
        if (null == context || StringUtil.isEmpty(packageName)) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 跳转设置界面
     *
     * @param activity
     */
    public static void toSettingView(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    /**
     * 拨打电话界面
     *
     * @param activity
     * @param phone
     */
    public static void toPhoneView(Activity activity, String phone) {
        try {
            Uri uri = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            LogUtil.e("e-->>" + e.getMessage());
        }

    }

    /**
     * 跳转到通知设置界面
     *
     * @param context
     */
    public static void tonNotificationView(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }

    }

    public static String getBuildMessage() {
        StringBuffer stringBuffer = new StringBuffer("Build:" + '\n');
        stringBuffer.append("主板-->>").append(Build.BOARD).append('\n');
        stringBuffer.append("系统定制商-->>").append(Build.BRAND).append('\n');
        if (Build.VERSION.SDK_INT > 21) {
            if (null != Build.SUPPORTED_ABIS && Build.SUPPORTED_ABIS.length > 0) {
                for (String supportedAbi : Build.SUPPORTED_ABIS) {
                    stringBuffer.append("CPU指令集-->>").append(supportedAbi).append('\n');
                }
            }
        }
        stringBuffer.append("设备参数-->>").append(Build.DEVICE).append('\n');
        stringBuffer.append("显示屏参数-->>").append(Build.DISPLAY).append('\n');
        stringBuffer.append("唯一编号-->>").append(Build.FINGERPRINT).append('\n');
        stringBuffer.append("硬件序列号-->>").append(Build.SERIAL).append('\n');
        stringBuffer.append("修订版本列表-->>").append(Build.ID).append('\n');
        stringBuffer.append("硬件制造商-->>").append(Build.MANUFACTURER).append('\n');
        stringBuffer.append("版本-->>").append(Build.MODEL).append('\n');
        stringBuffer.append("硬件名-->>").append(Build.HARDWARE).append('\n');
        stringBuffer.append("手机产品名-->>").append(Build.PRODUCT).append('\n');
        stringBuffer.append("描述Build的标签-->>").append(Build.TAGS).append('\n');
        stringBuffer.append("Builder类型-->>").append(Build.TYPE).append('\n');
        stringBuffer.append("当前开发代号-->>").append(Build.VERSION.CODENAME).append('\n');
        stringBuffer.append("源码控制版本号-->>").append(Build.VERSION.INCREMENTAL).append('\n');
        stringBuffer.append("版本字符串-->>").append(Build.VERSION.RELEASE).append('\n');
        stringBuffer.append("版本号-->>").append(String.valueOf(Build.VERSION.SDK_INT)).append('\n');
        stringBuffer.append("Host值-->>").append(Build.HOST).append('\n');
        stringBuffer.append("User名-->>").append(Build.USER).append('\n');
        stringBuffer.append("设备品牌-->>").append(Build.BRAND).append('\n');
        stringBuffer.append("编译时间-->>").append(TimeUtil.getTimeString(Build.TIME)).append('\n');
        stringBuffer.append("os.name-->>").append(System.getProperty("os.name")).append('\n');
        stringBuffer.append("os.arch-->>").append(System.getProperty("os.arch")).append('\n');
        stringBuffer.append("os.version-->>").append(System.getProperty("os.version"));

        return stringBuffer.toString();
    }

    /**
     * 当前界面 全屏
     * eg:super.onCreate(savedInstanceState); 之前调用
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        if (null == activity) {
            return;
        }
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }
}
