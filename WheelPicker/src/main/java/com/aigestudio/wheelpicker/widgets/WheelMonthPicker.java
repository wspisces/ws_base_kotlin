package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * 月份选择器
 * <p>
 * Picker for Months
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelMonthPicker extends WheelPicker implements IWheelMonthPicker {
    private int mSelectedMonth;
    Calendar maxCalendar;
    private int mYear;
    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            data.add(i);
        super.setData(data);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    @Override
    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getCurrentMonth() {
        return Integer.parseInt(String.valueOf(getData().get(getCurrentItemPosition())));
    }

    @Override
    public void setMaxDate(Calendar c) {
        maxCalendar = c;
    }

    public void setYear(int year) {
        mYear = year;
        List<Integer> data = new ArrayList<>();
        int month = 12;
        if (maxCalendar != null && maxCalendar.get(Calendar.YEAR) == mYear){
             month = maxCalendar.get(Calendar.MONTH) + 1;
        }
        for (int i = 1; i <= month; i++) {
            data.add(i);
        }
        super.setData(data);
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }
}