package com.colin.demo.app.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.StringUtil;

public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (StringUtil.isEmpty(action)) {
            LogUtil.e("action-->>" + String.valueOf(action));
            return;
        }
        //加载完成安装
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, AppUpdateManager.STATUS_UN_FIND);
            if (downloadApkId == AppUpdateManager.STATUS_UN_FIND) {
                return;
            }
//            AppUpdateManager.getInstance().installApk(context, downloadApkId);
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            //处理 如果还未完成下载，用户点击Notification
            Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewDownloadIntent);
        }
    }


}
