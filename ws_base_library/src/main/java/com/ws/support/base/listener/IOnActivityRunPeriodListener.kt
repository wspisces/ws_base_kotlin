package com.ws.support.base.listener

import android.content.Intent
import android.os.Bundle

/**
 * IOnActivityRunPeriodListener.class
 * 页面运行周期监听器
 * time:2018/4/9
 */
interface IOnActivityRunPeriodListener {
    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onPause()
    fun onResume()
    fun onStop()
    fun onDestroy()
    fun onRestart()
    fun onNewIntent(intent: Intent?)
}