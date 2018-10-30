package com.colin.device.api.library;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;


public class PermissionUtil {


    private static class Holder {
        public final static PermissionUtil instance = new PermissionUtil();
    }

    public static PermissionUtil getInstance() {
        return PermissionUtil.Holder.instance;
    }

    //日历数据权限
    public static final String[] PERMISSIONS_CALENDAR = {Manifest.permission.READ_CALENDAR
            , Manifest.permission.WRITE_CALENDAR};

    //相机权限
    public static final String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //联系人权限
    public static final String[] PERMISSIONS_CONTACTS = {Manifest.permission.READ_CONTACTS
            , Manifest.permission.GET_ACCOUNTS
            , Manifest.permission.WRITE_CONTACTS
    };
    //定位权限
    public static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    //麦克风权限
    public static final String[] PERMISSIONS_MICROPHONE = {Manifest.permission.RECORD_AUDIO};
    //拨打电话权限
    public static final String[] PERMISSIONS_PHONE = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.CALL_PHONE
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.WRITE_CALL_LOG
            , Manifest.permission.ADD_VOICEMAIL
            , Manifest.permission.USE_SIP
            , Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    //传感器
    public static final String[] PERMISSIONS_SENSORS = {Manifest.permission.BODY_SENSORS};
    //短信
    public static final String[] PERMISSIONS_WRITE_SMS = {Manifest.permission_group.SMS
    };
    public static final String[] PERMISSIONS_SMS = {Manifest.permission.READ_SMS
            , Manifest.permission.RECEIVE_SMS
            , Manifest.permission.RECEIVE_MMS
            , Manifest.permission.SEND_SMS
            , Manifest.permission.BROADCAST_SMS
            , Manifest.permission.RECEIVE_WAP_PUSH
    };
    //内存卡权限
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String[] DELETE_PACKAGES = {Manifest.permission.DELETE_PACKAGES};


    //屏幕显示警告框
    public static final String[] PERMISSIONS_WINDOW = {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.SYSTEM_ALERT_WINDOW};

    //客服按钮需要获取权限
    public static final String[] PERMISSIONS_CHAT = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    //欢迎界面必须申请的请求
    public static final String[] PERMISSIONS_WELCOME = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    //下载APP 安装
    public static final String[] PERMISSIONS_DOWNLOAD_APP = {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CODE_CALENDAR = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_CONTACTS = 2;
    public static final int REQUEST_CODE_LOCATION = 3;
    public static final int REQUEST_CODE_MICROPHONE = 4;
    public static final int REQUEST_CODE_PHONE = 5;
    public static final int REQUEST_CODE_SENSORS = 6;
    public static final int REQUEST_CODE_SMS = 7;
    public static final int REQUEST_CODE_STORAGE = 8;
    public static final int REQUEST_CODE_WINDOW = 9;
    public static final int REQUEST_CODE_PACKAGES = 10;
    public static final int REQUEST_CODE_MORE = 11;


    /**
     * 判定系统是否统一 相应的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public boolean checkPermission(@NonNull Context context, String... permissions) {
        //没有权限判定
        if (null == permissions || permissions.length == 0) return true;
        //低版本判定
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 如果允许存在于清单:true
     * 不允许存在于清单:false
     */
    private boolean checkManiPermission(@NonNull Context context, @NonNull String permissionName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (packageInfo.requestedPermissions != null) {
                for (String p : packageInfo.requestedPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
