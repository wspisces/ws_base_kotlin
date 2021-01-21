package com.ws.component.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ws.base.R
import com.ws.support.utils.ScreenUtils
import com.ws.support.utils.StringUtils
import com.ws.support.utils.ToastUtils

/**
 * 输入弹窗
 *
 * @author ws
 * 2020/6/29 17:57
 * 修改人：ws
 */
class InputDialogFragment private constructor() : DialogFragment() {
    lateinit var tvTitle: TextView
    lateinit var btnCancel: Button
    lateinit var btnOk: Button
    lateinit var et: EditText
    var title = ""
    var errorMsg = ""
    var hint = ""
    var defaultContent = ""
    var cancelAble:Boolean = true

    var listener: OnInputDialogFragmentListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.Dialog_Fullscreen_Bottom)
        //dialog.setCanceledOnTouchOutside(true);
        //dialog.setCancelable(true);
        //设置actionbar的隐藏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_input, container, false)
        tvTitle = view.findViewById(R.id.tv_title)
        et = view.findViewById(R.id.et)
        et.hint = hint
        et.setText(defaultContent)
        if (defaultContent.length > 0) {
            et.setSelection(defaultContent.length)
        }
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnOk = view.findViewById(R.id.btn_ok)
        tvTitle.text = title
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 必须设置这两个,才能设置宽度
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        // 遮罩层透明度
        dialog!!.window!!.setDimAmount(0.8f)
        dialog!!.setCancelable(cancelAble)
        dialog!!.setCanceledOnTouchOutside(cancelAble)
        val window = dialog!!.window
        //window.getDecorView().setPadding(0, 0, 0, 0);
        val attributes = window!!.attributes
        attributes.width = (ScreenUtils.getScreenWidth(activity) * 0.8f).toInt()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.dimAmount = 0.7f
        attributes.gravity = Gravity.CENTER
        dialog!!.window!!.attributes = attributes
        return view
    }

    fun show(manager: FragmentManager) {
        show(manager, "InputDialogFragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnCancel.setOnClickListener { listener?.onCancel(this) }
        btnOk.setOnClickListener {
            if (et.text.length > 0) {
                listener?.onCommit(this, et.text.toString())
            } else {
                if (StringUtils.isNotEmptyWithNull(errorMsg)) ToastUtils.error(errorMsg)
            }
        }
    }

    interface OnInputDialogFragmentListener {
        fun onCommit(dialogFragment: DialogFragment?, str: String?)
        fun onCancel(dialogFragment: DialogFragment?)
    }

    companion object {
        fun newInstance(title: String, defaultContent: String, hint: String, error: String, listener: OnInputDialogFragmentListener): InputDialogFragment {
            val dialog = InputDialogFragment()
            dialog.title = title
            dialog.hint = hint
            dialog.errorMsg = error
            dialog.defaultContent = defaultContent
            dialog.listener = listener
            return dialog
        }

        fun newInstance(title: String, defaultContent: String, hint: String, error: String, cancelable: Boolean, listener: OnInputDialogFragmentListener): InputDialogFragment {
            val dialog = InputDialogFragment()
            dialog.title = title
            dialog.hint = hint
            dialog.errorMsg = error
            dialog.defaultContent = defaultContent
            dialog.cancelAble = cancelable
            dialog.listener = listener
            return dialog
        }
    }
}