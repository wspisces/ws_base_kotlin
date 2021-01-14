package com.ws.support.widget

import android.app.Dialog
import android.content.Context
import android.graphics.PorterDuff
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.ws.base.R
import java.util.*

/**
 * 描述信息
 *
 * @author ws
 * @date 2020/6/5 14:01
 * 修改人：ws
 */
class MyProgressDialog private constructor(context: Context, theme: Int) : Dialog(context, theme) {
    fun setMessage(msg: String?): MyProgressDialog? {
        val loadingTextView = dialog!!.findViewById<TextView>(R.id.tv_status)
        if (!TextUtils.isEmpty(msg)) {
            loadingTextView.text = msg
            loadingTextView.visibility = View.VISIBLE
        } else loadingTextView.visibility = View.GONE
        return dialog
    }

    companion object {
        private var dialog: MyProgressDialog? = null
        fun createProgrssDialog(context: Context): MyProgressDialog? {
            dialog = MyProgressDialog(context, R.style.base_CommonDialog)
            dialog!!.setContentView(R.layout.dialog_progress)
            val progressBar: ContentLoadingProgressBar = dialog!!.findViewById(R.id.progress)
            progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
            dialog!!.window?.attributes?.gravity = Gravity.CENTER
            return dialog
        }

        fun setWidth(dialog: Dialog, context: Context) {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(dm)
            val params = dialog.window!!.attributes
            params.width = (dm.widthPixels * 0.7).toInt()
            //params.height = (int) (dm.widthPixels * 0.9);
            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}