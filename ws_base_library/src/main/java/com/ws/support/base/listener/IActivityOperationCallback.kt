package com.ws.support.base.listener

import android.view.View

/**
 * 页面操作回调
 *
 * @author Johnny.xu
 * date 2017/3/15
 */
interface IActivityOperationCallback {
    /**
     * 隐藏键盘
     */
    fun hideKeyboard(view: View?)

    /**
     * 显示键盘
     */
    fun showKeyboard(view: View?)

    /**
     * 隐藏遮盖层
     * @return 当返回值为true的情况下，说明隐藏成功；
     * 否则为请求层不存在或请求层状态为隐藏状态
     */
    fun hideCoverView(): Boolean

    /**
     * 显示遮盖层
     */
    fun showCoverView()

    /**
     * 隐藏请求层
     * @return 当返回值为true的情况下，说明隐藏成功；
     * 否则为请求层不存在或请求层状态为隐藏状态
     */
    fun hideRequestView(): Boolean

    /**
     * 显示请求层 请求层文案默认为'请求中...'
     */
    fun showRequestView()

    /**
     * 显示请求层 请求层文案默认为'请求中...'
     */
    fun showRequestView(prompt: String?)

    /**
     * 隐藏网络错误层
     */
    fun hideNetWorkErrorView()

    /**
     * 显示网络错误层
     */
    fun showNetWorkErrorView(action: Runnable?)

    /**
     * 显示遮盖内容层
     */
    fun showCoverContentView()

    /**
     * 隐藏遮盖内容层
     * @return 当返回值为true的情况下，说明隐藏成功；
     * 否则为请求层不存在或请求层状态为隐藏状态
     */
    fun hideCoverContentView(): Boolean

    /**
     * 初始化覆盖页面
     */
    fun initCoverContentView(view: View?)

    /**
     * 返回
     */
    fun goBack()

    /**
     * 退出应用
     */
    fun exitApp()
}