package com.ws.component.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ws.base.R
import com.ws.support.utils.ScreenUtils

/**
 * 消息弹窗
 *
 * @author ws
 * 2020/8/17 19:14
 * 修改人：ws
 */
class AlertDialogFragment private constructor() : DialogFragment() {
    lateinit var  tvTitle: TextView
    lateinit var btnCancel: Button
    lateinit var btnOk: Button
    var title = ""
    var listener: OnAlertDialogFragmentListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.Dialog_Fullscreen_Bottom)
        dialog.setCanceledOnTouchOutside(true)
        //设置actionbar的隐藏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_alert, container, false)
        tvTitle = view.findViewById(R.id.tv_title)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnOk = view.findViewById(R.id.btn_ok)
        tvTitle.setText(title)
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 必须设置这两个,才能设置宽度
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        // 遮罩层透明度
        dialog!!.window!!.setDimAmount(0.8f)
        val window = dialog!!.window
        //window.getDecorView().setPadding(0, 0, 0, 0);
        val attributes = window!!.attributes
        attributes.width = (ScreenUtils.Companion.getScreenWidth(activity) * 0.8f) as Int
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
        btnCancel!!.setOnClickListener { view: View? -> dismiss() }
        btnOk!!.setOnClickListener { view: View? -> listener!!.onCommit(this) }
    }

    interface OnAlertDialogFragmentListener {
        fun onCommit(dialogFragment: DialogFragment?)
    }

    companion object {
        fun newInstance(title: String, listener: OnAlertDialogFragmentListener): AlertDialogFragment {
            val dialog = AlertDialogFragment()
            dialog.title = title
            dialog.listener = listener
            return dialog
        }
    }
}