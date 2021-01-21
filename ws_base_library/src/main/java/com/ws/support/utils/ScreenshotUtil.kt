@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Author : Ziwen Lan
 * Date : 2019/10/23
 * Time : 15:11
 * Introduction : 截屏工具类
 */
object ScreenshotUtil {
    /**
     * 截取指定activity显示内容
     * 需要读写权限
     */
    fun saveScreenshotFromActivity(activity: Activity) {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        val bitmap = view.drawingCache
        saveImageToGallery(bitmap, activity)
        //回收资源
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
    }

    /**
     * 截取指定View显示内容
     * 需要读写权限
     */
    fun saveScreenshotFromView(view: View, context: Activity): String {
        view.isDrawingCacheEnabled = true
        val bitmap = view.drawingCache
        val path = saveImageToGallery(bitmap, context)
        //回收资源
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        return path
    }

    /**
     * 保存图片至相册
     * 需要读写权限
     */
    fun saveImageToGallery(bmp: Bitmap, context: Activity): String {
        val appDir = File(getDCIM())
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
        // 通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getDCIM())))
        return file.absolutePath
    }

    /**
     * 获取相册路径
     */
    private fun getDCIM(): String {
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            return ""
        }
        var path = Environment.getExternalStorageDirectory().path + "/dcim/"
        if (File(path).exists()) {
            return path
        }
        path = Environment.getExternalStorageDirectory().path + "/DCIM/"
        val file = File(path)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return ""
            }
        }
        return path
    }
}