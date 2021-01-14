//package com.ws.support.base.activity
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.ws.support.base.listener.IOnActivityRunPeriodListener
//import java.util.*
//
///**
// * BaseFragmentActivity.class
// * 基础页面
// * time:2018/4/9
// */
//internal open class BaseFragmentActivity : AppCompatActivity() {
//    private var savedInstanceState: Bundle? = null
//    private val mOnActivityRunPeriodListenerList: MutableList<IOnActivityRunPeriodListener> = ArrayList()
//
//    /** 设置页面运行周期监听  */
//    protected fun setActivityRunPeriodListenerListener(listener: IOnActivityRunPeriodListener?) {
//        if (listener != null) {
//            mOnActivityRunPeriodListenerList.add(listener)
//            listener.onCreate(savedInstanceState)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        this.savedInstanceState = savedInstanceState
//        for (listener in mOnActivityRunPeriodListenerList) {
//            listener.onCreate(savedInstanceState)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        for (listener in mOnActivityRunPeriodListenerList) {
//            listener.onStart()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        for (listener in mOnActivityRunPeriodListenerList) {
//            listener.onResume()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        for (listener in mOnActivityRunPeriodListenerList) {
//            listener.onPause()
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        for (listener in mOnActivityRunPeriodListenerList) {
//            listener.onStop()
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        if (mOnActivityRunPeriodListenerList != null) {
//            for (listener in mOnActivityRunPeriodListenerList) {
//                listener.onNewIntent(intent)
//            }
//        }
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        if (mOnActivityRunPeriodListenerList != null) {
//            for (listener in mOnActivityRunPeriodListenerList) {
//                listener.onRestart()
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        for (listener in mOnActivityRunPeriodListenerList!!) {
//            listener.onDestroy()
//        }
//        mOnActivityRunPeriodListenerList.clear()
//    }
//}