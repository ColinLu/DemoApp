package com.colin.demo.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.colin.demo.app.manager.DialogManager;

import java.lang.ref.WeakReference;

public class CustomAsync extends AsyncTask<Void, VerifyError, String> {
    private WeakReference<Context> mWeakReference;
    private String url;
    private OnTaskListener mOnTaskListener;

    public CustomAsync(Context context) {
        this(context, null, null);
    }

    public CustomAsync(Context context, OnTaskListener onTaskListener) {
        this(context, null, onTaskListener);
    }

    public CustomAsync(Context context, String url, OnTaskListener onTaskListener) {
        this.url = url;
        this.mWeakReference = new WeakReference<>(context);
        this.mOnTaskListener = onTaskListener;
    }

    /**
     * 子线程开始之前 主线程操作
     */
    @Override
    protected void onPreExecute() {
        DialogManager.getInstance().showProgressDialog(mWeakReference.get());
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        if (null != mOnTaskListener) {
            mOnTaskListener.callBack(false, "取消操作");
        }
        mWeakReference.clear();
        mWeakReference = null;
        this.mOnTaskListener = null;
        this.url = null;
        DialogManager.getInstance().closeProgressDialog();
    }


    /**
     * 更新进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(VerifyError... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 子线程 后台操作
     *
     * @param voids
     * @return
     */
    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }

    /**
     * 操作完成 注意取消操作（空值判断）
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        if (null == mWeakReference || null == mWeakReference.get() || null == mOnTaskListener) {
            DialogManager.getInstance().closeProgressDialog();
            return;
        }
        mOnTaskListener.callBack(true, result);
        DialogManager.getInstance().closeProgressDialog();
    }

    public interface OnTaskListener {
        void callBack(boolean success, String value);
    }
}
