package com.colin.demo.app.manager;

import android.content.Context;

import com.colin.demo.app.dialog.DialogProgressBar;

public class DialogManager {
    /*耗时操作进度弹框*/
    private DialogProgressBar mDialogProgressBar;

    public static class Holder {
        static DialogManager instance = new DialogManager();
    }

    private DialogManager() {
    }

    public static DialogManager getInstance() {
        return DialogManager.Holder.instance;
    }

    public void showProgressDialog(Context context) {
        if (null != mDialogProgressBar && mDialogProgressBar.isShowing()) {
            return;
        }
        if (null == mDialogProgressBar) {
            mDialogProgressBar = new DialogProgressBar(context);
        }
        mDialogProgressBar.show();
    }

    public void closeProgressDialog() {
        if (null != mDialogProgressBar && mDialogProgressBar.isShowing()) {
            mDialogProgressBar.dismiss();
        }
        if (null != mDialogProgressBar) {
            mDialogProgressBar = null;
        }
    }
}
