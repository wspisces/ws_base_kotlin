package com.heinqi.component.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.aigestudio.wheelpicker.R;
import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 2个滚轮弹窗
 *
 * @author ws
 * 2020/6/29 18:07
 * 修改人：ws
 */
public class MyTwoPickerDialogFragment extends BaseDialogFragment implements WheelPicker.OnItemSelectedListener, View.OnClickListener {

    String title= "";
    public static String      Tag = "MyPickeDialogFragment";
    private       WheelPicker picker1;
    private       WheelPicker picker2;
    private       TextView    tvTitle;
    private       Button      btnOK,btnCancel;
    List<String> data1 = new ArrayList<>();
    List<List<String>> data2 = new ArrayList<>();
    public static MyTwoPickerDialogFragment newInstance(String title, List<String> data1, List<List<String>> data2,OnTwoPickerDialogListener l) {
        MyTwoPickerDialogFragment dialog = new MyTwoPickerDialogFragment();
        dialog.listener = l;
        if (data1 == null)data1 = new ArrayList<>();
        dialog.data1 = data1;
        if (data2 == null)data2 = new ArrayList<>();
        dialog.data2 = data2;
        dialog.title = title;
        return dialog;
    }

    OnTwoPickerDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selector_two_picker, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        btnOK = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        picker1 = view.findViewById(R.id.picker1);
        picker2 = view.findViewById(R.id.picker2);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picker1.setData(data1);
        if (!data1.isEmpty()) {
            picker1.setSelectedItemPosition(0);
            selectData1 = data1.get(0);
            selectIndex1 = 0;
            if (!data2.isEmpty()){
                selectData2 = data2.get(selectIndex1).get(0);
                selectIndex2 = 0;
                picker2.setData(data2.get(selectIndex1));
                picker2.setSelectedItemPosition(0);
            }
        }
        picker1.setOnItemSelectedListener(this);
        picker2.setOnItemSelectedListener(this);
    }
    String selectData1;
    int selectIndex1 = -1;
    String selectData2;
    int selectIndex2 = -1;
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        //Logger.i("data="+data+" position="+position);
        if (picker == picker1) {
            selectData1 = data.toString();
            selectIndex1 = position;
            if (!data2.isEmpty()){
                picker2.setData(data2.get(selectIndex1));
                picker2.setSelectedItemPosition(0);
                selectData2 = data2.get(selectIndex1).get(0);
                selectIndex2 = 0;
            }
        }else{
            selectData2 = data.toString();
            selectIndex2 = position;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ok){
            if (selectIndex1 > -1){
                listener.onSelected(selectData1,selectIndex1,selectData2,selectIndex2);
                dismiss();
            }
        }else if (id == R.id.btn_cancel){
            dismiss();
        }
    }

    public void show(@NonNull FragmentManager manager){
        if (data1.isEmpty()){
            Toast.makeText(getActivity(),"没有数据可以选择",Toast.LENGTH_SHORT).show();
            return;
        }
        show(manager,Tag);
    }

}
