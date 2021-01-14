package com.ws.support.utils

import java.util.*

/**
 * @author Eli
 * @cratetime 2018/12/10 下午2:58
 * @desc 权限util
 */
object PermissionUtils {
    private val permissionList: MutableList<String> = ArrayList()
    fun addPermission(permission: String) {
        if (!permissionList.contains(permission)) {
            permissionList.add(permission)
        }
    }

    fun getPermission(): Array<String?> {
        val mPermissionArray = arrayOfNulls<String>(permissionList.size)
        for (i in permissionList.indices) {
            mPermissionArray[i] = permissionList[i]
        }
        return mPermissionArray
    }
}