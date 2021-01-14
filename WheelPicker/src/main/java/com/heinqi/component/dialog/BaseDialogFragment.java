package com.heinqi.component.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.aigestudio.wheelpicker.R;

/**
 * 描述信息
 *
 * @author ws
 * 2020/6/29 17:57
 * 修改人：ws
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.8f;
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Fullscreen_Bottom);
        dialog.setCanceledOnTouchOutside(true);
        //设置actionbar的隐藏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


}
