package com.ws.support.widget

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ws.base.R
import java.util.*

/**
 * 等待窗口
 *
 * @author ws
 * @date 2020/6/2 14:12
 * 修改人：ws
 */
@Suppress("DEPRECATION")
class MyProgressDialogFragment : DialogFragment() {
    lateinit var progressBar: ContentLoadingProgressBar
    lateinit var tv: TextView
    var msg: String? = null
    private var mOnClickListener: DialogInterface.OnDismissListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_progress_fragment, container, false)
        progressBar = view.findViewById(R.id.progress)
        this.progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        tv = view.findViewById(R.id.tv_status)
        tv.text = msg
        if (msg!!.isEmpty()) {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //设置actionbar的隐藏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
            val window = getDialog()!!.window!!
            val windowParams = window.attributes
            windowParams.dimAmount = 0.0f
            window.attributes = windowParams
            //设置宽度
            val dm = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(dm)
            dialog.window?.setLayout((dm.widthPixels * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        mOnClickListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mOnClickListener != null) {
            mOnClickListener!!.onDismiss(dialog)
        }
    }

    fun setMessage(msg: String) {
        this.msg = msg
        tv.text = msg
        if (msg.isEmpty()) {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
        }

    }

    fun show(manager: FragmentManager?) {
        super.show(manager!!, MyProgressDialogFragment::class.java.simpleName)
    }

    companion object {
        fun newInstance(msg: String?): MyProgressDialogFragment {
            val dialog = MyProgressDialogFragment()
            dialog.msg = msg
            return dialog
        }
    }
}