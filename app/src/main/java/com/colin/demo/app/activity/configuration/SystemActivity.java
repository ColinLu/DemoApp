package com.colin.demo.app.activity.configuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellInfo;
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
import com.colin.demo.app.utils.AppUtil;
import com.colin.demo.app.utils.InitViewUtil;
import com.colin.demo.app.utils.PermissionUtil;
import com.colin.demo.app.utils.TimeUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                new DialogTips(SystemActivity.this).setMessage(((ItemBean) object).description).show();
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
    @SuppressLint({"MissingPermission", "HardwareIds"})
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
        ///////////////////////////////////////////////////Build取值开始///////////////////////////////////////////////////////////////////
        mList.add(new ItemBean("设备ID", Build.ID, "Either a changelist number, or a label like \"M4-rc20\""));
        mList.add(new ItemBean("设备类型", Build.TYPE, "The type of build, like \"user\" or \"eng\"."));
        mList.add(new ItemBean("描述build的标签", Build.TAGS, "Comma-separated tags describing the build, like \"unsigned,debug\"."));
        mList.add(new ItemBean("串口序列号", Build.SERIAL, "被getSerial()方法替代"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mList.add(new ItemBean("串口序列号", Build.getSerial(), "方法获取需要权限--READ_PHONE_STATE"));
        }

        mList.add(new ItemBean("品牌", Build.BRAND, "品牌手机如果有的话就有值"));
        mList.add(new ItemBean("设备名", Build.DEVICE, "The name of the industrial design."));
        mList.add(new ItemBean("硬件识别码", Build.FINGERPRINT, "唯一标识此构建的字符串。不要试图解析这个值。"));
        mList.add(new ItemBean("硬件名称", Build.HARDWARE, "The name of the hardware (from the kernel command line or /proc)."));

        mList.add(new ItemBean("固件版本", Build.RADIO, "The radio firmware version number. getRadioVersion} instead"));
        mList.add(new ItemBean("固件版本", Build.getRadioVersion(), "The radio firmware version number. getRadioVersion} instead"));

        mList.add(new ItemBean("系统启动程序版本号", Build.BOOTLOADER, "The system bootloader version number."));
        mList.add(new ItemBean("cpu指令集1", Build.CPU_ABI, "{@link #SUPPORTED_ABIS} instead"));
        mList.add(new ItemBean("cpu指令集2", Build.CPU_ABI2, "{@link #SUPPORTED_ABIS} instead"));
        mList.add(new ItemBean("cpu指令集", arrayToString(Build.SUPPORTED_ABIS), "{@link #SUPPORTED_ABIS} instead"));
        mList.add(new ItemBean("cpu指令集32", arrayToString(Build.SUPPORTED_32_BIT_ABIS), "{@link #SUPPORTED_ABIS} instead"));
        mList.add(new ItemBean("cpu指令集64", arrayToString(Build.SUPPORTED_64_BIT_ABIS), "{@link #SUPPORTED_ABIS} instead"));

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
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("VERSION_BASE_OS", Build.VERSION.BASE_OS, "The base OS build the product is based on."));
        }
        mList.add(new ItemBean("系统版本值", String.valueOf(Build.VERSION.SDK_INT), "The user-visible SDK version of the framework; its possible values are defined in {@link Build.VERSION_CODES}"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.add(new ItemBean("系统版本值", String.valueOf(Build.VERSION.PREVIEW_SDK_INT), "  * The developer preview revision of a prerelease SDK. This value will always be <code>0</code> on production platform builds/devices."));
        }
        ///////////////////////////////////////////////////Build取值结束///////////////////////////////////////////////////////////////////

        //最好获取权限 READ_PHONE_STATE
        //https://blog.csdn.net/perArther/article/details/51772561
        ///////////////////////////////////////////////////手机取值开始///////////////////////////////////////////////////////////////////
        TelephonyManager phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (null != phone) {

            mList.add(new ItemBean("移动终端的唯一标识", phone.getDeviceId(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
            mList.add(new ItemBean("GSM手机的IMSI", phone.getSubscriberId(), "Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable."));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mList.add(new ItemBean("移动终端的唯一标识", phone.getImei(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mList.add(new ItemBean("移动终端的唯一标识", phone.getMeid(), "Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones. Return null if device ID is not available."));
            }
            mList.add(new ItemBean("移动终端的软件版本", phone.getDeviceSoftwareVersion(), "返回移动终端的软件版本，例如：GSM手机的IMEI/SV码"));
            mList.add(new ItemBean("GSM手机的一组标识符", phone.getGroupIdLevel1(), "Returns the Group Identifier Level1 for a GSM phone. Return null if it is unavailable"));
            mList.add(new ItemBean("短信代理商的URL", phone.getMmsUAProfUrl(), "the MMS user agent profile URL"));
            mList.add(new ItemBean("ISO标准的国家码", phone.getNetworkCountryIso(), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code)"));
            mList.add(new ItemBean("电话类型", getPhoneType(phone.getPhoneType()), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code)"));

            mList.add(new ItemBean("MCC+MNC代码", phone.getNetworkOperator(), "(SIM卡运营商国家代码和运营商网络代码)(IMSI);Returns the numeric name (MCC+MNC) of current registered operator."));

            mList.add(new ItemBean("移动网络运营商", phone.getNetworkOperatorName(), "Returns the alphabetic name of current registered operator."));
            mList.add(new ItemBean("CC+SIM卡运营商的名称", phone.getSimOperatorName(), "Returns the Service Provider Name (SPN)."));
            mList.add(new ItemBean("CC+MNC代码", phone.getSimOperator(), "Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits."));
            mList.add(new ItemBean("SIM卡的状态", getSimState(phone.getSimState()), "Returns the alphabetic name of current registered operator."));
            mList.add(new ItemBean("获取网络类型", getNetworkType(phone.getNetworkType()), "Returns the ISO country code equivalent of the current registered operator's MCC (Mobile Country Code) of a subscription."));

            mList.add(new ItemBean("CC+SIM卡运营商的国家代码", phone.getSimCountryIso(), "Returns the ISO country code equivalent for the SIM provider's country code."));
            mList.add(new ItemBean("SIM卡的序号", phone.getSimSerialNumber(), "Returns the serial number of the SIM, if applicable. Return null if it is unavailable."));
            mList.add(new ItemBean("语音信箱的检索字母标识符", phone.getVoiceMailAlphaTag(), " Retrieves the alphabetic identifier associated with the voice mail number."));


            mList.add(new ItemBean("获取设备的当前位置", phone.getCellLocation().toString(), "#getAllCellInfo} instead"));
            //ACCESS_COARSE_LOCATION
            mList.add(new ItemBean("cellInfos", listToString(phone.getAllCellInfo()), "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available."));
            mList.add(new ItemBean("手机号码", phone.getLine1Number(), "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available."));
            mList.add(new ItemBean("IMSI", phone.getSubscriberId(), "Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable."));
            mList.add(new ItemBean("数据活动状态", getPhoneActivityState(phone.getDataActivity()), "Returns a constant indicating the type of activity on a data connection"));
            mList.add(new ItemBean("数据连接状态", getPhoneDataState(phone.getDataState()), "Returns a constant indicating the type of activity on a data connection"));


        }
        ///////////////////////////////////////////////////手机取值结束///////////////////////////////////////////////////////////////////
        //https://blog.csdn.net/jdsjlzx/article/details/40740543
        @SuppressLint("WifiManagerLeak") WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (null != wifi && null != wifi.getConnectionInfo()) {
            WifiInfo connectionInfo = wifi.getConnectionInfo();
            mList.add(new ItemBean("WifiMac地址", connectionInfo.getMacAddress(), "Returns a constant indicating the type of activity on a data connection"));
        }


        mAdapter.notifyDataSetChanged();

        Display display = getWindowManager().getDefaultDisplay();

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        DisplayMetrics book = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(book);

        try {
            //反射获取值
            Class localClass = Class.forName("android.os.SystemProperties");
            Object localObject1 = localClass.newInstance();
            Object localObject2 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"gsm.version.baseband", "no message"});
            Object localObject3 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"ro.build.display.id", ""});


//            setEditText(R.id.get, localObject2 + "");
//
//            setEditText(R.id.osVersion, localObject3 + "");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取网络连接管理者
        ConnectivityManager connectionManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

//        setEditText(R.id.lianwang, networkInfo.getType() + "");
//        setEditText(R.id.lianwangname, networkInfo.getTypeName());
//
//
//        setEditText(R.id.wifimac, wifi.getConnectionInfo().getMacAddress());
//        setEditText(R.id.getssid, wifi.getConnectionInfo().getSSID());
//        setEditText(R.id.getbssid, wifi.getConnectionInfo().getBSSID());
//        setEditText(R.id.text_item_ip_value, wifi.getConnectionInfo().getIpAddress() + "");
//        setEditText(R.id.bluemac, BluetoothAdapter.getDefaultAdapter().getAddress());
//        setEditText(R.id.bluname, BluetoothAdapter.getDefaultAdapter().getName() );
//
//        setEditText(R.id.cpu, getCpuName());
//
//
//        setEditText(R.id.text_item_android_id_value, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
//
//        setEditText(R.id.radiovis, Build.getRadioVersion());
//        setEditText(R.id.text_item_build_serial_value, Build.SERIAL);
//        setEditText(R.id.text_item_build_brand_value, Build.BRAND);
//        setEditText(R.id.text_item_build_tag_value, Build.TAGS);
//        setEditText(R.id.text_item_build_device_value, Build.DEVICE);
//        setEditText(R.id.text_item_fingerprint_value, Build.FINGERPRINT);
//        setEditText(R.id.text_item_build_bootloader_value, Build.BOOTLOADER);
//        setEditText(R.id.text_item_version_release_value, Build.VERSION.RELEASE);
//        setEditText(R.id.text_item_version_sdk_value, Build.VERSION.SDK);
//        setEditText(R.id.text_item_version_sdk_int_value, Build.VERSION.SDK_INT + "");
//        setEditText(R.id.text_item_version_code_name_value, Build.VERSION.CODENAME);
//        setEditText(R.id.text_item_version_incremental_value, Build.VERSION.INCREMENTAL);
//        setEditText(R.id.text_item_build_cpu_abi_value, Build.CPU_ABI);
//        setEditText(R.id.text_item_build_cpu_abi_2_value, Build.CPU_ABI2);
//        setEditText(R.id.text_item_build_board_value, Build.BOARD);
//        setEditText(R.id.text_item_build_model_value, Build.MODEL);
//        setEditText(R.id.text_item_build_product_value, Build.PRODUCT);
//        setEditText(R.id.text_item_build_type_value, Build.TYPE);
//        setEditText(R.id.text_item_build_user_value, Build.USER);
//        setEditText(R.id.text_item_build_display_value, Build.DISPLAY);
//        setEditText(R.id.text_item_build_hardware_value, Build.HARDWARE);
//        setEditText(R.id.text_item_build_host_value, Build.HOST);
//        setEditText(R.id.text_item_build_manufacturer_value, Build.MANUFACTURER);
//
//        setEditText(R.id.text_item_build_id_value, Build.ID);
//        setEditText(R.id.text_item_build_time_value, Build.TIME + "");
//
//        setEditText(R.id.text_item_width_value, display.getWidth() + "");
//        setEditText(R.id.text_item_height_value, display.getHeight() + "");
//        setEditText(R.id.text_item_dpi_value, book.densityDpi + "");
//        setEditText(R.id.text_item_density_value, book.density + "");
//        setEditText(R.id.text_item_dpi_x_value, book.xdpi + "");
//        setEditText(R.id.text_item_dpi_y_value, book.ydpi + "");
//        setEditText(R.id.text_item_scale_density_value, book.scaledDensity + "");
//
//
//        //setEditText(R.id.wl,getNetworkState(this)+"");
//        // 方法2
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        setEditText(R.id.text_item_x_width_value, width + "");
//        setEditText(R.id.text_item_x_height_value, height + "");


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
     * @param objectList
     * @return
     */
    private String listToString(List<CellInfo> objectList) {
        if (null == objectList || objectList.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : objectList) {
            stringBuilder.append(o.toString()).append('\n');
        }
        return stringBuilder.toString().trim();
    }

    /**
     * 获取数据活动状态
     * 0:   DATA_ACTIVITY_NON     数据连接状态：活动，但无数据发送和接受
     * 1:   DATA_ACTIVITY_IN      数据连接状态：活动，正在接受数据
     * 2:   DATA_ACTIVITY_OUT     数据连接状态：活动，正在发送数据
     * 3:   DATA_ACTIVITY_INOUT   数据连接状态：活动，正在接受和发送数据
     * 4:   DATA_ACTIVITY_DORMANT 睡眠模式
     */
    private String getPhoneActivityState(int state) {
        switch (state) {
            case TelephonyManager.DATA_ACTIVITY_NONE:
                return "但无数据发送和接受";
            case TelephonyManager.DATA_ACTIVITY_IN:
                return "正在接受数据";
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return "正在发送数据";
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return "正在接受和发送数据";
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                return "睡眠模式";
            default:
                return "但无数据发送和接受";
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
                return "断开";
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
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "TD_SCDMA";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "IWLAN";
            case 19:
                return "LTE_CA";
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
