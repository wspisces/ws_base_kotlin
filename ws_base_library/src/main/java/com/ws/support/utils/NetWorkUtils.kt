@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Proxy
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.ws.support.base.BaseApplication
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * NetWork Utils
 *
 * **Attentions**
 *  * You should add **android.permission.ACCESS_NETWORK_STATE**
 * in manifest, to get network status.
 *
 *
 * @author [Trinea](http://www.trinea.cn) 2014-11-03
 */
object NetWorkUtils {
    const val NETWORK_TYPE_WIFI = "wifi"
    const val NETWORK_TYPE_3G = "eg"
    const val NETWORK_TYPE_2G = "2g"
    const val NETWORK_TYPE_WAP = "wap"
    const val NETWORK_TYPE_UNKNOWN = "unknown"
    const val NETWORK_TYPE_DISCONNECT = "disconnect"

    /**
     * Get network type
     *
     * @param context
     * @return
     */
    fun getNetworkType(context: Context): Int {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo?.type ?: -1
    }

    /**
     * Get network type name
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getNetworkTypeName(context: Context): String {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        var type = NETWORK_TYPE_DISCONNECT
        if (manager.activeNetworkInfo?.also { networkInfo = it } == null) {
            return type
        }
        if (networkInfo?.isConnected == true) {
            val typeName = networkInfo?.typeName
            type = if ("WIFI".equals(typeName, ignoreCase = true)) {
                NETWORK_TYPE_WIFI
            } else if ("MOBILE".equals(typeName, ignoreCase = true)) {
                val proxyHost = Proxy.getDefaultHost()
                if (TextUtils.isEmpty(proxyHost)) if (isFastMobileNetwork(context)) NETWORK_TYPE_3G else NETWORK_TYPE_2G else NETWORK_TYPE_WAP
            } else {
                NETWORK_TYPE_UNKNOWN
            }
        }
        return type
    }

    /**
     * Whether is fast mobile network
     *
     * @param context
     * @return
     */
    private fun isFastMobileNetwork(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                ?: return false
        return when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_1xRTT -> false
            TelephonyManager.NETWORK_TYPE_CDMA -> false
            TelephonyManager.NETWORK_TYPE_EDGE -> false
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> true
            TelephonyManager.NETWORK_TYPE_EVDO_A -> true
            TelephonyManager.NETWORK_TYPE_GPRS -> false
            TelephonyManager.NETWORK_TYPE_HSDPA -> true
            TelephonyManager.NETWORK_TYPE_HSPA -> true
            TelephonyManager.NETWORK_TYPE_HSUPA -> true
            TelephonyManager.NETWORK_TYPE_UMTS -> true
            TelephonyManager.NETWORK_TYPE_EHRPD -> true
            TelephonyManager.NETWORK_TYPE_EVDO_B -> true
            TelephonyManager.NETWORK_TYPE_HSPAP -> true
            TelephonyManager.NETWORK_TYPE_IDEN -> false
            TelephonyManager.NETWORK_TYPE_LTE -> true
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
            else -> false
        }
    }

    // 检测网络
    fun checkNetworkAvailable(): Boolean {
        val connectivity = BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            @SuppressLint("MissingPermission")
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        val netWorkInfo = info[i]
                        if (netWorkInfo.type == ConnectivityManager.TYPE_WIFI) {
                            return true
                        } else if (netWorkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                            return true
                        } else if (netWorkInfo.type == ConnectivityManager.TYPE_ETHERNET) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun getIP(context: Context): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }
}