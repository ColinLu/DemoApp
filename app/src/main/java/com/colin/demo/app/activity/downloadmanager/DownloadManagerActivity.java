package com.colin.demo.app.activity.downloadmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.download.AppUpdateManager;
import com.colin.demo.app.download.UpdaterConfig;
import com.colin.demo.app.utils.AppUtil;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.PermissionUtil;
import com.colin.demo.app.widget.NumberProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;

public class DownloadManagerActivity extends BaseActivity {
    private TextView text_download_state;
    private TextView downloadSize;
    private TextView netSpeed;
    private TextView tvProgress;
    private NumberProgressBar progress_down_value;
    private NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
    }

    @Override
    protected void initView() {
        this.text_download_state = this.findViewById(R.id.text_download_state);
        this.downloadSize = this.findViewById(R.id.downloadSize);
        this.netSpeed = this.findViewById(R.id.netSpeed);
        this.tvProgress = this.findViewById(R.id.tvProgress);
        this.progress_down_value = this.findViewById(R.id.progress_down_value);
    }

    @Override
    protected void initData() {
        setTitle(R.string.download);
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.button_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downApp();
            }


        });
        findViewById(R.id.button_download_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDownload("http://192.168.1.250:81/upload/fenqi0625.apk");
            }


        });
    }

    private void customDownload(String url) {
        //适配Android 8.0
        boolean canRequestPackageInstalls = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
            LogUtil.e("canRequestPackageInstalls-->>" + String.valueOf(canRequestPackageInstalls));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && !canRequestPackageInstalls) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            startActivityForResult(intent, Constants.REQUEST_CODE_INSTALL);
            return;
        }
        if (!PermissionUtil.getInstance().checkPermission(this, Arrays.asList(PermissionUtil.PERMISSIONS_STORAGE), PermissionUtil.REQUEST_CODE_STORAGE)) {
            return;
        }
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback("App.apk") {

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        text_download_state.setText("正在下载中");
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        text_download_state.setText("下载完成-->>" + response.body().getAbsolutePath());
                        AppUtil.installApk(DownloadManagerActivity.this, response.body());
                    }

                    @Override
                    public void onError(Response<File> response) {
                        text_download_state.setText("下载出错");

                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                        String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                        downloadSize.setText(downloadLength + "/" + totalLength);
                        String speed = Formatter.formatFileSize(getApplicationContext(), progress.speed);
                        netSpeed.setText(String.format("%s/s", speed));
                        tvProgress.setText(numberFormat.format(progress.fraction));
                        progress_down_value.setMax(10000);
                        progress_down_value.setProgress((int) (progress.fraction * 10000));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                });
    }

    @Override
    protected void initAsync() {

    }

    private void downApp() {
        //适配Android 8.0
        boolean canRequestPackageInstalls = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
            LogUtil.e("canRequestPackageInstalls-->>" + String.valueOf(canRequestPackageInstalls));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && !canRequestPackageInstalls) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            startActivityForResult(intent, Constants.REQUEST_CODE_INSTALL);
            return;
        }
        if (!PermissionUtil.getInstance().checkPermission(this, Arrays.asList(PermissionUtil.PERMISSIONS_STORAGE), PermissionUtil.REQUEST_CODE_STORAGE)) {
            return;
        }
        UpdaterConfig config = new UpdaterConfig.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setDescription(getString(R.string.system_download_description))
                .setFilename("app.apk")
                .setFileUrl("http://192.168.1.250:81/upload/fenqi0625.apk")
                .setCanMediaScanner(true)
                .build();
        AppUpdateManager.getInstance().download(config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_INSTALL) {
            if (resultCode == Activity.RESULT_OK) {
                downApp();
                return;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.getInstance().onRequestPermissionsResult(PermissionUtil.REQUEST_CODE_STORAGE, grantResults)) {
            downApp();
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
