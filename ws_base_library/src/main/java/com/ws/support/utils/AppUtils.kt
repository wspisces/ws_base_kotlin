package com.ws.support.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import android.telephony.TelephonyManager
import com.orhanobut.logger.Logger
import java.io.File
import java.text.DecimalFormat

//import com.tbruyelle.rxpermissions2.RxPermissions;
/**
 * 跟App相关的辅助类
 *
 * @author zhy
 */
class AppUtils private constructor() {
    companion object {
        /**
         * 获取应用程序名称
         */
        fun getAppName(context: Context): String? {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(
                        context.packageName, 0)
                val labelRes = packageInfo.applicationInfo.labelRes
                return context.resources.getString(labelRes)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * [获取应用程序版本名称信息]
         * @param context
         * @return 当前应用的版本号
         */
        fun getVersionCode(context: Context): Int {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                return packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return 1
        }

        /**
         * [获取应用程序版本名称信息]
         * @param context
         * @return 当前应用的版本名称
         */
        fun getVersionName(context: Context): String? {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(
                        context.packageName, 0)
                return packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        @SuppressLint("MissingPermission")
        fun getImei(context: Context): String {
            val TelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return TelephonyMgr.deviceId
        }

        @SuppressLint("HardwareIds", "MissingPermission")
        fun getWlanId(context: Context): String {
            val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wm.connectionInfo.macAddress
        }

        /**
         * 判断系统是否在后台运行
         * @param context 上下文
         * @param appPackageName 包名
         */
        fun isAppRunning(context: Context, appPackageName: String): Boolean {
            //判断应用是否在运行
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val list = am.getRunningTasks(100)
            var isAppRunning = false
            for (info in list) {
                if (info.topActivity!!.packageName == appPackageName || info.baseActivity!!.packageName == appPackageName) {
                    isAppRunning = true
                    break
                }
            }
            return isAppRunning
        }

        /**
         * 拨打电话
         * @param context 上下文-最好是Activity
         * @param phoneNum 手机号码
         */
        /*	@SuppressLint("CheckResult")
	public static void callPhone(final Context context, final String phoneNum) {

		if (context instanceof AppCompatActivity) {

			RxPermissions rxPermissions = new RxPermissions((AppCompatActivity) context);
			rxPermissions.request(Manifest.permission.CALL_PHONE)
					.subscribe(new Consumer<Boolean>() {
						@Override
						public void accept(Boolean isOpen) throws Exception {
							if (isOpen) {
								callPhoneAction(phoneNum, context);
							} else {
								Toast.makeText(context, "请打开拨打电话权限", Toast.LENGTH_SHORT).show();
							}
						}
					});
		} else {

			callPhoneAction(phoneNum, context);
		}
	}*/
        @SuppressLint("MissingPermission")
        private fun callPhoneAction(phoneNum: String, context: Context) {
            val intent = Intent(Intent.ACTION_CALL)
            val data = Uri.parse("tel:$phoneNum")
            intent.data = data
            context.startActivity(intent)
        }

        /**
         * 判断服务是否在后台运行
         * @param context 上下文
         * @param serviceName 服务名称
         * @return true 正在运行
         */
        fun isServiceRunning(context: Context, serviceName: String): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceName == service.service.className) {
                    Logger.i("检测服务:$serviceName")
                    return true
                }
            }
            return false
        }

        /**
         * 判断是否安装目标应用
         * @param packageName 目标应用安装后的包名
         * @return 是否已安装目标应用
         */
        fun isInstallByread(packageName: String): Boolean {
            return File("/data/data/$packageName").exists()
        }

        /**
         * 获取Manifest Meta Data
         * @param context
         * @param metaKey
         * @return
         */
        fun getMetaData(context: Context, metaKey: String?): String? {
            val name = context.packageName
            val appInfo: ApplicationInfo
            var msg: String? = ""
            try {
                appInfo = context.packageManager.getApplicationInfo(name,
                        PackageManager.GET_META_DATA)
                msg = appInfo.metaData.getString(metaKey)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

//		if (cn.finalteam.toolsfinal.StringUtils.isEmpty(msg)) {
//			return "";
//		}
            return msg
        }

        /**
         * 获得渠道号
         * @param context
         * @param channelKey
         * @return
         */
        fun getChannelNo(context: Context, channelKey: String?): String? {
            return getMetaData(context, channelKey)
        }

        fun getPackageUid(context: Context, packageName: String?): Int {
            val packageManager = context.packageManager
            var uid = -1
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                uid = packageInfo.applicationInfo.uid
            } catch (e: PackageManager.NameNotFoundException) {
            }
            return uid
        }

        fun isPackage(context: Context, s: CharSequence): Boolean {
            val packageManager = context.packageManager
            try {
                packageManager.getPackageInfo(s.toString(), PackageManager.GET_META_DATA)
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }
            return true
        }

        fun jumpToDeviceWifiPage(context: Context) {
            context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }

        fun getDataSize(size: Long): String {
            val formater = DecimalFormat("####.00")
            val b = 1024L
            val kb = 1024 * 1024L
            val mb = 1024 * 1024 * 1024L
            val gb = 1024 * 1024 * 1024 * 1024L
            return if (size < b) {
                size.toString() + "B"
            } else if (size < kb) {
                val kbsize = size / 1024f
                formater.format(kbsize.toDouble()) + "KB"
            } else if (size < mb) {
                val mbsize = size / 1024f / 1024f
                formater.format(mbsize.toDouble()) + "MB"
            } else if (size < gb) {
                val gbsize = size / 1024f / 1024f / 1024f
                formater.format(gbsize.toDouble()) + "GB"
            } else {
                ""
            }
        }

        fun getKb(size: Long): String {
            val formater = DecimalFormat("####.00")
            val b = 1024L
            if (size < b) {
                return size.toString() + "B"
            }
            val kbsize = size / 1024f
            return formater.format(kbsize.toDouble()) + "KB"
        }
    }

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }
}