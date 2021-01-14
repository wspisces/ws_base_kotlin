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
 * 滚轮弹窗
 *
 * @author ws
 * 2020/6/29 18:07
 * 修改人：ws
 */
public class MyPickeDialogFragment extends BaseDialogFragment implements WheelPicker.OnItemSelectedListener, View.OnClickListener {

    String title= "";
    public static String      Tag = "MyPickeDialogFragment";
    private       WheelPicker picker;
    private       TextView    tvTitle;
    private       Button      btnOK,btnCancel;
    List<String> data = new ArrayList<>();
    public static MyPickeDialogFragment newInstance(String title, List<String> data, OnPickerDialogListener l) {
        MyPickeDialogFragment dialog = new MyPickeDialogFragment();
        dialog.listener = l;
        if (data == null)data = new ArrayList<>();
        dialog.data = data;
        dialog.title = title;
        return dialog;
    }

    OnPickerDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selector, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        btnOK = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        picker = view.findViewById(R.id.picker);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picker.setData(data);
        if (!data.isEmpty()) {
            picker.setSelectedItemPosition(0);
            selectData = data.get(0);
            selectIndex = 0;
        }
        picker.setOnItemSelectedListener(this);
    }
    String selectData;
    int selectIndex = -1;
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        //Logger.i("data="+data+" position="+position);
        selectData = data.toString();
        selectIndex = position;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ok){
            if (selectIndex > -1){
                listener.onSelected(selectData,selectIndex);
                dismiss();
            }
        }else if (id == R.id.btn_cancel){
            dismiss();
        }
    }

    public void show(@NonNull FragmentManager manager){
        if (data.isEmpty()){
            Toast.makeText(getActivity(),"没有数据可以选择",Toast.LENGTH_SHORT).show();
            return;
        }
        show(manager,Tag);
    }

}
