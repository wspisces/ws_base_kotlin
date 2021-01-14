package com.ws.support.utils

import android.text.TextUtils

/**
 * Created by wg on 2018/2/6.
 */
object JsonIsNullUtils {
    fun isNotEmpty(mObject: Any?): Boolean {
        return if (mObject == null || TextUtils.equals(mObject.toString(), "null")
                || TextUtils.equals(mObject.toString(), "[]")
                || TextUtils.equals(mObject.toString(), "{}")
                || TextUtils.equals(mObject.toString(), "")) {
            false
        } else {
            true
        }
    }
}