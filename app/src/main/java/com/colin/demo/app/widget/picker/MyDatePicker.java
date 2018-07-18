package com.colin.demo.app.widget.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.colin.demo.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 年份选择器
 * Created by ycuwq on 17-12-27.
 */
@SuppressWarnings("unused")
public class MyDatePicker extends WheelPicker<Integer> {
    private static final int DATE_TYPE_DEFAULT = 0;
    private static final int DATE_TYPE_YEAR = 1;
    private static final int DATE_TYPE_MONTH = 2;
    private static final int DATE_TYPE_DAY = 3;
    private static final int DATE_TYPE_HOUR = 4;
    private static final int DATE_TYPE_MINUTE = 5;
    private static final int DATE_TYPE_SECOND = 6;


    private int dateType = DATE_TYPE_DEFAULT;       //时间类型
    private int selectValue = 0;                    //选中值
    private int startValue = 0;                      //开始值
    private int endValue = 0;                        //结束值
    private OnValueSelectedListener mOnYearSelectedListener;

    public MyDatePicker(Context context) {
        this(context, null);
    }

    public MyDatePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();

    }


    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyDatePicker);
        dateType = a.getInteger(R.styleable.MyDatePicker_dateType, DATE_TYPE_DEFAULT);
        selectValue = a.getInteger(R.styleable.MyDatePicker_selectValue, selectValue);
        startValue = a.getInteger(R.styleable.MyDatePicker_startValue, startValue);
        endValue = a.getInteger(R.styleable.MyDatePicker_endValue, endValue);
        a.recycle();
    }

    private void initView() {
        setItemMaximumWidthText(getItemMaxinumWidthText());
        updateValue();
        setSelectedValue(selectValue, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                selectValue = item;
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearSelected(item);
                }
            }
        });
    }

    private String getItemMaxinumWidthText() {
        switch (dateType) {
            case DATE_TYPE_YEAR:
                return "0000";
            case DATE_TYPE_MONTH:
            case DATE_TYPE_DAY:
            case DATE_TYPE_HOUR:
            case DATE_TYPE_MINUTE:
            case DATE_TYPE_SECOND:
            case DATE_TYPE_DEFAULT:
                return "00";
            default:
                return "00";
        }
    }

    private void updateValue() {
        List<Integer> list = new ArrayList<>();
        for (int i = startValue; i <= endValue; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setValue(int startValue, int endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
        updateValue();
    }

    public void setSelectedValue(int selectedYear) {
        setSelectedValue(selectedYear, true);
    }

    public void setSelectedValue(int selectedYear, boolean smoothScroll) {
        setCurrentPosition(getSelectPosition(), smoothScroll);
    }

    public int getSelectPosition() {
        List<Integer> dataList = getDataList();
        if (null == dataList || dataList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) == selectValue) {
                return i;
            }
        }
        return 0;
    }

    public int getSelectedYear() {
        return selectValue;
    }

    public void setOnYearSelectedListener(OnValueSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    public interface OnValueSelectedListener {
        void onYearSelected(int year);
    }

}
