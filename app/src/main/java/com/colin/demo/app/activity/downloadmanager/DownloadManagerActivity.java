package com.colin.demo.app.activity.downloadmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.download.AppUpdateManager;
import com.colin.demo.app.download.UpdaterConfig;
import com.colin.demo.app.utils.LogUtil;
import com.colin.demo.app.utils.PermissionUtil;

import java.util.Arrays;

public class DownloadManagerActivity extends BaseActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
    }

    @Override
    protected void initView() {
        this.mTextView = this.findViewById(R.id.text_download_state);
    }

    @Override
    protected void initData() {
        setTitle(R.string.download);
        mTextView.setText("http://192.168.1.250:81/upload/fenqi0625.apk");
    }

    @Override
    protected void initListener() {
        findViewById(R.id.button_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downApp();
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
