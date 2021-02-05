@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.widget.Toast
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
    //    private static void show(String msg, ToatType type, int time, boolean witnIcon) {
    //        if (TextUtils.isEmpty(msg)) {
    //            Logger.e("提示信息不能为空或者null");
    //            return;
    //        }
    //        Context context = BaseApplication.getInstance();
    //        switch (type) {
    //            case Info:
    //                Toasty.info(context, msg, time, witnIcon);
    //                break;
    //            case Normal:
    //                Toasty.normal(context, msg, time);
    //                break;
    //            case Warning:
    //                Toasty.warning(context, msg, time, witnIcon);
    //                break;
    //            case Success:
    //                Toasty.success(context, msg, time, witnIcon);
    //                break;
    //            case Error:
    //                Toasty.error(context, msg, time, witnIcon);
    //                break;
    //        }
    //    }
    //
    //    //显示短时间toast
    //    public static void warn(String msg) {
    //        show(msg, ToatType.Warning, Toasty.LENGTH_SHORT, true);
    //    }
    //
    //    public static void warnL(String msg) {
    //        show(msg, ToatType.Warning, Toasty.LENGTH_LONG, true);
    //    }
    //
    //    //显示短时间toast
    //    public static void error(String msg) {
    //        show(msg, ToatType.Error, Toasty.LENGTH_SHORT, true);
    //    }
    //
    //    public static void errorL(String msg) {
    //        show(msg, ToatType.Error, Toasty.LENGTH_LONG, true);
    //    }
    //
    //    public static void success(String msg) {
    //        show(msg, ToatType.Success, Toasty.LENGTH_SHORT, true);
    //    }
    //
    //    public static void successL(String msg) {
    //        show(msg, ToatType.Success, Toasty.LENGTH_LONG, true);
    //    }
    //
    //    public static void info(String msg) {
    //        show(msg, ToatType.Info, Toasty.LENGTH_SHORT, true);
    //    }
    //
    //    public static void infoL(String msg) {
    //        show(msg, ToatType.Info, Toasty.LENGTH_LONG, true);
    //    }
    //
    //    public static void normal(String msg) {
    //        show(msg, ToatType.Normal, Toasty.LENGTH_SHORT, true);
    //    }
    //
    //    public static void normalL(String msg) {
    //        show(msg, ToatType.Normal, Toasty.LENGTH_LONG, true);
    //    }
    //
    //    enum ToatType {
    //        Normal, Info, Warning, Success, Error
    //    }
    lateinit var toast: Toast

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            toast.cancel()
        }
    }

    //显示短时间toast
    fun warn(msg: String) {
        Toasty.warning(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true).show()
    }

    fun warnL(msg: String) {
        Toasty.warning(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true).show()
    }

    //显示短时间toast
    fun error(msg: String) {
        Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true).show()
    }

    fun error(msg: Int) {
        Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true).show()
    }

    fun errorL(msg: String) {
        Toasty.error(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true).show()
    }

    fun success(msg: String) {
        Toasty.success(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true).show()
    }

    fun successL(msg: String) {
        Toasty.success(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true).show()
    }

    fun info(msg: String) {
        Toasty.info(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT, true).show()
    }

    fun infoL(msg: String) {
        Toasty.info(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG, true).show()
    }

    fun normal(msg: String) {
        Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_SHORT).show()
    }

    fun normalL(msg: String) {
        Toasty.normal(BaseApplication.getInstance(), msg, Toasty.LENGTH_LONG).show()
    }

    @SuppressLint("ShowToast")
    fun shortT(msg: String) {
        toast.setText(msg)
        toast.show()
        handler.removeMessages(0)
        handler.sendEmptyMessageDelayed(0, 200)
    }
}