package com.colin.demo.app.activity.picker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.dialog.DatePickerDialogFragment;
import com.colin.demo.app.widget.picker.DatePicker;

public class PickerActivity extends BaseActivity {
    private ItemBean mItemBean;
    private TextView text_picker_value;
    private DatePicker picker_date_show;
    private NumberPicker number_picker_show;
    private TimePicker time_picker_show;
    private android.widget.DatePicker date_picker_show;


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
        number_picker_show = this.findViewById(R.id.number_picker_show);
        time_picker_show = this.findViewById(R.id.time_picker_show);
        date_picker_show = this.findViewById(R.id.date_picker_show);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);
        initNumberPickerView();
        initTimerPickerView();
        initDatePickerView();
    }

    private void initNumberPickerView() {
        number_picker_show.setMinValue(0);
        number_picker_show.setMaxValue(100);
        number_picker_show.setValue(21);
        number_picker_show.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%03d", value);
            }
        });

        number_picker_show.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                text_picker_value.setText(String.format("选中值:%03d", newVal));
            }
        });
    }

    private void initTimerPickerView() {
        time_picker_show.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            time_picker_show.setHour(10);
            time_picker_show.setMinute(10);
        } else {
            time_picker_show.setCurrentHour(10);
            time_picker_show.setCurrentMinute(50);
        }
        time_picker_show.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                text_picker_value.setText(String.format("选择时间:%02d时%02d分", hourOfDay, minute));
            }
        });
    }


    private void initDatePickerView() {
        date_picker_show.setMinDate(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 365 * 20);
        date_picker_show.setMaxDate(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date_picker_show.setOnDateChangedListener(new android.widget.DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    text_picker_value.setText(String.format("选择时间:%04d年%02d月%02d日", year, monthOfYear,dayOfMonth));
                }
            });
        }
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
