@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.view.Gravity
import android.widget.Toast
import com.ws.base.BuildConfig
import com.ws.support.base.BaseApplication
import es.dmoral.toasty.Toasty

/**
 * Toasty封装
 *
 * @author ws
 * 2020/8/5 21:26
 * 修改人：ws
 */
object ToastUtils {
    var toast: Toast? = null

    //    @SuppressLint("HandlerLeak")
//    private val handler: Handler = object : Handler() {
//        override fun dispatchMessage(msg: Message) {
//            super.dispatchMessage(msg)
//            toast.cancel()
//        }
//    }
    var isCenter = true
    fun isCenter(center: Boolean) {
        isCenter = center
    }

    fun cancel() {
        toast?.cancel()
    }

    private fun _show(){
        if (isCenter){
            toast?.setGravity(Gravity.CENTER, 0, 0)
        }
        toast?.show()
    }

    //显示短时间toast
    fun warn(msg: String) {
        toast = Toasty.warning(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true)
        _show()
    }

    fun warnL(msg: String) {
        toast = Toasty.warning(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true)
        _show()
    }

    //显示短时间toast
    fun error(msg: String) {
        if (StringUtils.isEmptyWithNull(msg))return
        toast = Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true)
        _show()
    }

    fun error(msg: Int) {
        toast = Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true)
        _show()
    }

    fun errorL(msg: String) {
        toast = Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true)
        _show()
    }

    fun success(msg: String) {
        toast = Toasty.success(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true)
        _show()
    }

    fun successL(msg: String) {
        toast = Toasty.success(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true)
        _show()
    }

    fun info(msg: String) {
        toast = Toasty.info(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true)
        _show()
    }

    fun infoL(msg: String) {
        toast = Toasty.info(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true)
        _show()
    }

    fun normal(msg: Int) {

        toast = Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT)
        _show()
    }

    fun normal(msg: String) {
        if (StringUtils.isEmptyWithNull(msg))return
        toast = Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT)
        _show()
    }

    fun normalL(msg: String) {
        toast = Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG)
        _show()
    }


//    @SuppressLint("ShowToast")
//    fun shortT(msg: String) {
//        toast.setText(msg)
//        toast.show()
//        handler.removeMessages(0)
//        handler.sendEmptyMessageDelayed(0, 200)
//    }

    fun debug(msg: String) {
        if (BuildConfig.DEBUG) {
            toast = Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT)
            toast!!.setGravity(Gravity.CENTER, 0, 0)
            toast!!.show()
        }
    }
}