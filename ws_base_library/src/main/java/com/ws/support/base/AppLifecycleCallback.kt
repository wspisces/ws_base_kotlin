package com.ws.support.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

/**
 * Created by sunpeng on 17/6/2.
 */
class AppLifecycleCallback : ActivityLifecycleCallbacks {
    private var appStatus = APP_STATUS_UNKNOWN
    private var isForground = true
    private var appCount = 0
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        ActivityStackManager.Companion.getInstance().addActivity(activity)
        if (appStatus == APP_STATUS_UNKNOWN) {
            appStatus = APP_STATUS_LIVE
            startLauncherActivity(activity)
        }
        if (savedInstanceState != null && savedInstanceState.getBoolean("saveStateKey", false)) {
            Log.e(TAG, "localTime --> " + savedInstanceState.getLong("localTime"))
        }
    }

    override fun onActivityStarted(activity: Activity) {
        appCount++
        if (!isForground) {
            isForground = true
            Log.e(TAG, "app into forground")
        }
    }

    override fun onActivityResumed(activity: Activity) {
        // 弱引用持有当前 Activity 实例
        ActivityStackManager.Companion.getInstance().setCurrentActivity(activity)
        // Activity 页面栈方式
        ActivityStackManager.Companion.getInstance().setTopActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        appCount--
        if (!isForgroundAppValue()) {
            isForground = false
            Log.d(TAG, "app into background ")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        outState.putBoolean("saveStateKey", true)
        outState.putLong("localTime", System.currentTimeMillis())
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStackManager.Companion.getInstance().removeActivity(activity)
    }

    private fun isForgroundAppValue(): Boolean {
        return appCount > 0
    }

    companion object {
        private const val TAG = "AppLifecycleCallback"
        private const val APP_STATUS_UNKNOWN = -1
        private const val APP_STATUS_LIVE = 0
        private fun startLauncherActivity(activity: Activity) {
            try {
                val launchIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
                val launcherClassName = launchIntent!!.component!!.className
                val className = activity.componentName.className
                if (TextUtils.isEmpty(launcherClassName) || launcherClassName == className) {
                    return
                }
                Log.e(TAG, "launcher ClassName --> $launcherClassName")
                Log.e(TAG, "current ClassName --> $className")
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activity.startActivity(launchIntent)
                activity.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}