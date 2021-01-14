package com.ws.component.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.ws.base.R

/**
 * 描述信息
 *
 * @author ws
 * 2020/6/29 17:57
 * 修改人：ws
 */
abstract class BaseDialogFragment : DialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val window = dialog!!.window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.dimAmount = 0.8f
        attributes.gravity = Gravity.BOTTOM
        window.attributes = attributes
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.Dialog_Fullscreen_Bottom)
        dialog.setCanceledOnTouchOutside(true)
        //设置actionbar的隐藏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}