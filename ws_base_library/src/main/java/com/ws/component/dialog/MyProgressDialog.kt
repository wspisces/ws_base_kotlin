package com.ws.component.dialog

import android.content.Context
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.ws.base.R

/**
 * dialog
 *
 * @author ws
 * @date 2020/6/5 14:01
 * 修改人：ws
 */
@Suppress("DEPRECATION")
class MyProgressDialog private constructor(context: Context, theme: Int) : AppCompatDialog(context, theme) {
    fun setMessage(msg: String?): MyProgressDialog? {
        val loadingTextView = dialog!!.findViewById<TextView>(R.id.tv_status)
        if (!TextUtils.isEmpty(msg)) {
            loadingTextView!!.text = msg
            loadingTextView.visibility = View.VISIBLE
        } else loadingTextView!!.visibility = View.GONE
        return dialog
    }

    companion object {
        private var dialog: MyProgressDialog? = null
        fun createProblemDialog(context: Context, message: String?): MyProgressDialog? {
            dialog = MyProgressDialog(context, R.style.base_CommonDialog)
            dialog!!.setContentView(R.layout.dialog_progress)
            val loadingTextView = dialog!!.findViewById<TextView>(R.id.tv_status)
            if (!TextUtils.isEmpty(message)) {
                loadingTextView!!.text = message
                loadingTextView.visibility = View.VISIBLE
            } else loadingTextView!!.visibility = View.GONE
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            return dialog
        }

        fun setWidth(dialog: AppCompatDialog, context: Context) {
            val dm = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).also {
                it.defaultDisplay.getMetrics(dm)
            }
            val params = dialog.window!!.attributes
            params.width = (dm.widthPixels * 0.6).toInt()
            //params.height = (int) (dm.widthPixels * 0.9);
            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}