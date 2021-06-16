package com.ws.support.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.ws.base.R
import com.ws.support.base.BaseApplication
import java.util.*

/**
 * @author curry
 */
@SuppressLint("StaticFieldLeak")
object EasyToastUtil {
    var toast: Toast? = null
    var tv: TextView? = null
    @SuppressLint("StaticFieldLeak", "InflateParams")
    fun showToast(context: Context?, message: String?, longToast: Boolean) {
        if (null == toast) {
            toast = Toast(context)
            val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
            tv = view.findViewById(R.id.tv_toast)
            toast!!.view = view
            toast!!.setGravity(Gravity.CENTER, 0, 0)
        }
        if (longToast) {
            toast!!.duration = Toast.LENGTH_LONG
        } else {
            toast!!.duration = Toast.LENGTH_SHORT
        }
        tv!!.text = message
        toast!!.show()
    }

    private const val DEBUG = false
    fun debug(message: String?) {
        if (DEBUG) showToast(BaseApplication.getInstance(), message, false)
    }

    fun showToast(message: String?) {
        if (StringUtils.isNotEmptyWithNull(message)) {
            showToast(BaseApplication.getInstance(), message, false)
        }
    }

    fun showToast(message: String?, time: Long) {
        if (!StringUtils.isNotEmptyWithNull(message)) {
            return
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                toast!!.cancel()
            }
        }, time)
        showToast(BaseApplication.getInstance(), message, false)
    }

    fun showToast(context: Context?, message: String?) {
        if (!StringUtils.isNotEmptyWithNull(message)) {
            return
        }
        showToast(context, message, false)
    }

    /**
     * @param context
     * @param resId   ---资源id
     */
    fun showToastById(context: Context?, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }
}