package com.colin.demo.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.callback.IDialogCallBack;
import com.colin.demo.app.utils.MetricsUtil;


/**
 * Created by Administrator on 2017/8/17.
 */

public class DialogTips extends Dialog implements View.OnClickListener {
    private CharSequence mMessage;
    private int messageRes = R.string.app_name;
    private boolean isOutViewCancel = false;
    private IDialogCallBack mIDialogCallBack;


    private ImageView image_dialog_del;
    private TextView text_dialog_content;

    public DialogTips setMessage(CharSequence message) {
        this.mMessage = message;
        return this;
    }

    public DialogTips setMessage(int messageRes) {
        this.messageRes = messageRes;
        return this;
    }

    public DialogTips setOutViewCancel(boolean outViewCancel) {
        isOutViewCancel = outViewCancel;
        return this;
    }

    public DialogTips setIDialogCallBack(IDialogCallBack IDialogCallBack) {
        mIDialogCallBack = IDialogCallBack;
        return this;
    }

    public DialogTips(@NonNull Context context) {
        this(context, R.style.DialogStyle);
    }

    public DialogTips(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_label_tips);
        initView();
        initData();
        this.setCancelable(isOutViewCancel);
        this.setCanceledOnTouchOutside(isOutViewCancel);

        Window window = this.getWindow();
        if (null == window) {
            return;
        }
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (null == layoutParams) {
            return;
        }
        layoutParams.width = (int) (MetricsUtil.getWindowsWidth(getContext()) * 0.95);
        layoutParams.height = (int) (MetricsUtil.getWindowsHeight(getContext()) * 0.55);
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
    }


    private void initView() {
        image_dialog_del = findViewById(R.id.image_dialog_del);
        text_dialog_content = findViewById(R.id.text_dialog_content);
        image_dialog_del.setOnClickListener(this);
        text_dialog_content.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    private void initData() {
        if (null != text_dialog_content) {
            text_dialog_content.setText(messageRes);
        }
        if (null != text_dialog_content && null != mMessage) {
            text_dialog_content.setText(mMessage);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (null != mIDialogCallBack) {
            mIDialogCallBack.click(v, v.getId() == R.id.image_dialog_del, mMessage);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
