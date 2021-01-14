package com.ws.support.utils

import android.view.View

/**
 *
 * Title:MeasureViewCallback.java
 *
 * Description:测量回调
 * @author Johnny.xu
 * @date 2016年12月13日
 */
interface MeasureViewCallback {
    fun callback(view: View?, width: Int, height: Int)
}