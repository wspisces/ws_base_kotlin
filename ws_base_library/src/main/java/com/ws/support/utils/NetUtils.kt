package com.ws.support.utils

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity

/**
 * 跟网络相关的工具类
 *
 * @author zhy
 */
class NetUtils private constructor() {
    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    fun hasSimCard(context: Context): Boolean {
        val simState = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simState
        return hasSimCard(simState)
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    fun hasSimCard(telephonyManager: TelephonyManager): Boolean {
        val simState = telephonyManager.simState
        return hasSimCard(simState)
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    fun hasSimCard(simState: Int): Boolean {
        var result = true
        when (simState) {
            TelephonyManager.SIM_STATE_ABSENT -> result = false // 没有SIM卡
            TelephonyManager.SIM_STATE_UNKNOWN -> result = false
        }
        return result
    }

    companion object {
        /**
         * 判断网络是否连接
         *
         * @param context
         * @return
         */
        fun isConnected(context: Context): Boolean {
            val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (null != connectivity) {
                val info = connectivity.activeNetworkInfo
                if (null != info && info.isConnected) {
                    if (info.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
            return false
        }

        fun getNetLevel(context: Context): Int {
            val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//		if (null != connectivity)
//		{
//
//			NetworkInfo info = connectivity.getActiveNetworkInfo();
//			if (null != info && info.isConnected())
//			{
//				if (info.getState() == NetworkInfo.State.CONNECTED)
//				{
//					return info.get;
//				}
//			}
//		}
            return 0
        }

        /**
         * 判断是否是wifi连接
         */
        @SuppressLint("MissingPermission")
        fun isWifi(context: Context): Boolean {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    ?: return false
            return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * 打开网络设置界面
         */
        fun openSetting(activity: AppCompatActivity) {
            val intent = Intent("/")
            val cm = ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings")
            intent.component = cm
            intent.action = "android.intent.action.VIEW"
            activity.startActivityForResult(intent, 0)
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}