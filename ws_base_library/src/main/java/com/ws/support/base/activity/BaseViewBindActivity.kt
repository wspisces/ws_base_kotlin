package com.ws.support.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ws.base.R
import com.ws.base.databinding.ActivityBaseBinding
import com.ws.support.widget.MyProgressDialogFragment
import com.ws.support.widget.MyProgressDialogFragment.Companion.newInstance
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * 描述信息
 *
 * @author ws
 * 2020/11/13 11:04
 * 修改人：ws
 */
@Suppress("DEPRECATION")
abstract class BaseViewBindActivity : AppCompatActivity() {
    protected var baseBinding: ActivityBaseBinding? = null
    protected var mContext: Context? = null
    protected var mActivity: AppCompatActivity? = null
    private var progressDialog: MyProgressDialogFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen) {
            //设置无标题
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //设置全屏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        requestedOrientation = if (isPortrait) {
            // 强制切换为竖屏
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // 强制切换为横屏
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding!!.root)
        initData_()
        initToolbar()
        initView(baseBinding)
    }

    private fun initData_() {
        mContext = this
        mActivity = this
    }

    protected val isPortrait: Boolean
        get() = true
    protected val isFullScreen: Boolean
        get() = false

    /**
     * 隐藏Toolbar
     */
    protected fun hideToolbar(): Boolean {
        return false
    }

    /**
     * 是否显示返回按钮
     */
    protected fun enableNavigation(): Boolean {
        return true
    }

    /**
     * 设置Toolbar标题
     */
    protected abstract val toolbarTite: String?

    /**
     * 初始化Toolbar
     */
    private fun initToolbar() {
        if (isFullScreen || hideToolbar()) {
            baseBinding!!.toolbar.visibility = View.GONE
            return
        }
        baseBinding!!.toolbar.title = ""
        if (enableNavigation()) {
            baseBinding!!.toolbar.setNavigationIcon(R.drawable.ic_back_black)
        }
        setSupportActionBar(baseBinding!!.toolbar)
        setTitle(toolbarTite)
    }

    /**
     * 设置标题
     */
    protected fun setTitle(title: String?) {
        if (title != null) {
            baseBinding!!.title.text = title
        }
    }

    /**
     * 获取资源ID
     *
     * @return 布局资源ID
     */
    protected abstract val layoutId: Int
    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

    /**
     * 初始化界面
     *
     * @param bindView 界面绑定对象
     */
    protected abstract fun initView(bindView: ActivityBaseBinding?)
    @SuppressLint("CheckResult")
    protected fun deleyActionOnMain(back: Consumer<Long?>?) {
        deleyAction(DELAY_TIME, AndroidSchedulers.mainThread(), back)
    }

    @SuppressLint("CheckResult")
    protected fun deleyActionOnMain(time: Long, back: Consumer<Long?>?) {
        deleyAction(time, AndroidSchedulers.mainThread(), back)
    }

    @SuppressLint("CheckResult")
    protected fun deleyAction(scheduler: Scheduler?, back: Consumer<Long?>?) {
        Observable.timer(DELAY_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(back)
    }

    @SuppressLint("CheckResult")
    protected fun deleyAction(time: Long, scheduler: Scheduler?, back: Consumer<Long?>?) {
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(back)
    }

    /**
     * 公用组件：进度条
     */
    protected fun initProgress(msg: String?) {
        try {
            if (null == progressDialog) {
                progressDialog = newInstance(msg)
            }
            progressDialog!!.setMessage(msg!!)
            progressDialog!!.show(supportFragmentManager, MyProgressDialogFragment::class.java.simpleName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 公用组件：关闭进度条
     */
    protected fun hideProgress() {
        if (!isFinishing && null != progressDialog && progressDialog!!.dialog != null && progressDialog!!.dialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    companion object {
        private const val DELAY_TIME: Long = 800
    }
}