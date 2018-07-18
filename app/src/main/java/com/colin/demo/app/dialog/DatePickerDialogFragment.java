package com.colin.demo.app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.colin.demo.app.R;
import com.colin.demo.app.widget.picker.DatePicker;

/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DatePickerDialogFragment extends DialogFragment {

    protected DatePicker mDatePicker;
    protected Button mCancelButton, mDecideButton;

    private int mSelectedYear = -1, mSelectedMonth = -1, mSelectedDay = -1;
    private OnDateChooseListener mOnDateChooseListener;
    private boolean mIsShowAnimation = true;

    public DatePickerDialogFragment setSelectedDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
        setSelectedDate();
        return this;
    }

    public DatePickerDialogFragment setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
        return this;
    }

    public DatePickerDialogFragment showAnimation(boolean show) {
        mIsShowAnimation = show;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date, container);


        mDatePicker = view.findViewById(R.id.dayPicker_dialog);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDecideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(mDatePicker.getYear(),
                            mDatePicker.getMonth(), mDatePicker.getDay());
                }
                dismiss();
            }
        });

        if (mSelectedYear > 0) {
            setSelectedDate();
        }
        initChild();
        return view;
    }

    protected void initChild() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DatePickerBottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_date);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        if (window != null) {
            if (mIsShowAnimation) {
                window.getAttributes().windowAnimations = R.style.DatePickerDialogAnim;
            }
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM; // 紧贴底部
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            layoutParams.dimAmount = 0.35f;
            window.setAttributes(layoutParams);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }


    private void setSelectedDate() {
        if (mDatePicker != null) {
            mDatePicker.setDate(mSelectedYear, mSelectedMonth, mSelectedDay, false);
        }
    }

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day);
    }


}
