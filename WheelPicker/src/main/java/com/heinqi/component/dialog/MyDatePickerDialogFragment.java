package com.heinqi.component.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.aigestudio.wheelpicker.R;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期滚轮弹窗
 *
 * @author ws
 * 2020/6/29 18:07
 * 修改人：ws
 */
public class MyDatePickerDialogFragment extends BaseDialogFragment implements WheelDatePicker.OnDateSelectedListener, View.OnClickListener {

    public static String Tag = "MyDatePickeDialogFragment";
    String title = "";
    OnDatePickerDialogListener listener;
    Calendar selectCalendar;
    private WheelDatePicker picker;
    private TextView        tvTitle;
    private Button          btnOK, btnCancel;
    private String date;

    public static MyDatePickerDialogFragment newInstance(String title, String date, OnDatePickerDialogListener l) {
        MyDatePickerDialogFragment dialog = new MyDatePickerDialogFragment();
        dialog.listener = l;
        dialog.title = title;
        dialog.date = date;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date_selector, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        btnOK = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        picker = view.findViewById(R.id.picker);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        picker.setYearFrame(1900, Calendar.getInstance().get(Calendar.YEAR));
        picker.setMaxDate(Calendar.getInstance());
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).parse(date);
                assert d != null;
                c.setTime(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        picker.setYearAndMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        picker.setSelectedDay(c.get(Calendar.DAY_OF_MONTH));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picker.setOnDateSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ok) {
            if (listener!=null){
                listener.onSelected(selectCalendar);
                dismiss();
            }
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    public void show(@NonNull FragmentManager manager) {
        show(manager, Tag);
    }

    @Override
    public void onDateSelected(WheelDatePicker picker, Date date) {
        selectCalendar = Calendar.getInstance();
        selectCalendar.setTime(date);
        String string = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(date);
    }
}
