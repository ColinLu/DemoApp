package com.colin.demo.app.activity.picker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.dialog.DatePickerDialogFragment;
import com.colin.demo.app.widget.picker.DatePicker;

public class PickerActivity extends BaseActivity {
    private ItemBean mItemBean;
    private TextView text_picker_value;
    private DatePicker picker_date_show;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
    }

    @Override
    protected void initView() {
        text_picker_value = this.findViewById(R.id.text_picker_value);
        picker_date_show = this.findViewById(R.id.picker_date_show);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);

    }

    @Override
    protected void initListener() {
        picker_date_show.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateSelected(int year, int month, int day) {
                text_picker_value.setText(String.format("%4d年%2d月%2d日", year, month, day));
            }
        });
    }

    @Override
    protected void initAsync() {

    }

    public void showDatePickerDialog(View view) {
        new DatePickerDialogFragment().setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateChoose(int year, int month, int day) {
                text_picker_value.setText(String.format("%4d年%2d月%2d日", year, month, day));
            }
        }).show(getSupportFragmentManager(), "DatePickerDialogFragment");
    }
}
