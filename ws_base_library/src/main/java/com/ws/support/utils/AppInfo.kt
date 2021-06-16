package com.ws.support.utils

import android.graphics.drawable.Drawable





/**
 * 描述信息
 * @author ws
 * @date 5/10/21 5:33 PM
 * 修改人：ws
 */
/**
 * App信息类
 */
class AppInfo {
    // 包名
    var packageName: String? = null

    // APP名
    var appName: String? = null

    // 图标
    var icon: Drawable? = null

    // 版本号
    var versionName: String? = null

    // 权限
    var permissions: Array<String> = arrayOf()

    // 主Activity的类名
    var launchActivityName: String? = null
}

