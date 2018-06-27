package com.colin.demo.app.download;

import android.content.Context;

import com.colin.demo.app.utils.StringUtil;

/**
 * Description：下载器参数配置
 * <br/>
 * Created by kumu on 2017/5/17.
 */

public class UpdaterConfig {

    public Context mContext;
    public String mTitle;
    public String mDescription;
    public String mDownloadPath;
    public String mFileUrl;
    public String mFilename = "app.apk";       //默认下载app文件名
    public boolean mIsShowDownloadUI = true;
    public int mNotificationVisibility;
    public boolean mCanMediaScanner;
    public boolean mAllowedOverRoaming;
    public int mAllowedNetworkTypes = ~0;// default to all network types allowed
    public OnDownloadListener mOnDownloadListener;//接口回调 监听进度


    public UpdaterConfig(Context context) {
        mContext = context;
    }

    public String getFilename() {
        if (StringUtil.isEmpty(mFilename)) {
            return "app.apk";
        }
        return mFilename;
    }


    public static class Builder {

        UpdaterConfig updaterConfig;

        public Builder(Context context) {
            updaterConfig = new UpdaterConfig(context);
        }

        public Builder setTitle(String title) {
            updaterConfig.mTitle = title;
            return this;
        }

        public Builder setDescription(String description) {
            updaterConfig.mDescription = description;
            return this;
        }

        /**
         * 文件下载路径
         *
         * @param downloadPath
         * @return
         */
        public Builder setDownloadPath(String downloadPath) {
            updaterConfig.mDownloadPath = downloadPath;
            return this;
        }

        /**
         * 文件网络地址
         *
         * @param url
         * @return
         */
        public Builder setFileUrl(String url) {
            updaterConfig.mFileUrl = url;
            return this;
        }

        /**
         * 下载的文件名
         *
         * @param filename
         * @return
         */
        public Builder setFilename(String filename) {
            updaterConfig.mFilename = filename;
            return this;
        }

        public Builder setIsShowDownloadUI(boolean isShowDownloadUI) {
            updaterConfig.mIsShowDownloadUI = isShowDownloadUI;
            return this;
        }

        public Builder setNotificationVisibility(int notificationVisibility) {
            updaterConfig.mNotificationVisibility = notificationVisibility;
            return this;
        }

        /**
         * 能否被 MediaScanner 扫描
         *
         * @param canMediaScanner
         * @return
         */
        public Builder setCanMediaScanner(boolean canMediaScanner) {
            updaterConfig.mCanMediaScanner = canMediaScanner;
            return this;
        }

        /**
         * 移动网络是否允许下载
         *
         * @param allowedOverRoaming
         * @return
         */
        public Builder setAllowedOverRoaming(boolean allowedOverRoaming) {
            updaterConfig.mAllowedOverRoaming = allowedOverRoaming;
            return this;
        }


        /**
         * By default, all network types are allowed
         *
         * @param allowedNetworkTypes
         * @see AllowedNetworkType#NETWORK_MOBILE
         * @see AllowedNetworkType#NETWORK_WIFI
         */
        public Builder setAllowedNetworkTypes(int allowedNetworkTypes) {
            updaterConfig.mAllowedNetworkTypes = allowedNetworkTypes;
            return this;
        }

        /**
         * 接口回调
         *
         * @param onDownloadListener
         * @return
         */
        public Builder setOnDownloadListener(OnDownloadListener onDownloadListener) {
            this.updaterConfig.mOnDownloadListener = onDownloadListener;
            return this;
        }

        public UpdaterConfig build() {
            return updaterConfig;
        }


    }

    public interface AllowedNetworkType {
        /**
         * Bit flag for {@link android.app.DownloadManager.Request#NETWORK_MOBILE}
         */
        int NETWORK_MOBILE = 1 << 0;

        /**
         * Bit flag for {@link android.app.DownloadManager.Request#NETWORK_WIFI}
         */
        int NETWORK_WIFI = 1 << 1;
    }

    public interface OnDownloadListener {
        void progress(int status, int[] progress);

        void loadFinish(int downloadID);
    }
}
