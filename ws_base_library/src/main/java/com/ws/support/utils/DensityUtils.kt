package com.ws.support.utils

import android.content.Context
import android.util.TypedValue

/**
 * 常用单位转换的辅助类
 *
 * @author zhy
 */
class DensityUtils private constructor() {
    companion object {
        /**
         * dp转px
         *
         * @param context
         * @param val
         * @return
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.resources.displayMetrics).toInt()
        }

        /**
         * sp转px
         *
         * @param context
         * @param val
         * @return
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    spVal, context.resources.displayMetrics).toInt()
        }

        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         *
         * @param fontScale
         * @param pxVal
         * @return
         */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}