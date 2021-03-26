package com.ws.support.http

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import com.ws.base.R
import com.ws.support.widget.MyProgressDialog
import java.util.*

/**
 * Created by curry on 2018/6/21.
 */
class ProgressHandler : Handler {
    //    public static DialogInterface.OnKeyListener onKeyListener = (dialog, keyCode, event) -> {
    ////        if (keyCode == KeyEvent.KEYCODE_BACK
    ////                && event.getAction() == KeyEvent.ACTION_DOWN) {
    ////            dismissDialog();
    ////        }
    //        return false;
    //    };
    private var pd: MyProgressDialog? = null
    private var mContext: Context
    private var mProgressCancelListener: ProgressCancelListener? = null
    private var cancelable: Boolean

    constructor(context: Context, cancelable: Boolean) {
        mContext = context
        this.cancelable = cancelable
    }

    constructor(context: Context, listener: ProgressCancelListener?, cancelable: Boolean) {
        mContext = context
        mProgressCancelListener = listener
        this.cancelable = cancelable
    }

    private fun initProgressDialog(message: String) {
        if (pd == null) {
            pd = MyProgressDialog.createProgrssDialog(mContext)
            pd?.setCancelable(cancelable)
            pd?.setMessage(message)
            pd?.setCanceledOnTouchOutside(false)
            //pd.setOnKeyListener(onKeyListener);
            pd?.window?.setGravity(Gravity.CENTER)
            if (cancelable) {
                pd!!.setOnCancelListener {
                    mProgressCancelListener?.onProgressCanceled()
                }
            }
            if (!pd!!.isShowing) {
                pd?.show() //显示进度条
            }
        }
    }

    private fun dismissProgressDialog() {
        pd?.dismiss() //取消进度条
        pd = null
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            SHOW_PROGRESS -> initProgressDialog(msg.data.getString("message",""))
            DISMISS_PROGRESS -> dismissProgressDialog()
        }
    }

    fun showProgressDialog() {
        val bundle = Bundle()
        bundle.putString("message", mContext.getString(R.string.tip_loading))
        val message = obtainMessage(SHOW_PROGRESS)
        message.data = bundle
        message.sendToTarget()
    }

    fun disMissProgressDialog() {
        obtainMessage(DISMISS_PROGRESS).sendToTarget()
    }

    //接口，用来取消进度条
    interface ProgressCancelListener {
        fun onProgressCanceled()
    }

    companion object {
        const val SHOW_PROGRESS = 0
        const val DISMISS_PROGRESS = 1
    }
}