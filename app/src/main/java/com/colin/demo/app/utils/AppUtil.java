package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
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
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.colin.demo.app.BuildConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/10/12.
 */

public abstract class AppUtil {

    /**
     * Build class所有的字段属性
     * Build.BOARD : Z91
     * Build.BOOTLOADER : unknown
     * Build.BRAND : FaDi
     * Build.CPU_ABI : arm64-v8a
     * Build.CPU_ABI2 :
     * Build.DEVICE : Z91
     * Build.DISPLAY : TEST_FaDi_Z91_S100_20180108
     * Build.FINGERPRINT : FaDi/Z91/Z91:7.1.1/N6F26Q/1515397384:user/release-keys
     * Build.HARDWARE : mt6739
     * Build.HOST : 69959bbb90c6
     * Build.ID : N6F26Q
     * Build.IS_DEBUGGABLE : true
     * Build.IS_EMULATOR : false
     * Build.MANUFACTURER : FaDi
     * Build.MODEL : Z91
     * Build.PERMISSIONS_REVIEW_REQUIRED : false
     * Build.PRODUCT : Z91
     * Build.RADIO : unknown
     * Build.SERIAL : 0123456789ABCDEF
     * Build.SUPPORTED_32_BIT_ABIS : [Ljava.lang.String;@305cf5e
     * Build.SUPPORTED_64_BIT_ABIS : [Ljava.lang.String;@f5c1f3f
     * Build.SUPPORTED_ABIS : [Ljava.lang.String;@578b00c
     * Build.TAG : Build
     * Build.TAGS : release-keys
     * Build.TIME : 1515397382000
     * Build.TYPE : user
     * Build.UNKNOWN : unknown
     * Build.USER : FaDi
     * Build.VERSION.ACTIVE_CODENAMES : [Ljava.lang.String;@f4ecd55
     * Build.VERSION.ALL_CODENAMES : [Ljava.lang.String;@bdb836a
     * Build.VERSION.BASE_OS :
     * Build.VERSION.CODENAME : REL
     * Build.VERSION.INCREMENTAL : 1515397384
     * Build.VERSION.PREVIEW_SDK_INT : 0
     * Build.VERSION.RELEASE : 7.1.1
     * Build.VERSION.RESOURCES_SDK_INT : 25
     * Build.VERSION.SDK : 25
     * Build.VERSION.SDK_INT : 25
     * Build.VERSION.SECURITY_PATCH : 2017-11-05
     */
    public static List<String> getAllBuildInformation() {
        List<String> result = new ArrayList<>();
        Field[] fields = Build.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String info = "Build." + field.getName() + " : " + field.get(null);
                LogUtil.d(info);

                result.add(info);
            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }

        Field[] fieldsVersion = Build.VERSION.class.getDeclaredFields();
        for (Field field : fieldsVersion) {
            try {
                field.setAccessible(true);
                String info = "Build.VERSION." + field.getName() + " : " + field.get(null);
                LogUtil.d(info);
                result.add(info);
            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return result;
    }

    public static Map<String, String> getBuildInformation() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                LogUtil.d("Build." + field.getName() + " : " + field.get(null));
                map.put("Build." + field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }

        Field[] fieldsVersion = Build.VERSION.class.getDeclaredFields();
        for (Field field : fieldsVersion) {
            try {
                field.setAccessible(true);
                String info = "Build.VERSION." + field.getName() + " : " + field.get(null);
                LogUtil.d("Build." + field.getName() + " : " + field.get(null));
                map.put("Build.VERSION." + field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return map;
    }

    public static Map<String, String> getWifiInfoInformation() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = WifiInfo.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                LogUtil.d("WifiInfo." + field.getName() + " : " + field.get(null));
                map.put("WifiInfo." + field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return map;
    }

    public static Map<String, String> getNetworkInfoInformation() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = NetworkInfo.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                LogUtil.d("NetworkInfo." + field.getName() + " : " + field.get(null));
                map.put("NetworkInfo." + field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return map;
    }

    public static Map<String, String> getBluetoothAdapterInformation() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = BluetoothAdapter.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                LogUtil.d("BluetoothAdapter." + field.getName() + " : " + field.get(null));
                map.put("BluetoothAdapter." + field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return map;
    }

    public static Map<String, String> getTelephonyManagerInformation() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = TelephonyManager.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                LogUtil.d("TelephonyManager." + field.getName() + " : " + field.get(null));
                map.put("TelephonyManager." + field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
        return map;
    }

    /* /proc/meminfo

    MemTotal:        2902436 kB
    MemFree:          199240 kB
    MemAvailable:    1088764 kB
    Buffers:           40848 kB
    Cached:           862908 kB
    SwapCached:        54696 kB
    Active:          1222848 kB
    Inactive:         671468 kB
    Active(anon):     758516 kB
    Inactive(anon):   242560 kB
    Active(file):     464332 kB
    Inactive(file):   428908 kB
    Unevictable:        5972 kB
    Mlocked:             256 kB
    SwapTotal:       1048572 kB
    SwapFree:         537124 kB
    Dirty:                12 kB
    Writeback:             0 kB
    AnonPages:        988820 kB
    Mapped:           508996 kB
    Shmem:              4800 kB
    Slab:             157204 kB
    SReclaimable:      57364 kB
    SUnreclaim:        99840 kB
    KernelStack:       41376 kB
    PageTables:        51820 kB
    NFS_Unstable:          0 kB
    Bounce:                0 kB
    WritebackTmp:          0 kB
    CommitLimit:     2499788 kB
    Committed_AS:   76292324 kB
    VmallocTotal:   258867136 kB
    VmallocUsed:           0 kB
    VmallocChunk:          0 kB
    CmaTotal:              0 kB
    CmaFree:               0 kB
*/
    public static List<String> getMemInfo() {
        List<String> result = new ArrayList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/proc/meminfo"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * OpenGL ES 版本
     *
     * @param mContext
     * @return
     */
    public static String getOpenGlVersion(Context mContext) {
        return ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context mContext) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        }else {
            totalBlocks = stat.getBlockCount();
        }
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context mContext) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    /**
     * 获得机身ROM总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context mContext) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        }else {
            totalBlocks = stat.getBlockCount();
        }
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用ROM
     *
     * @return
     */
    public static String getRomAvailableSize(Context mContext) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    public static boolean isRooted() {
        // nexus 5x "/su/bin/"
        String[] paths = {"/system/xbin/", "/system/bin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/"};
        try {
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i] + "su";
                if (new File(path).exists()) {
                    String execResult = exec(new String[]{"ls", "-l", path});
                    Log.d("cyb", "isRooted=" + execResult);
                    if (TextUtils.isEmpty(execResult) || execResult.indexOf("root") == execResult.lastIndexOf("root")) {
                        return false;
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String exec(String[] exec) {
        String ret = "";
        ProcessBuilder processBuilder = new ProcessBuilder(exec);
        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ret += line;
            }
            process.getInputStream().close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

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
        if (null == locationManager) {
            return false;
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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

    /**
     * 得到WiFi管理者
     *
     * @param context
     * @return
     */
    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 打开WIFI
     */

    public static void openWifi(Context context) {
        WifiManager mWifiManager = getWifiManager(context);
        if (null == mWifiManager) {
            return;
        }
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(context, "亲，Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "亲，Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
        }
    }

    // 关闭WIFI
    public static void closeWifi(Context context) {
        WifiManager mWifiManager = getWifiManager(context);
        if (null == mWifiManager) {
            return;
        }
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Toast.makeText(context, "亲，Wifi已经关闭，不用再关了", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(context, "亲，Wifi正在关闭，不用再关了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请重新关闭", Toast.LENGTH_SHORT).show();
        }
    }

    // 检查当前WIFI状态
    public static void checkState(Context context) {
        WifiManager mWifiManager = getWifiManager(context);
        if (null == mWifiManager) {
            return;
        }
        if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(context, "Wifi正在关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Toast.makeText(context, "Wifi已经关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(context, "Wifi正在开启", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context, "Wifi已经开启", Toast.LENGTH_SHORT).show();
        } else {//WiFiManager.WIFI_STATE_UNKNOWN
            Toast.makeText(context, "没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 得到当前手机连接的WiFi信息
     *
     * @param context
     * @return
     */
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        if (null == wifiManager) {
            return null;
        }
        return wifiManager.getConnectionInfo();
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取SIM卡运营商
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission,HardwareIds")
    public static String getOperators(Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (null == telephonyManager) {
            LogUtil.e("telephonyManager 空");
            return null;
        }
        return null;

    }
}
