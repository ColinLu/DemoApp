package com.colin.demo.app.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.colin.demo.app.BuildConfig;
import com.colin.demo.app.R;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.SpUtil;
import com.colin.demo.app.utils.ToastUtil;

import java.io.File;
import java.util.List;

/**
 * 描述：下载工具类
 * <p>
 * 作者：Colin
 * 时间：2018/6/26
 */
public class AppUpdateManager {
    /**
     * getDownloadStatus如果没找到会返回-1
     */
    public static final long STATUS_UN_FIND = -1L;
    //下载保存关键id
    private static final String SP_DOWNLOAD_ID = "downloadId";

    private DownloadManager mDownloadManager;

    private AppUpdateManager() {

    }


    public static class Holder {
        static AppUpdateManager instance = new AppUpdateManager();
    }

    public static AppUpdateManager getInstance() {
        return AppUpdateManager.Holder.instance;
    }

    public DownloadManager getDownloadManager(Context context) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mDownloadManager;
    }

    /**
     * 根据配置内容下载
     * 检查历史下载记录
     *
     * @param updaterConfig
     */
    public void download(UpdaterConfig updaterConfig) {
        if (null == updaterConfig || null == updaterConfig.mContext) {
            return;
        }
        //检查下载配置是否可以使用
        if (!checkDownloadState(updaterConfig.mContext)) {
            ToastUtil.showToast(R.string.system_download_component_disable);
            showDownloadSetting(updaterConfig.mContext);
            return;
        }
        //检查本地收缓存历史下载记录
        long downloadId = SpUtil.get(updaterConfig.mContext, SP_DOWNLOAD_ID, STATUS_UN_FIND);
        LogUtil.e("downloadId-->>" + downloadId);
        if (downloadId == STATUS_UN_FIND) {
            startDownload(updaterConfig);
            return;
        }
        //获取下载状态
        int status = getDownloadStatus(updaterConfig.mContext, downloadId);
        LogUtil.e("status-->>" + String.valueOf(status));
        switch (status) {
            case DownloadManager.STATUS_RUNNING://正在下载
            case DownloadManager.STATUS_PENDING://准备中
            case DownloadManager.STATUS_PAUSED: //暂停
                break;
            //下载成功
            case DownloadManager.STATUS_SUCCESSFUL://下载成功
                Uri uri = getDownloadUri(updaterConfig.mContext, downloadId);
                //没有，重新下载
                if (null == uri) {
                    startDownload(updaterConfig);
                    return;
                }
                //失效
                if (null == getApkInfo(updaterConfig.mContext, uri.getPath())) {
                    getDownloadManager(updaterConfig.mContext).remove(downloadId);
                    startDownload(updaterConfig);
                    return;
                }
                installApk(updaterConfig.mContext, downloadId);
                break;
            case DownloadManager.STATUS_FAILED://下载失败
                startDownload(updaterConfig);
                break;
            default:
                startDownload(updaterConfig);
                break;
        }
    }


    /**
     * 开始下载
     *
     * @param updaterConfig
     */
    private void startDownload(UpdaterConfig updaterConfig) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(updaterConfig.mFileUrl));
        request.setAllowedNetworkTypes(updaterConfig.mAllowedNetworkTypes);
        //request.setAllowedOverMetered()
        //移动网络是否允许下载
        request.setAllowedOverRoaming(updaterConfig.mAllowedOverRoaming);
        if (updaterConfig.mCanMediaScanner) {
            //能够被MediaScanner扫描
            request.allowScanningByMediaScanner();
        }

        // 设置一些基本显示信息
        request.setTitle(updaterConfig.mTitle);
        request.setDescription(updaterConfig.mDescription);
        // 3.0(11)以后才有该方法
        //在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //点击正在下载的Notification进入下载详情界面，如果设为true则可以看到下载任务的进度，如果设为false，则看不到我们下载的任务
        request.setVisibleInDownloadsUi(updaterConfig.mIsShowDownloadUI);

        request.setAllowedOverRoaming(false);

        request.setMimeType("application/vnd.android.package-archive");
        //设置文件的保存的位置[三种方式]
        //第一种
//        file:///storage/emulated/0/Android/data/your-package/files/Download/app.apk
//        request.setDestinationInExternalFilesDir(updaterConfig.mContext, Environment.DIRECTORY_DOWNLOADS, updaterConfig.getFilename());
        //第二种
        //file:///storage/emulated/0/Download/app.apk
//        创建目录  没有创建
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, updaterConfig.getFilename());
        }
        //第三种 自定义文件路径
//        request.setDestinationUri(getDownloadAppUri(updaterConfig.mContext, updaterConfig.getFilename()));

        //把DownloadId保存到本地
        long downloadId = getDownloadManager(updaterConfig.mContext).enqueue(request);
        registerListener(updaterConfig, downloadId);
        SpUtil.put(updaterConfig.mContext, SP_DOWNLOAD_ID, downloadId);
        //保存文件名字
        SpUtil.put(updaterConfig.mContext, String.valueOf(downloadId), updaterConfig.getFilename());
    }

    /**
     * 注册监听器接口回调
     *
     * @param updaterConfig
     * @param downloadId
     */
    private void registerListener(UpdaterConfig updaterConfig, long downloadId) {
        if (null == updaterConfig.mContext || null == updaterConfig.mOnDownloadListener) {
            return;
        }
    }


    /**
     * 获取文件保存的路径  有问题
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see DownloadManager#getMimeTypeForDownloadedFile(long) (Context, long)
     */
    private String getDownloadPath(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = getDownloadManager(context).query(query);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = getDownloadManager(context).query(query);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                cursor.close();
            }
        }
        return -1;
    }


    /**
     * 系统的下载组件是否可用
     *
     * @return boolean
     */
    public boolean checkDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 到下载设置界面
     *
     * @param context
     */
    public void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intentAvailable(context, intent)) {
            context.startActivity(intent);
        }

    }

    /**
     * 要启动的intent是否可用
     *
     * @return boolean
     */
    public boolean intentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param context Context 当前运行程序的Context
     * @param path    apk file's location
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    public boolean compare(Context context, String path) {
        PackageInfo apkInfo = getApkInfo(context, path);
        if (apkInfo == null) {
            return false;
        }

        String localPackage = context.getPackageName();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
            LogUtil.e("apk file packageName=" + apkInfo.packageName + ",versionName=" + apkInfo.versionName);
            LogUtil.e("current app packageName=" + packageInfo.packageName + ",versionName=" + packageInfo.versionName);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            if (apkInfo.packageName.equals(localPackage)) {
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 获取保存文件的地址  android 7.0 不适配
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see AppUpdateManager#getDownloadPath(Context, long)
     */
    public Uri getDownloadUri(Context context, long downloadId) {
        return getDownloadManager(context).getUriForDownloadedFile(downloadId);
    }

    /**
     * 安装app
     *
     * @param context
     * @param downloadApkId
     */
    public void installApk(Context context, long downloadApkId) {
        if (downloadApkId == STATUS_UN_FIND) {
            LogUtil.e("状态不对");
            return;
        }

        String fileName = SpUtil.get(context, String.valueOf(downloadApkId), "");
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        dirPath = dirPath.endsWith(File.separator) ? dirPath : dirPath + File.separator;
        File apkFile = new File(dirPath, fileName);
        installApk(context, apkFile);
    }

    /**
     * 安装app 适配权限7.0
     *
     * @param context
     * @param file
     */
    public void installApk(Context context, File file) {
        if (null == file) {
            LogUtil.e("file为空");
            return;
        }
        if (!file.exists()) {
            ToastUtil.showToast("App安装文件不存在!");
            return;
        }
        Uri apkFileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        } else {
            apkFileUri = Uri.fromFile(file);
        }
        LogUtil.e("uri path-->>" + apkFileUri.getPath());
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * 安装app
     *
     * @param context
     * @param uri
     */
    public void installApk(Context context, Uri uri) {
        if (null == uri) {
            LogUtil.e("uri为空");
            return;
        }
        LogUtil.e("uri path-->>" + uri.getPath());
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }


}
