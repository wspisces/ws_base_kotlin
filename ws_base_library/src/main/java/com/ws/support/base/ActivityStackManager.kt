package com.ws.support.base

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * 描述信息
 *
 * @author ws
 * @date 2020/9/24 14:41
 * 修改人：ws
 */
class ActivityStackManager private constructor() {
    private var activities: Stack<Activity>? = null
    private var sCurrentActivityWeakRef: WeakReference<Activity>? = null

    private object InstanceHolder {
        val sInstance = ActivityStackManager()
    }

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef!!.get()
        }
        return currentActivity
    }

    fun setCurrentActivity(activity: Activity) {
        sCurrentActivityWeakRef = WeakReference(activity)
    }

    fun addActivity(activity: Activity) {
        if (activities == null) {
            activities = Stack()
        }
        if (activities!!.search(activity) == -1) {
            activities!!.push(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        if (activities != null && activities!!.size > 0) {
            activities!!.remove(activity)
        }
    }

    fun setTopActivity(activity: Activity) {
        if (activities != null && activities!!.size > 0) {
            if (activities!!.search(activity) == -1) {
                activities!!.push(activity)
                return
            }
            val location = activities!!.search(activity)
            if (location != 1) {
                activities!!.remove(activity)
                activities!!.push(activity)
            }
        }
    }

    fun getTopActivity(): Activity? {
        return if (activities != null && activities!!.size > 0) {
            activities!!.peek()
        } else null
    }

    fun isTopActivity(activity: Activity): Boolean {
        return activity == activities!!.peek()
    }

    fun finishTopActivity() {
        if (activities != null && activities!!.size > 0) {
            val activity = activities!!.pop()
            activity?.finish()
        }
    }

    fun finishAllActivity() {
        if (activities != null && activities!!.size > 0) {
            while (!activities!!.empty()) {
                val activity = activities!!.pop()
                activity?.finish()
            }
            activities!!.clear()
            activities = null
        }
    }

    fun getActivityStack(): Stack<Activity>? {
        return activities
    }

    companion object {
        fun getInstance(): ActivityStackManager {
            return InstanceHolder.sInstance
        }
    }
}