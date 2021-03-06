package com.ws.support.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * 获得屏幕相关的辅助类
 *
 * @author zhy
 */
@Suppress("DEPRECATION")
class ScreenUtils private constructor() {
    companion object {
        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context?): Int {
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context): Int {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 获得状态栏的高度
         *
         * @param context
         * @return
         */
        @SuppressLint("PrivateApi")
        fun getStatusHeight(context: Context): Int {
            var statusHeight = -1
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = "${clazz.getField("status_bar_height")[`object`]}".toInt()
                statusHeight = context.resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusHeight
        }

        /**
         * 获取当前屏幕截图，包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithStatusBar(activity: AppCompatActivity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            val bp: Bitmap? = Bitmap.createBitmap(bmp, 0, 0, width, height)
            view.destroyDrawingCache()
            return bp
        }

        /**
         * 获取当前屏幕截图，不包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithoutStatusBar(activity: AppCompatActivity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            val bp: Bitmap? = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                    - statusBarHeight)
            view.destroyDrawingCache()
            return bp
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}