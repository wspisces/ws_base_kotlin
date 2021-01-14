package com.ws.support.base.listener

import android.util.AttributeSet

/**
 * 自定义View操作接口
 */
interface ViewOperationListener {
    fun initPaint()
    fun initParams(attrs: AttributeSet?)
}