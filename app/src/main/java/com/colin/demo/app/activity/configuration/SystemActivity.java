package com.colin.demo.app.activity.configuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.adapter.SystemAdapter;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.callback.OnRecyclerItemClickListener;
import com.colin.demo.app.dialog.DialogTips;
import com.colin.demo.app.manager.BlueManager;
import com.colin.demo.app.manager.NetworkManager;
import com.colin.demo.app.manager.WiFiManager;
import com.colin.demo.app.utils.AppUtil;
import com.colin.demo.app.utils.CpuUtil;
import com.colin.demo.app.utils.InitViewUtil;
import com.colin.demo.app.utils.PermissionUtil;
import com.colin.demo.app.utils.StringUtil;
import com.colin.demo.app.utils.TimeUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SystemActivity extends BaseActivity {
    private ItemBean mItemBean;
    private SwipeRefreshLayout refresh_list;
    private RecyclerView recycler_list;
    private List<ItemBean> mList;
    private SystemAdapter mAdapter;

    @Override
    protected void onDestroy() {
        if (null != mList) {
            mList.clear();
        }
        mItemBean = null;
        mList = null;
        mAdapter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_title_refresh_list);
    }

    @Override
    protected void initView() {
        refresh_list = this.findViewById(R.id.refresh_list);
        recycler_list = this.findViewById(R.id.recycler_list);
        InitViewUtil.initSwipeRefreshLayout(refresh_list);
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (null == mList) {
            mList = new ArrayList<>();
        }
        if (null == mAdapter) {
            mAdapter = new SystemAdapter(this, mList);
        }
        InitViewUtil.initRecyclerView(recycler_list, mAdapter);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);
    }


    /**
     * 获取CPU型号
     *
     * @return
     */
    public static String getCpuName() {

        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;

    }

    @Override
    protected void initListener() {
        refresh_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAsync();
            }
        });
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClick(View view, Object object, int position) {
                if (null == object || !(object instanceof ItemBean)) {
                    return;
                }
                ItemBean itemBean = (ItemBean) object;
                if (StringUtil.isEmpty(itemBean.description)) {
                    return;
                }
                new DialogTips(SystemActivity.this).setMessage(itemBean.description).show();
            }
        });
    }

    @Override
    protected void initAsync() {
        recycler_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(true);
            }
        }, 500);
    }

    /**
     * 1、Build.VERSION 各种版本字符串
     * 2、Build.VERSION_CODES 目前已知的版本代码的枚举类
     * 四、常量：UNKNOWN 当一个版本属性不知道时所设定的值。其字符串值为 unknown .
     * 五、构造方法： Build ()
     * 六、静态属性
     * 1、BOARD 主板：The name of the underlying board, like goldfish.
     * 2、BOOTLOADER 系统启动程序版本号：The system bootloader version number.
     * 3、BRAND 系统定制商：The consumer-visible brand with which the product/hardware will be associated, if any.
     * 4、CPU_ABI cpu指令集：The name of the instruction set (CPU type + ABI convention) of native code.
     * 5、CPU_ABI2 cpu指令集2：The name of the second instruction set (CPU type + ABI convention) of native code.
     * 6、DEVICE 设备参数：The name of the industrial design.
     * 7、DISPLAY 显示屏参数：A build ID string meant for displaying to the user
     * 8、FINGERPRINT 唯一识别码：A string that uniquely identifies this build. Do not attempt to parse this value.
     * 9、HARDWARE 硬件名称：The name of the hardware (from the kernel command line or /proc).
     * 10、HOST
     * 11、ID 修订版本列表：Either a changelist number, or a label like M4-rc20.
     * 12、MANUFACTURER 硬件制造商：The manufacturer of the product/hardware.
     * 13、MODEL 版本即最终用户可见的名称：The end-user-visible name for the end product.
     * 14、PRODUCT 整个产品的名称：The name of the overall product.
     * 15、RADIO 无线电固件版本：The radio firmware version number. 在API14后已过时。使用getRadioVersion()代替。
     * 16、SERIAL 硬件序列号：A hardware serial number, if available. Alphanumeric only, case-insensitive.
     * 17、TAGS 描述build的标签,如未签名，debug等等。：Comma-separated tags describing the build, like unsigned,debug.
     * 19、TYPE build的类型：The type of build, like user or eng.
     * 20、USER
     * <p>
     * sb.append("主板： "+ Build.BOARD+"\n");
     * sb.append("系统启动程序版本号： " + Build.BOOTLOADER+"\n");
     * sb.append("系统定制商：" + Build.BRAND+"\n");
     * sb.append("cpu指令集： " + Build.CPU_ABI+"\n");
     * sb.append("cpu指令集2 "+ Build.CPU_ABI2+"\n");
     * sb.append("设置参数： "+ Build.DEVICE+"\n");
     * sb.append("显示屏参数：" + Build.DISPLAY+"\n");
     * sb.append("无线电固件版本：" + Build.getRadioVersion()+"\n");
     * sb.append("硬件识别码：" + Build.FINGERPRINT+"\n");
     * sb.append("硬件名称：" + Build.HARDWARE+"\n");
     * sb.append("HOST: " + Build.HOST+"\n");
     * sb.append("修订版本列表：" + Build.ID+"\n");
     * sb.append("硬件制造商：" + Build.MANUFACTURER+"\n");
     * sb.append("版本：" + Build.MODEL+"\n");
     * sb.append("硬件序列号：" + Build.SERIAL+"\n");
     * sb.append("手机制造商：" + Build.PRODUCT+"\n");
     * sb.append("描述Build的标签：" + Build.TAGS+"\n");
     * sb.append("TIME: " + Build.TIME+"\n");
     * sb.append("builder类型：" + Build.TYPE+"\n");
     * sb.append("USER: " + Build.USER+"\n");
     * <p>
     * <p>
     * static int BASE                      //October 2008: The original, first, version of Android.
     * static int BASE_1_1                  //February 2009: First Android update, officially called 1.1.
     * static int CUPCAKE                   //May 2009: Android 1.5.
     * static int CUR_DEVELOPMENT           //Magic version number for a current development build, which has not yet turned into an official release.
     * static int DONUT                     //September 2009: Android 1.6.
     * static int ECLAIR                    //November 2009: Android 2.0
     * static int ECLAIR_0_1                //December 2009: Android 2.0.1
     * static int ECLAIR_MR1                //January 2010: Android 2.1
     * static int FROYO June                //2010: Android 2.2
     * static int GINGERBREAD               //November 2010: Android 2.3
     * static int GINGERBREAD_MR1           //February 2011: Android 2.3.3.
     * static int HONEYCOMB                 //February 2011: Android 3.0.
     * static int HONEYCOMB_MR1             //May 2011: Android 3.1.
     * static int HONEYCOMB_MR2             //June 2011: Android 3.2.
     * static int ICE_CREAM_SANDWICH        //October 2011: Android 4.0.
     * static int ICE_CREAM_SANDWICH_MR1    //December 2011: Android 4.0.3.
     * static int JELLY_BEAN                //June 2012: Android 4.1.
     * static int JELLY_BEAN_MR1            //Android 4.2: Moar jelly beans!
     *
     * @param refresh
     */
    @SuppressLint({"MissingPermission", "HardwareIds", "DefaultLocale"})
    private void loadData(boolean refresh) {
        if (null != refresh_list && refresh_list.isRefreshing()) {
            refresh_list.setRefreshing(false);
        }

        if (isDestroy) {
            return;
        }
        //定位
        if (!AppUtil.isOpenGps(this)) {
            ToastUtil.showToast("请打开定位功能");
            return;
        }
        //权限
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(PermissionUtil.PERMISSIONS_LOCATION));
        list.addAll(Arrays.asList(PermissionUtil.PERMISSIONS_PHONE));
        if (!PermissionUtil.getInstance().checkPermission(this, list, PermissionUtil.REQUEST_CODE_LOCATION)) {
            return;
        }

        mList.clear();
        mList.add(new ItemBean("ANDROID_ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), ""));

        ///////////////////////////////////////////////////Build取值开始///////////////////////////////////////////////////////////////////
        List<String> stringList = AppUtil.getAllBuildInformation();
        mList.add(new ItemBean("设备ID", Build.ID, "Either a changelist number, or a label like \"M4-rc20\""));
        mList.add(new ItemBean("设备类型", Build.TYPE, "The type of build, like \"user\" or \"eng\"."));
        mList.add(new ItemBean("描述build的标签", Build.TAGS, "Comma-separated tags describing the build, like \"unsigned,debug\"."));

        mList.add(new ItemBean("串口序列号", Build.SERIAL, "被getSerial()方法替代"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mList.add(new ItemBean("串口序列号", Build.getSerial(), "方法获取需要权限--READ_PHONE_STATE"));
        } else {
            mList.add(new ItemBean("串口序列号", getString(R.string.version_codes_error_format, Build.VERSION_CODES.O), "方法获取需要权限--READ_PHONE_STATE"));
        }

        mList.add(new ItemBean("品牌", Build.BRAND, "品牌手机如果有的话就有值"));
        mList.add(new ItemBean("设备名", Build.DEVICE, "The name of the industrial design."));
        mList.add(new ItemBean("硬件识别码", Build.FINGERPRINT, "唯一标识此构建的字符串。不要试图解析这个值。"));
        mList.add(new ItemBean("硬件名称", Build.HARDWARE, "The name of the hardware (from the kernel command line or /proc)."));

        mList.add(new ItemBean("固件版本", Build.RADIO, "The radio firmware version number. getRadioVersion} instead"));
        mList.add(new ItemBean("固件版本", Build.getRadioVersion(), "The radio firmware version number. getRadioVersion} instead"));

        mList.add(new ItemBean("系统启动程序版本号", Build.BOOTLOADER, "The system bootloader version number."));
        mList.add(new ItemBean("CPU名称", getCpuName(), ""));
        mList.add(new ItemBean("cpu指令集1", Build.CPU_ABI, "{@link #SUPPORTED_ABIS} instead"));
        mList.add(new ItemBean("cpu指令集2", Build.CPU_ABI2, "{@link #SUPPORTED_ABIS} instead"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mList.add(new ItemBean("cpu指令集", arrayToString(Build.SUPPORTED_ABIS), "{@link #SUPPORTED_ABIS} instead"));
        } else {
            mList.add(new ItemBean("cpu指令集", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), "{@link #SUPPORTED_ABIS} instead"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mList.add(new ItemBean("cpu指令集32", arrayToString(Build.SUPPORTED_32_BIT_ABIS), "{@link #SUPPORTED_ABIS} instead"));
        } else {
            mList.add(new ItemBean("cpu指令集32", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), "{@link #SUPPORTED_ABIS} instead"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mList.add(new ItemBean("cpu指令集64", arrayToString(Build.SUPPORTED_64_BIT_ABIS), "{@link #SUPPORTED_ABIS} instead"));
        } else {
            mList.add(new ItemBean("cpu指令集64", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), "{@link #SUPPORTED_ABIS} instead"));
        }

        mList.add(new ItemBean("主板", Build.BOARD, "The name of the underlying board, like \"goldfish\"."));
        mList.add(new ItemBean("版本型号", Build.MODEL, "The end-user-visible name for the end product. "));
        mList.add(new ItemBean("手机制造商", Build.PRODUCT, "The name of the overall product."));
        mList.add(new ItemBean("制造商", Build.MANUFACTURER, "The manufacturer of the product/hardware. "));
        mList.add(new ItemBean("硬件名称", Build.HARDWARE, " The name of the hardware (from the kernel command line or /proc). "));
        mList.add(new ItemBean("显示屏幕参数", Build.DISPLAY, "A build ID string meant for displaying to the user "));


        mList.add(new ItemBean("USER", Build.USER, "The following properties only make sense for internal engineering builds."));
        mList.add(new ItemBean("HOST", Build.HOST, "The following properties only make sense for internal engineering builds."));
        mList.add(new ItemBean("TIME", TimeUtil.getTimeString(Build.TIME), "The following properties only make sense for internal engineering builds."));


        mList.add(new ItemBean("VERSION_RELEASE", Build.VERSION.RELEASE, "The user-visible version string.  E.g., \"1.0\" or \"3.4b5\""));
        mList.add(new ItemBean("VERSION_SDK", Build.VERSION.SDK, "The user-visible SDK version of the framework in its raw String representation; use {@link #SDK_INT} instead."));
        mList.add(new ItemBean("VERSION_CODENAME", Build.VERSION.CODENAME, "The current development codename, or the string \"REL\" if this is a release build"));
        mList.add(new ItemBean("VERSION_INCREMENTAL", Build.VERSION.INCREMENTAL, "The internal value used by the underlying source control to represent this build.  E.g., a perforce changelist number or a git hash."));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("VERSION_PATCH", Build.VERSION.SECURITY_PATCH, "The user-visible security patch level."));
        } else {
            mList.add(new ItemBean("VERSION_PATCH", getString(R.string.version_codes_error_format, Build.VERSION_CODES.M), "The user-visible security patch level."));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("VERSION_BASE_OS", Build.VERSION.BASE_OS, "The base OS build the product is based on."));
        } else {
            mList.add(new ItemBean("VERSION_BASE_OS", getString(R.string.version_codes_error_format, Build.VERSION_CODES.M), "The base OS build the product is based on."));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("系统版本值", String.valueOf(Build.VERSION.PREVIEW_SDK_INT), "The developer preview revision of a prerelease SDK. This value will always be <code>0</code> on production platform builds/devices."));
        } else {
            mList.add(new ItemBean("系统版本值", getString(R.string.version_codes_error_format, Build.VERSION_CODES.M), "The developer preview revision of a prerelease SDK. This value will always be <code>0</code> on production platform builds/devices."));
        }
        mList.add(new ItemBean("系统版本值", String.valueOf(Build.VERSION.SDK_INT), "The user-visible SDK version of the framework; its possible values are defined in {@link Build.VERSION_CODES}"));

        ///////////////////////////////////////////////////Build取值结束///////////////////////////////////////////////////////////////////

        //最好获取权限 READ_PHONE_STATE
        //https://blog.csdn.net/perArther/article/details/51772561
        ///////////////////////////////////////////////////手机TelephonyManager取值开始///////////////////////////////////////////////////////////////////
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        mList.add(new ItemBean("DeviceId", null == telephonyManager ? "" : telephonyManager.getDeviceId(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
        mList.add(new ItemBean("SubscriberId", null == telephonyManager ? "" : telephonyManager.getSubscriberId(), "Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable."));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mList.add(new ItemBean("移动终端的唯一标识", null == telephonyManager ? "" : telephonyManager.getImei(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
        } else {
            mList.add(new ItemBean("移动终端的唯一标识", getString(R.string.version_codes_error_format, Build.VERSION_CODES.O), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mList.add(new ItemBean("移动终端的唯一标识", null == telephonyManager ? "" : telephonyManager.getMeid(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
        } else {
            mList.add(new ItemBean("移动终端的唯一标识", getString(R.string.version_codes_error_format, Build.VERSION_CODES.O), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
        }
        mList.add(new ItemBean("移动终端的软件版本", null == telephonyManager ? "" : telephonyManager.getDeviceSoftwareVersion(), "返回移动终端的软件版本，例如：GSM手机的IMEI/SV码"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mList.add(new ItemBean("GSM手机的一组标识符", null == telephonyManager ? "" : telephonyManager.getGroupIdLevel1(), "Returns the Group Identifier Level1 for a GSM phone. Return null if it is unavailable"));
        } else {
            mList.add(new ItemBean("GSM手机的一组标识符", getString(R.string.version_codes_error_format, Build.VERSION_CODES.O), "Returns the Group Identifier Level1 for a GSM phone. Return null if it is unavailable"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mList.add(new ItemBean("短信代理商的URL", null == telephonyManager ? "" : telephonyManager.getMmsUAProfUrl(), "the MMS user agent profile URL"));
        } else {
            mList.add(new ItemBean("短信代理商的URL", getString(R.string.version_codes_error_format, Build.VERSION_CODES.O), "the MMS user agent profile URL"));
        }
        mList.add(new ItemBean("ISO标准的国家码", null == telephonyManager ? "" : telephonyManager.getNetworkCountryIso(), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code)"));
        mList.add(new ItemBean("电话类型", getPhoneType(null == telephonyManager ? TelephonyManager.PHONE_TYPE_NONE : telephonyManager.getPhoneType()), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code)"));

        mList.add(new ItemBean("MCC+MNC代码", null == telephonyManager ? "" : telephonyManager.getNetworkOperator(), "(SIM卡运营商国家代码和运营商网络代码)(IMSI);Returns the numeric name (MCC+MNC) of current registered operator."));

        mList.add(new ItemBean("移动网络运营商", null == telephonyManager ? "" : telephonyManager.getNetworkOperatorName(), "Returns the alphabetic name of current registered operator."));
        mList.add(new ItemBean("CC+SIM卡运营商的名称", null == telephonyManager ? "" : telephonyManager.getSimOperatorName(), "Returns the Service Provider Name (SPN)."));
        mList.add(new ItemBean("CC+MNC代码", null == telephonyManager ? "" : telephonyManager.getSimOperator(), "Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits."));
        mList.add(new ItemBean("SIM卡的状态", getSimState(null == telephonyManager ? TelephonyManager.SIM_STATE_UNKNOWN : telephonyManager.getSimState()), "Returns the alphabetic name of current registered operator."));
        mList.add(new ItemBean("获取网络类型", getNetworkType(null == telephonyManager ? TelephonyManager.NETWORK_TYPE_UNKNOWN : telephonyManager.getNetworkType()), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code) of a subscription."));

        mList.add(new ItemBean("CC+SIM卡运营商的国家代码", null == telephonyManager ? "" : telephonyManager.getSimCountryIso(), "Returns the ISO country code equivalent for the SIM provider's country code."));
        mList.add(new ItemBean("SIM卡的序号", null == telephonyManager ? "" : telephonyManager.getSimSerialNumber(), "Returns the serial number of the SIM, if applicable. Return null if it is unavailable."));
        mList.add(new ItemBean("语音信箱的检索字母标识符", null == telephonyManager ? "" : telephonyManager.getVoiceMailAlphaTag(), " Retrieves the alphabetic identifier associated with the voice mail number."));


        mList.add(new ItemBean("获取设备的当前位置", null == telephonyManager ? "" : telephonyManager.getCellLocation().toString(), "#getAllCellInfo} instead"));
        //ACCESS_COARSE_LOCATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mList.add(new ItemBean("AllCellInfo", listToString(null == telephonyManager ? null : telephonyManager.getAllCellInfo()), "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available."));
        } else {
            mList.add(new ItemBean("AllCellInfo", getString(R.string.version_codes_error_format, Build.VERSION_CODES.JELLY_BEAN_MR1), "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available."));
        }
        mList.add(new ItemBean("手机号码", null == telephonyManager ? "" : telephonyManager.getLine1Number(), "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available."));
        mList.add(new ItemBean("IMSI", null == telephonyManager ? "" : telephonyManager.getSubscriberId(), "Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable."));
        mList.add(new ItemBean("数据活动状态", getPhoneActivityState(null == telephonyManager ? TelephonyManager.DATA_ACTIVITY_NONE : telephonyManager.getDataActivity()), "Returns a constant indicating the type of activity on a data connection"));
        mList.add(new ItemBean("数据连接状态", getPhoneDataState(null == telephonyManager ? -1 : telephonyManager.getDataState()), "Returns a constant indicating the type of activity on a data connection"));


        ///////////////////////////////////////////////////手机TelephonyManager取值结束///////////////////////////////////////////////////////////////////
        //https://blog.csdn.net/jdsjlzx/article/details/40740543
        //https://www.jianshu.com/p/67aaf1fdb921
        ///////////////////////////////////////////////////WifiManager取值开始///////////////////////////////////////////////////////////////////
        if (null != WiFiManager.getInstance().getWifiManager(this)) {
            mList.add(new ItemBean("WiFi是否打开", String.valueOf(WiFiManager.getInstance().isWifiEnabled(this)), ""));
            mList.add(new ItemBean("WiFi状态", WiFiManager.getInstance().getWifiStateString(this), ""));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("是否支持5GWifi", String.valueOf(WiFiManager.getInstance().getWifiManager(this).is5GHzBandSupported()), "true if this adapter supports 5 GHz band"));
            } else {
                mList.add(new ItemBean("是否支持5GWifi", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), "true if this adapter supports 5 GHz band"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("是否支持ApRtt", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isDeviceToApRttSupported()), " true if this adapter supports Device-to-AP RTT"));
            } else {
                mList.add(new ItemBean("是否支持ApRtt", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), " true if this adapter supports Device-to-AP RTT"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("isEnhancedPowerReportingSupported", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isEnhancedPowerReportingSupported()), " true if this adapter supports advanced power/performance counters"));
            } else {
                mList.add(new ItemBean("isEnhancedPowerReportingSupported", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), " true if this adapter supports advanced power/performance counters"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("isP2pSupported", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isP2pSupported()), ""));
            } else {
                mList.add(new ItemBean("isP2pSupported", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), ""));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("isPreferredNetworkOffloadSupported", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isPreferredNetworkOffloadSupported()), ""));
            } else {
                mList.add(new ItemBean("isPreferredNetworkOffloadSupported", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), ""));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mList.add(new ItemBean("isScanAlwaysAvailable", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isScanAlwaysAvailable()), ""));
            } else {
                mList.add(new ItemBean("isScanAlwaysAvailable", getString(R.string.version_codes_error_format, Build.VERSION_CODES.JELLY_BEAN_MR2), ""));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mList.add(new ItemBean("isTdlsSupported", String.valueOf(WiFiManager.getInstance().getWifiManager(this).isTdlsSupported()), "true if this adapter supports Tunnel Directed Link Setup"));
            } else {
                mList.add(new ItemBean("isTdlsSupported", getString(R.string.version_codes_error_format, Build.VERSION_CODES.LOLLIPOP), "true if this adapter supports Tunnel Directed Link Setup"));
            }

            mList.add(new ItemBean("WiFiDHCP的信息", WiFiManager.getInstance().getWifiManager(this).getDhcpInfo().toString(), ""));
            mList.add(new ItemBean("网络连接的状态", listToString(WiFiManager.getInstance().getWifiManager(this).getConfiguredNetworks()), ""));
        } else {
            mList.add(new ItemBean("WiFi", "不支持WiFi", ""));
        }
        //用于描述已经链接的Wifi的信息
        WifiInfo wifiInfo = WiFiManager.getInstance().getWifiInfo(this);
        if (null != wifiInfo) {
            mList.add(new ItemBean("IP地址", String.valueOf(wifiInfo.getIpAddress()), ""));
            mList.add(new ItemBean("Mac地址", wifiInfo.getMacAddress(), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("SSID是否被隐藏", String.valueOf(wifiInfo.getHiddenSSID()), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("BSSID", wifiInfo.getBSSID(), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("SSID信息", wifiInfo.getSSID(), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("链接的速度", String.valueOf(wifiInfo.getLinkSpeed()), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("WiFi强度", String.valueOf(wifiInfo.getRssi()), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("getNetworkId", String.valueOf(wifiInfo.getNetworkId()), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("网络链接的状态", wifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()).toString(), "Returns a constant indicating the type of activity on a data connection"));
        } else {
            mList.add(new ItemBean("WiFi连接", "未连接", ""));
        }

        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = NetworkManager.getInstance().getNetworkInfo(this);
        if (null != networkInfo) {
            mList.add(new ItemBean("网络是否有效", String.valueOf(networkInfo.isAvailable()), ""));
            mList.add(new ItemBean("网络是否连接", String.valueOf(networkInfo.isConnected()), ""));
            mList.add(new ItemBean("网络类型", String.valueOf(networkInfo.getType()), ""));
            mList.add(new ItemBean("网络类型的名", networkInfo.getTypeName(), ""));
            mList.add(new ItemBean("精确的网络状态", NetworkManager.getInstance().getDetailedState(this).name(), ""));
            mList.add(new ItemBean("粗略的网络状态", NetworkManager.getInstance().getState(this).name(), ""));
            mList.add(new ItemBean("networkInfo", networkInfo.toString(), ""));
        } else {
            mList.add(new ItemBean("网络连接", "未连接网络", ""));
        }
        Network network = NetworkManager.getInstance().getNetwork(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("network", null == network ? "" : network.toString(), ""));
        } else {
            mList.add(new ItemBean("network", getString(R.string.version_codes_error_format, Build.VERSION_CODES.M), ""));
        }
        // 方法1
        Display display = getWindowManager().getDefaultDisplay();
        mList.add(new ItemBean("Height", String.valueOf(display.getHeight()), ""));
        mList.add(new ItemBean("Width", String.valueOf(display.getWidth()), ""));

        // 方法2
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mList.add(new ItemBean("HeightPixels", String.valueOf(displayMetrics.heightPixels), ""));
        mList.add(new ItemBean("WidthPixels", String.valueOf(displayMetrics.widthPixels), ""));
        mList.add(new ItemBean("scaledDensity", String.valueOf(displayMetrics.scaledDensity), ""));
        mList.add(new ItemBean("densityDpi", String.valueOf(displayMetrics.densityDpi), ""));
        mList.add(new ItemBean("density", String.valueOf(displayMetrics.density), ""));
        mList.add(new ItemBean("xdpi", String.valueOf(displayMetrics.xdpi), ""));
        mList.add(new ItemBean("ydpi", String.valueOf(displayMetrics.ydpi), ""));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mList.add(new ItemBean("蓝牙名称", BlueManager.getInstance().getBluetoothName(this), ""));
            mList.add(new ItemBean("蓝牙地址", BlueManager.getInstance().getBluetoothAddress(this), ""));
            mList.add(new ItemBean("蓝牙开关状态", BlueManager.getInstance().getBluetoothStateString(this), ""));
            mList.add(new ItemBean("蓝牙扫描状态", BlueManager.getInstance().getBluetoothScanModeString(this), ""));
            mList.add(new ItemBean("蓝牙设备绑定", listToString(BlueManager.getInstance().getBluetoothBondedDevices(this)), ""));
        } else {
            mList.add(new ItemBean("蓝牙设备", getString(R.string.version_codes_error_format, Build.VERSION_CODES.M), ""));
        }

        mList.add(new ItemBean("Cpu信息", arrayToString(CpuUtil.getCpuInfo().toArray(new String[CpuUtil.getCpuInfo().size()])), ""));
        mList.add(new ItemBean("Cpu核数", String.valueOf(CpuUtil.getCPUCoreNum()), ""));
        mList.add(new ItemBean("Cpu核数", String.valueOf(CpuUtil.getNumCpuCores()), ""));
        mList.add(new ItemBean("Cpu最高频率", String.valueOf(CpuUtil.getCpuMaxFreq()), ""));
        mList.add(new ItemBean("Cpu最低频率", String.valueOf(CpuUtil.getCpuMinFreq()), ""));

        mList.add(new ItemBean("SD卡总大小", AppUtil.getSDTotalSize(this), "获得SD卡总大小"));
        mList.add(new ItemBean("SD可用大小", AppUtil.getSDAvailableSize(this), "获得sd卡剩余容量，即可用大小"));
        mList.add(new ItemBean("ROM总大小", AppUtil.getRomTotalSize(this), "获得机身ROM总大小"));
        mList.add(new ItemBean("ROM可用", AppUtil.getRomAvailableSize(this), "获得机身可用ROM"));
        mList.add(new ItemBean("是否Root", String.valueOf(AppUtil.isRooted()), "nexus 5x \"/su/bin/\""));


        mAdapter.notifyDataSetChanged();


    }


    /**
     * WIFI_STATE_DISABLING  WIFI网卡正在关闭  0
     * WIFI_STATE_DISABLED   WIFI网卡不可用  1
     * WIFI_STATE_ENABLING    WIFI网卡正在打开  2
     * WIFI_STATE_ENABLED     WIFI网卡可用  3
     * WIFI_STATE_UNKNOWN    WIFI网卡状态不可知 4
     *
     * @param state
     * @return
     */
    private String getWifiState(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_DISABLING:
                return "WIFI网卡正在关闭";
            case WifiManager.WIFI_STATE_DISABLED:
                return "WIFI网卡不可用";
            case WifiManager.WIFI_STATE_ENABLING:
                return "WIFI网卡正在打开";
            case WifiManager.WIFI_STATE_ENABLED:
                return "WIFI网卡可用";
            case WifiManager.WIFI_STATE_UNKNOWN:
                return "WIFI网卡状态不可知";
            default:
                return "WIFI网卡状态不可知";
        }
    }

    /**
     * Wifi强度
     *
     * @param level
     * @return
     */
    private String getWifiLevel(int level) {
        String levelStr = "无信号";
        if (level <= 0 && level >= -50) {
            levelStr = "信号最好";
        } else if (level < -50 && level >= -70) {
            levelStr = "信号较好";
        } else if (level < -70 && level >= -80) {
            levelStr = "信号一般";
        } else if (level < -80 && level >= -100) {
            levelStr = "信号差";
        }
        return "信号强度：" + levelStr;
    }

    /**
     * 数组转字符串  换行
     *
     * @param array
     * @return
     */
    private String arrayToString(String[] array) {
        if (null == array || array.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : array) {
            stringBuilder.append(value).append('\n');
        }
        return stringBuilder.toString().trim();
    }

    /**
     * 数组转字符串  换行
     *
     * @param list
     * @return
     */
    private String listToString(Collection<? extends Parcelable> list) {
        if (null == list || list.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Parcelable parcelable : list) {
            stringBuilder.append(parcelable.toString()).append('\n');
        }
        return stringBuilder.toString().trim();
    }

    /**
     * 获取数据活动状态
     * 0:   DATA_ACTIVITY_NON     数据连接状态：活动，无数据发送和接受
     * 1:   DATA_ACTIVITY_IN      数据连接状态：活动，正在接受数据
     * 2:   DATA_ACTIVITY_OUT     数据连接状态：活动，正在发送数据
     * 3:   DATA_ACTIVITY_INOUT   数据连接状态：活动，正在接受和发送数据
     * 4:   DATA_ACTIVITY_DORMANT 睡眠模式
     */
    private String getPhoneActivityState(int state) {
        switch (state) {
            case TelephonyManager.DATA_ACTIVITY_NONE:
                return "无数据发送和接受";
            case TelephonyManager.DATA_ACTIVITY_IN:
                return "正在接受数据";
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return "正在发送数据";
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return "正在接受和发送数据";
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                return "睡眠模式";
            default:
                return "无数据发送和接受";
        }
    }

    /**
     * 获取电话类型
     * 0:  PHONE_TYPE_NONE     无类型
     * 1:  PHONE_TYPE_GSM      全球移动通信系统
     * 2:  PHONE_TYPE_CDMA     码分多址
     * 3:  PHONE_TYPE_SIP      跨越因特网的高级电话业务
     *
     * @param type
     * @return
     */
    private String getPhoneType(int type) {
        switch (type) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return "无类型";
            case TelephonyManager.PHONE_TYPE_GSM:
                return "全球移动通信系统";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "码分多址";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "跨越因特网的高级电话业务";
            default:
                return "无类型";
        }
    }

    /**
     * 获取SIM卡的状态:
     * 0:   SIM_STATE_UNKNOWN          未知
     * 1:   SIM_STATE_ABSENT           没有可用的卡
     * 2:   SIM_STATE_PIN_REQUIRED     锁定：需要用户的卡销解锁
     * 3:   SIM_STATE_PUK_REQUIRED     锁定：要求用户的SIM卡PUK码解锁
     * 4:   SIM_STATE_NETWORK_LOCKED   锁定:要求网络销解锁
     * 5:   SIM_STATE_READY            准备完毕
     *
     * @param state
     * @return
     */
    private String getSimState(int state) {
        switch (state) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return "未知";
            case TelephonyManager.SIM_STATE_ABSENT:
                return "没有可用的卡";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return "锁定：需要用户的卡销解锁";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return "锁定：要求用户的SIM卡PUK码解锁";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return "锁定:要求网络销解锁";
            case TelephonyManager.SIM_STATE_READY:
                return "准备完毕";
            default:
                return "未知";
        }
    }

    /**
     * 0:   DATA_DISCONNECTED   数据连接状态：断开
     * 1:   DATA_CONNECTING     数据连接状态：正在连接
     * 2:   DATA_CONNECTED      数据连接状态：已连接
     * 3:   DATA_SUSPENDED      数据连接状态：暂停
     *
     * @param state
     * @return
     */
    private String getPhoneDataState(int state) {
        switch (state) {
            case TelephonyManager.DATA_DISCONNECTED:
                return "断开";
            case TelephonyManager.DATA_CONNECTING:
                return "正在连接";
            case TelephonyManager.DATA_CONNECTED:
                return "已连接";
            case TelephonyManager.DATA_SUSPENDED:
                return "暂停";
            default:
                return "未知状态";
        }
    }

    /**
     * * 获取网络类型
     * NETWORK_TYPE_UNKNOWN = 0;
     * NETWORK_TYPE_GPRS = 1;       2G
     * NETWORK_TYPE_EDGE = 2;       2G
     * NETWORK_TYPE_UMTS = 3;       3G
     * NETWORK_TYPE_CDMA = 4;       2G
     * NETWORK_TYPE_EVDO_0 = 5;     3G
     * NETWORK_TYPE_EVDO_A = 6;     3G
     * NETWORK_TYPE_1xRTT = 7;      2G
     * NETWORK_TYPE_HSDPA = 8;      3G
     * NETWORK_TYPE_HSUPA = 9;      3G
     * NETWORK_TYPE_HSPA = 10;      3G
     * NETWORK_TYPE_IDEN = 11;      2G
     * NETWORK_TYPE_EVDO_B = 12;    3G
     * NETWORK_TYPE_LTE = 13;       4G
     * NETWORK_TYPE_EHRPD = 14;     3G
     * NETWORK_TYPE_HSPAP = 15;     3G
     * NETWORK_TYPE_GSM = 16;       2G
     * NETWORK_TYPE_TD_SCDMA = 17;  3G
     * NETWORK_TYPE_IWLAN = 18;     4G
     *
     * @param type
     * @return
     */
    private String getNetworkType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "2G-GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "2G-EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G-UMTS";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "2G-CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "3G-CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "3G-CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "2G-CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G-HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G-HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G-HSPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G-IDEN";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "3G-CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G-LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "3G-CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G-HSPA+";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "2G-GSM";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "3G-TD_SCDMA";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "4G-IWLAN";
            case 19:
                return "4G-LTE_CA";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionUtil.getInstance().onRequestPermissionsResult(PermissionUtil.REQUEST_CODE_LOCATION, grantResults)) {
            loadData(true);
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
