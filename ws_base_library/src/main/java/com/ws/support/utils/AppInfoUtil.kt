package com.ws.support.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.util.Log
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList


/**
 * 描述信息
 * @author ws
 * @date 5/10/21 5:31 PM
 * 修改人：ws
 */
/**
 * 获取手机上安装的所有APP的信息 配合AppInfo类使用
 */
@Suppress("DEPRECATION")
class AppInfoUtil private constructor(context: Context) {
    private val pManager: PackageManager

    // 所有应用
    private var allPackageList: MutableList<PackageInfo>? = null

    // 筛选结果
    private var result: MutableList<PackageInfo>

    /** 获取已安装的APP  */
    fun getInstalledApps(type: Int): List<AppInfo>? {
        // 0 表示不接受任何参数。其他参数都带有限制
        // 版本号、APP权限只能通过PackageInfo获取，故这里不使用getInstalledApplications()方法
        allPackageList = pManager.getInstalledPackages(0)
        if (allPackageList == null) {
            Log.e("AppInfoUtil类", "getInstalledApps()方法中的allPackageList为空")
            return null
        }
        // 根据APP名排序
        Collections.sort(allPackageList, PackageInfoComparator(pManager))
        // 筛选
        result.clear()
        when (type) {
            GET_ALL_APP -> result = allPackageList as MutableList<PackageInfo>
            GET_SYSTEM_APP -> for (info in allPackageList!!) {
                // FLAG_SYSTEM = 1<<0，if set, this application is installed in
                // the device's system image.
                // 下面&运算有两种结果：
                // 1，则flags的末位为1，即系统APP
                // 0，则flags的末位为0，即非系统APP
                if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                    result.add(info)
                }
            }
            GET_THIRD_APP -> for (info in allPackageList!!) {
                // FLAG_SYSTEM = 1<<0，同上
                if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    result.add(info)
                } else if (info.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1) {
                    result.add(info)
                }
            }
            GET_SDCARD_APP -> for (info in allPackageList!!) {
                // FLAG_EXTERNAL_STORAGE = 1<<18，Set to true if the application
                // is
                // currently installed on external/removable/unprotected storage
                if (info.applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE == 1) {
                    result.add(info)
                }
            }
        }
        return getAppInfoByPackageInfo(result)
    }

    @SuppressLint("WrongConstant")
    fun getAppInfoByIntent(intent: Intent?): List<AppInfo> {
        val resolveInfos = pManager.queryIntentActivities(intent!!,
                PackageManager.GET_INTENT_FILTERS)
        // 调用系统排序 ， 根据name排序
        // 此排序会将系统自带App与用户安装的APP分开排序
        Collections.sort(resolveInfos, ResolveInfo.DisplayNameComparator(
                pManager))
        // // 此排序会将系统自带App与用户安装的APP混合排序
        // Collections.sort(resolveInfos, new DisplayNameComparator(pManager));
        return getAppInfobyResolveInfo(resolveInfos)
    }

    /** 获取单个App图标  */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppIcon(packageName: String?): Drawable {
        return pManager.getApplicationIcon(packageName!!)
    }

    /** 获取单个App名称  */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppName(packageName: String?): String {
        val appInfo = pManager.getApplicationInfo(packageName!!, 0)
        return pManager.getApplicationLabel(appInfo).toString()
    }

    /** 获取单个App版本号  */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppVersion(packageName: String?): String {
        val packageInfo = pManager.getPackageInfo(packageName!!, 0)
        return packageInfo.versionName
    }

    /** 获取单个App的所有权限  */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppPermission(packageName: String?): Array<String> {
        val packageInfo = pManager.getPackageInfo(packageName!!,
                PackageManager.GET_PERMISSIONS)
        return packageInfo.requestedPermissions
    }

    /** 获取单个App的签名  */
    @SuppressLint("PackageManagerGetSignatures")
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppSignature(packageName: String?): String {
        val packageInfo = pManager.getPackageInfo(packageName!!,
                PackageManager.GET_SIGNATURES)
        return packageInfo.signatures[0].toCharsString()
    }
    // /** 使用示例 **/
    // public static void main(String[] args) {
    // AppInfoUtil appInfoUtil = AppInfo.getInstance(context);
    //
    // // 获取所有APP
    // List<AppInfo> allAppInfo = appInfoUtil.getInstalledApps(AppInfoUtil.GET_ALL_APP);
    // for (AppInfo app : allAppInfo) {
    // String packageName = app.getPackageName();
    // String appName = app.getAppName();
    // Drawable icon = app.getIcon();
    // String versionName = app.getVersionName();
    // String[] permissions = app.getPermissions();
    // // 自由发挥...
    // }
    //
    // // 获取单个APP的信息
    // String appName = appInfoUtil.getAppName(packageName);
    // ...
    // }
    /** 从PackageInfo的List中提取App信息  */
    private fun getAppInfoByPackageInfo(list: List<PackageInfo>): List<AppInfo> {
        val appList: MutableList<AppInfo> = ArrayList<AppInfo>()
        for (info in list) {
            // 获取信息
            val packageName = info.applicationInfo.packageName
            val appName = pManager.getApplicationLabel(info.applicationInfo)
                    .toString()
            val icon = pManager.getApplicationIcon(info.applicationInfo)
            // // 也可以用如下方法获取APP图标，显然更烦琐
            // ApplicationInfo applicationInfo =
            // pManager.getApplicationInfo(packageName, 0);
            // Drawable icon = applicationInfo.loadIcon(pManager);
            val versionName = info.versionName
            val permissions = info.requestedPermissions
            val launchActivityName = getLaunchActivityName(packageName)
            // 储存信息
            val appInfo = AppInfo()
            appInfo.packageName = packageName
            appInfo.appName = appName
            appInfo.icon = icon
            appInfo.versionName = versionName
            appInfo.permissions = permissions
            appInfo.launchActivityName = launchActivityName
            appList.add(appInfo)
        }
        return appList
    }

    /** 从ResolveInfo的List中提取App信息  */
    private fun getAppInfobyResolveInfo(list: List<ResolveInfo>): List<AppInfo> {
        val appList: MutableList<AppInfo> = ArrayList<AppInfo>()
        for (info in list) {
            val packageName = info.activityInfo.packageName
            val appName = info.loadLabel(pManager).toString()
            val icon = info.loadIcon(pManager)
            val launchActivityName = getLaunchActivityName(packageName)
            val appInfo = AppInfo()
            appInfo.packageName = packageName
            appInfo.appName = appName
            appInfo.icon = icon
            appInfo.launchActivityName = launchActivityName

            appList.add(appInfo)
        }
        return appList
    }

    /** 获取指定包中主Activity的类名，并不是所有包都有主Activity  */
    @SuppressLint("QueryPermissionsNeeded")
    private fun getLaunchActivityName(packageName: String): String {
        // 根据PackageInfo对象取不出其中的主Activity，须用Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setPackage(packageName)
        val resolveInfos = pManager.queryIntentActivities(intent,
                0)
        var mainActivityName = ""
        if (resolveInfos.size >= 1) {
            mainActivityName = resolveInfos[0].activityInfo.name
        }
        return mainActivityName
    }

    /** 此比较器直接复制Android源码，但是却可以把系统APP与用户APP混合排列，何解？  */
    private class DisplayNameComparator(private val mPM: PackageManager) : Comparator<ResolveInfo?> {
        override fun compare(a: ResolveInfo?, b: ResolveInfo?): Int {
            var sa = a?.loadLabel(mPM)
            if (sa == null) sa = a?.activityInfo?.name
            var sb = b?.loadLabel(mPM)
            if (sb == null) sb = b?.activityInfo?.name
            return sCollator.compare(sa.toString(), sb.toString())
        }

        private val sCollator: Collator = Collator.getInstance()
    }

    /** 自定义的PackageInfo排序器  */
    private class PackageInfoComparator(private val mPM: PackageManager) : Comparator<PackageInfo?> {
        override fun compare(a: PackageInfo?, b: PackageInfo?): Int {
            val sa = mPM.getApplicationLabel(a!!.applicationInfo)
            val sb = mPM.getApplicationLabel(b!!.applicationInfo)
            return sCollator.compare(sa.toString(), sb.toString())
        }

        private val sCollator: Collator = Collator.getInstance()
    }

    companion object {
        const val GET_ALL_APP = 0 // 所有APP
        const val GET_SYSTEM_APP = 1 // 系统预装APP
        const val GET_THIRD_APP = 2 // 第三方APP
        const val GET_SDCARD_APP = 3 // SDCard的APP
        private var infoUtil: AppInfoUtil? = null

        /** 单例  */
        fun getInstance(context: Context): AppInfoUtil? {
            if (infoUtil == null) {
                infoUtil = AppInfoUtil(context)
            }
            return infoUtil
        }
    }

    /** 私有构造器  */
    init {
        pManager = context.getPackageManager()
        result = ArrayList()
    }
}

