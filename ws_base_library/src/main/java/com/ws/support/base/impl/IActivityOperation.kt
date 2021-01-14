package com.ws.support.base.impl

/**
 * IActivityOperation.class
 * 页面基础操作
 * @author Johnny.xu
 * time:2019/3/29
 */
interface IActivityOperation {
    /**
     * 隐藏请求层
     * @return 当返回值为true的情况下，说明隐藏成功；
     * 否则为请求层不存在或请求层状态为隐藏状态
     */
    fun hideRequestView(): Boolean

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
    fun showNetWorkErrorView(prompt: String?, action: Runnable?)

    /**
     * 清除错误页面回调功能
     */
    fun clearAllErrorRunnable()

    /**
     * 是否初始化EventBus
     */
    val isInitEventBus: Boolean

    /**
     * 是否显示标题栏
     */
    val isShowTitleBar: Boolean

    /**
     * 是否过滤设置背景
     */
    val isPassSettingWindowBackground: Boolean
    val contentViewById: Int
}