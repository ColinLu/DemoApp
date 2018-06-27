package com.colin.demo.app.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.colin.demo.app.R;

import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.attr.permission;

/**
 * Created by Colin on 2017/2/8.
 * <p>
 * android.permission-group.CALENDAR
 * android.permission.READ_CALENDAR
 * android.permission.WRITE_CALENDAR
 * <p>
 * android.permission-group.CAMERA
 * android.permission.CAMERA
 * <p>
 * android.permission-group.CONTACTS
 * android.permission.READ_CONTACTS
 * android.permission.WRITE_CONTACTS
 * android.permission.GET_ACCOUNTS
 * <p>
 * android.permission-group.LOCATION
 * android.permission.ACCESS_FINE_LOCATION
 * android.permission.ACCESS_COARSE_LOCATION
 * <p>
 * android.permission-group.MICROPHONE
 * android.permission.RECORD_AUDIO
 * <p>
 * android.permission-group.PHONE
 * android.permission.READ_PHONE_STATE
 * android.permission.CALL_PHONE
 * android.permission.READ_CALL_LOG
 * android.permission.WRITE_CALL_LOG
 * com.android.voicemail.permission.ADD_VOICEMAIL
 * android.permission.USE_SIP
 * android.permission.PROCESS_OUTGOING_CALLS
 * <p>
 * android.permission-group.SENSORS
 * android.permission.BODY_SENSORS
 * <p>
 * android.permission-group.SMS
 * android.permission.SEND_SMS
 * android.permission.RECEIVE_SMS
 * android.permission.READ_SMS
 * android.permission.RECEIVE_WAP_PUSH
 * android.permission.RECEIVE_MMS
 * android.permission.READ_CELL_BROADCASTS
 * <p>
 * android.permission-group.STORAGE
 * android.permission.READ_EXTERNAL_STORAGE
 * android.permission.WRITE_EXTERNAL_STORAGE
 * <p>
 * 其他手机需要适配这个权限
 * Manifest.permission.SYSTEM_ALERT_WINDOW
 */
@SuppressLint("InlinedApi")
public class PermissionUtil {
    private AlertDialog dialog;
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
//            , Manifest.permission.ADD_VOICEMAIL
//            , Manifest.permission.USE_SIP
//            , Manifest.permission.PROCESS_OUTGOING_CALLS

    };
    //传感器
    public static final String[] PERMISSIONS_SENSORS = {Manifest.permission.BODY_SENSORS};
    //短信
    public static final String[] PERMISSIONS_WRITE_SMS = {Manifest.permission_group.SMS
    };
    public static final String[] PERMISSIONS_SMS = {Manifest.permission.READ_SMS
//            , Manifest.permission.RECEIVE_SMS
//            , Manifest.permission.RECEIVE_MMS
//            , Manifest.permission.SEND_SMS
//            , Manifest.permission.BROADCAST_SMS
//            , Manifest.permission.RECEIVE_WAP_PUSH
    };
    //内存卡权限
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE
            , WRITE_EXTERNAL_STORAGE};
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
    public static final int REQUEST_CODE_CHAT = 12;
    public static final int REQUEST_CODE_SHARE = 13;
    public static final int REQUEST_CODE_WELCOME = 14;
    //自定义的权限
    private int mRequestCode = -1;

    private final String ALERT_TITLE = "权限提醒";
    private final String ALERT_MESSAGE_CALENDAR = "接下来操作需要读取日历信息，请允许APP获取手机日历权限";
    private final String ALERT_MESSAGE_CAMERA = "接下来操作需要拍照功能，请允许APP获取手机拍照功能权限";
    private final String ALERT_MESSAGE_CONTACTS = "接下来操作需要读取手机通讯录信息，请允许APP获取手机通讯录权限";
    private final String ALERT_MESSAGE_LOCATION = "接下来操作需要读取手机定位信息，请允许APP获取手机定位权限";
    private final String ALERT_MESSAGE_MICROPHONE = "接下来操作需要麦克风功能，请允许APP获取手机麦克风功能权限";
    private final String ALERT_MESSAGE_PHONE = "接下来操作需要手机打电话功能，请允许APP获取手机打电话权限";
    private final String ALERT_MESSAGE_SENSORS = "接下来操作传感器功能，请允许APP获取手机传感器权限";
    private final String ALERT_MESSAGE_SMS = "接下来操作需要读取短信信息，请允许APP获取手机短信权限";
    private final String ALERT_MESSAGE_STORAGE = "接下来操作需要读取手机存储数据，请允许APP获取手机存储权限";
    private final String ALERT_MESSAGE_WINDOW = "接下来操作屏幕上显示提示框，请允许APP获取手机显示提示框权限";


    private volatile static PermissionUtil instance = null;

    private PermissionUtil() {
    }

    public static PermissionUtil getInstance() {
        if (instance == null) {
            synchronized (PermissionUtil.class) {
                if (instance == null) {
                    instance = new PermissionUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 1、判断版本号
     * 2、配置清单文件中是否含有这个权限
     * 2、是否已经有了这个权限
     * 3、提醒用户获取权限
     * 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
     * 返回值是:PERMISSION_GRANTED 有这个权限
     * PERMISSION_DENIED 没有这个权限
     *
     * @param activity   上下文 Activity/Fragment
     * @param permission 可能一个权限可能多个权限
     * @return
     */
    public boolean checkPermission(@NonNull Activity activity, @NonNull String permission) {
        //判断是否符合版本要求  6.0 23及以上才需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("版本太低了，不需要申请权限==" + permission);
            return true;
        }
        //配置清单中是否存在这个权限
        if (!permissionExists(activity, permission)) {
            LogUtil.e("配置清单文件中没有这个权限，不需要申请权限==" + permission);
            return true;
        }
        //检测是否拥有权限。
        if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            LogUtil.e("Activity同意了这个权限，不需要再次申请权限==" + permission);
            return true;
        }
        showRequestPermissions(activity, permission);
        return false;
    }

    public boolean checkPermission(@NonNull Activity activity, @NonNull List<String> permissionList) {
        return checkPermission(activity, permissionList, REQUEST_CODE_MORE);
    }

    public boolean checkPermission(@NonNull Activity activity, @NonNull List<String> permissionList, int requestCode) {
        if (null == permissionList || permissionList.size() == 0) {
            return true;
        }
        //判断是否符合版本要求  6.0 23及以上才需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("版本太低了，不需要申请权限==" + permission);
            return true;
        }
        boolean agree = true;
        //配置清单中是否存在这个权限,并且这个权限没有被授予
        for (String permission : permissionList) {
            if (permissionExists(activity, permission) && ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                agree = false;
            }
        }
        if (!agree) {
            showRequestPermissions(activity, permissionList, requestCode);
        }
        return agree;
    }

    public boolean checkPermission(@NonNull Fragment fragment, @NonNull String permission) {
        //判断是否符合版本要求  6.0 23及以上才需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("版本太低了，不需要申请权限==" + permission);
            return true;
        }
        //配置清单中是否存在这个权限
        if (!permissionExists(fragment.getActivity(), permission)) {
            LogUtil.e("配置清单文件中没有这个权限，不需要申请权限==" + permission);
            return true;
        }
        //第一步，检测是否拥有权限。
        if (ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
            LogUtil.e("Fragment同意了这个权限，不需要再次申请权限==" + permission);
            return true;
        }
        showRequestPermissions(fragment, permission);
        return false;

    }

    public boolean checkPermission(@NonNull Fragment fragment, @NonNull List<String> permissionList) {
        return checkPermission(fragment, permissionList, REQUEST_CODE_MORE);
    }

    public boolean checkPermission(@NonNull Fragment fragment, @NonNull List<String> permissionList, int requestCode) {
        if (null == permissionList || permissionList.size() == 0) {
            return true;
        }
        //判断是否符合版本要求  6.0 23及以上才需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("版本太低了，不需要申请权限==" + permission);
            return true;
        }
        boolean isAgree = true;
        //配置清单中是否存在这个权限,并且这个权限没有被授予
        for (String permission : permissionList) {
            if (permissionExists(fragment.getActivity(), permission) && ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                isAgree = false;
            }
        }
        if (!isAgree) {
            showRequestPermissions(fragment, permissionList);
        }
        return isAgree;
    }

    /**
     * 多个权限操作
     *
     * @param activity
     * @param permissionList
     */
    private void showRequestPermissions(@NonNull Activity activity, @NonNull List<String> permissionList) {
        showRequestPermissions(activity, permissionList, REQUEST_CODE_MORE);
    }

    /**
     * 多个权限操作
     *
     * @param activity
     * @param permissionList
     */
    private void showRequestPermissions(@NonNull Activity activity, @NonNull List<String> permissionList, int requestCode) {
        if (permissionList.isEmpty()) {
            return;
        }
        boolean hasAlertWindowPermission = permissionList.contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (hasAlertWindowPermission) {
            int index = permissionList.indexOf(Manifest.permission.SYSTEM_ALERT_WINDOW);
            permissionList.remove(index);
        }
        mRequestCode = requestCode;
        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), requestCode);
    }


    private void showRequestPermissions(@NonNull Fragment fragment, @NonNull List<String> permissionList) {
        showRequestPermissions(fragment, permissionList, REQUEST_CODE_MORE);
    }

    private void showRequestPermissions(@NonNull Fragment fragment, @NonNull List<String> permissionList, int requestCode) {
        if (permissionList.isEmpty()) {
            return;
        }
        boolean hasAlertWindowPermission = permissionList.contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (hasAlertWindowPermission) {
            int index = permissionList.indexOf(Manifest.permission.SYSTEM_ALERT_WINDOW);
            permissionList.remove(index);
        }
        mRequestCode = requestCode;
        fragment.requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_MORE);
    }

    /**
     * 区分权限类型
     *
     * @param context            上下文Activity / Fragment
     * @param grantedPermissions 需要授予的权限
     */
    private void showRequestPermissions(Object context, String grantedPermissions) {
        if (grantedPermissions.equals(Manifest.permission.READ_CALENDAR)) {//日历
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CALENDAR, ALERT_MESSAGE_CALENDAR);
        } else if (grantedPermissions.equals(Manifest.permission.WRITE_CALENDAR)) {//日历
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CALENDAR, ALERT_MESSAGE_CALENDAR);
        } else if (grantedPermissions.equals(Manifest.permission.CAMERA)) {//照相机
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CAMERA, ALERT_MESSAGE_CAMERA);
        } else if (grantedPermissions.equals(Manifest.permission.GET_ACCOUNTS)) {//联系人权限
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CONTACTS, ALERT_MESSAGE_CONTACTS);
        } else if (grantedPermissions.equals(Manifest.permission.READ_CONTACTS)) {//联系人权限
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CONTACTS, ALERT_MESSAGE_CONTACTS);
        } else if (grantedPermissions.equals(Manifest.permission.WRITE_CONTACTS)) {//联系人权限
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_CONTACTS, ALERT_MESSAGE_CONTACTS);
        } else if (grantedPermissions.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {//定位
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_LOCATION, ALERT_MESSAGE_LOCATION);
        } else if (grantedPermissions.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {//定位
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_LOCATION, ALERT_MESSAGE_LOCATION);
        } else if (grantedPermissions.equals(Manifest.permission.RECORD_AUDIO)) {//麦克风
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_MICROPHONE, ALERT_MESSAGE_MICROPHONE);
        } else if (grantedPermissions.equals(Manifest.permission.CALL_PHONE)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.READ_PHONE_STATE)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.READ_CALL_LOG)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.WRITE_CALL_LOG)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.ADD_VOICEMAIL)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.USE_SIP)) {//打电话
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_PHONE, ALERT_MESSAGE_PHONE);
        } else if (grantedPermissions.equals(Manifest.permission.BODY_SENSORS)) {//传感器
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SENSORS, ALERT_MESSAGE_SENSORS);
        } else if (grantedPermissions.equals(Manifest.permission.SEND_SMS)) {//短信
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SMS, ALERT_MESSAGE_SMS);
        } else if (grantedPermissions.equals(Manifest.permission.RECEIVE_SMS)) {//短信
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SMS, ALERT_MESSAGE_SMS);
        } else if (grantedPermissions.equals(Manifest.permission.RECEIVE_WAP_PUSH)) {//短信
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SMS, ALERT_MESSAGE_SMS);
        } else if (grantedPermissions.equals(Manifest.permission.RECEIVE_MMS)) {//短信
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SMS, ALERT_MESSAGE_SMS);
        } else if (grantedPermissions.equals(Manifest.permission.BROADCAST_SMS)) {//短信
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_SMS, ALERT_MESSAGE_SMS);
        } else if (grantedPermissions.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {//存储
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_STORAGE, ALERT_MESSAGE_STORAGE);
        } else if (grantedPermissions.equals(WRITE_EXTERNAL_STORAGE)) {//存储
            requestPermissions(context, grantedPermissions, new String[]{grantedPermissions}, REQUEST_CODE_STORAGE, ALERT_MESSAGE_STORAGE);
        } else if (grantedPermissions.equals(Manifest.permission.DELETE_PACKAGES)) {//删除包
            requestSystemAlertPermission(context);
        } else if (grantedPermissions.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {//提示框
            requestSystemAlertPermission(context);
        }
    }


    /**
     * 开启权限提醒与授权
     * 检测是否需要显示申请权限对话框。如果第一步返回没有权限的话，则我们需要去申请权限，
     * 但是申请权限之前，需要显示查看用户是否已经禁止了申请权限对话框，如果禁止的话，我们用第三步的代码将没有任何效果。
     * ActivityCompat.shouldShowRequestPermissionRationale / FragmentCompat.shouldShowRequestPermissionRationale
     * 返回为 true 或 false ，在 6.0以下恒定为 false
     * 6.0以下还没有系统默认对话框存在，肯定不弹了。
     *
     * @param context            上下文Activity / Fragment
     * @param grantedPermissions 需要授予的权限
     * @param requestCode        返回值
     * @param alert_message      Dialog 提示信息
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions(Object context, String grantedPermissions, String[] permissions, int requestCode, String alert_message) {
        mRequestCode = requestCode;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, grantedPermissions)) {
                showDialog((Activity) context, alert_message, permissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        } else if (context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            if (fragment.shouldShowRequestPermissionRationale(grantedPermissions)) {
                showDialog(fragment, alert_message, permissions, requestCode);
            } else {
                fragment.requestPermissions(permissions, requestCode);
            }
        }
    }

    /**
     * 提醒用户开启权限
     *
     * @param activity
     * @param message
     * @param permissions
     * @param requestCode
     */
    private void showDialog(final Activity activity, String message, final String[] permissions, final int requestCode) {
        dialog = new AlertDialog.Builder(activity)
                .setTitle(ALERT_TITLE)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                }).create();
        dialog.show();
    }

    /**
     * 提醒用户开启权限
     *
     * @param fragment
     * @param message
     * @param permissions
     * @param requestCode
     */
    private void showDialog(final Fragment fragment, String message, final String[] permissions, final int requestCode) {
        dialog = new AlertDialog.Builder(fragment.getActivity())
                .setTitle(ALERT_TITLE)
//                .setIcon(R.drawableCreditText.ic_error_warning)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialog.dismiss();
                        dialog = null;
                        fragment.requestPermissions(permissions, requestCode);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }).create();
        dialog.show();
    }


    /**
     * @return 如果允许存在于清单:true
     * 不允许存在于清单:false
     */
    private boolean permissionExists(Context context, @NonNull String permissionName) {
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

    /**
     * 系统设置
     *
     * @param context
     */
    private void requestSystemAlertPermission(Object context) {
        try {
            if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Activity activity = (Activity) context;
                if (!Settings.canDrawOverlays(activity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, REQUEST_CODE_WINDOW);
                }
            } else if (context instanceof Fragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Fragment fragment = (Fragment) context;
                if (!Settings.canDrawOverlays(fragment.getActivity())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + fragment.getActivity().getPackageName()));
                    fragment.getActivity().startActivityForResult(intent, REQUEST_CODE_WINDOW);
                }
            }
        } catch (Exception e) {
            LogUtil.e("e==" + e.getMessage());
        }
    }

    /**
     * 权限获取 得到结果
     *
     * @return
     */
    public boolean onRequestPermissionsResult(int[] grantResults) {
        LogUtil.e("mRequestCode-->>" + String.valueOf(mRequestCode));
        boolean grant = true;
        if (grantResults == null) {
            return false;
        }

        for (int grantResult : grantResults) {
            if (grantResult != 0) {
                grant = false;
            }

        }
        return grant;
    }

    /**
     * 权限获取 得到结果
     *
     * @return
     */
    public boolean onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (mRequestCode != requestCode) {
            LogUtil.e("requestCode--" + String.valueOf(requestCode));
            return false;
        }
        boolean grant = true;
        if (grantResults == null) {
            LogUtil.e("grantResult-->>空");
            return false;
        }
        for (int grantResult : grantResults) {
            LogUtil.e("grantResult-->>" + String.valueOf(grantResult));
            if (grantResult != 0) {
                grant = false;
            }
        }
        return grant;
    }
}
