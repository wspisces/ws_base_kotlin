package com.ws.support.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
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
abstract class BaseViewBindActivity: AppCompatActivity() {
    protected lateinit var baseBinding: ActivityBaseBinding
    protected lateinit var mContext: Context
    protected lateinit var mActivity: AppCompatActivity
    private var progressDialog: MyProgressDialogFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen()) {
            //设置无标题
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //设置全屏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        requestedOrientation = if (isPortrait()) {
            // 强制切换为竖屏
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // 强制切换为横屏
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)
        initData_()
        initToolbar()
        initView(baseBinding)
    }

    private fun initData_() {
        mContext = this
        mActivity = this
    }



    /**
     * 隐藏Toolbar
     */
    protected open fun hideToolbar(): Boolean {
        return false
    }

    /**
     * 是否显示返回按钮
     */
    protected open fun enableNavigation(): Boolean {
        return true
    }

    /**
     * 是否强制竖屏
     */
    protected open fun isPortrait(): Boolean {
        return true
    }
    /**
     * 是否全屏
     */
    protected open fun isFullScreen(): Boolean {
        return false
    }
    /**
     * 设置Toolbar标题
     */
    protected abstract val toolbarTite: String?

    /**
     * 初始化Toolbar
     */
    private fun initToolbar() {
        if (isFullScreen() || hideToolbar()) {
            baseBinding.toolbar.visibility = View.GONE
            return
        }
        baseBinding.toolbar.title = ""
        if (enableNavigation()) {
            baseBinding.toolbar.setNavigationIcon(R.drawable.ic_back_white)
        }
        setSupportActionBar(baseBinding.toolbar)
        setTitle(toolbarTite)
    }

    /**
     * 设置标题
     */
    protected fun setTitle(title: String?) {
        if (title != null) {
            baseBinding.title.text = title
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
    protected abstract fun initView(bindView: ActivityBaseBinding)

    @SuppressLint("CheckResult")
    @JvmOverloads
    protected fun deleyActionOnMain(time: Long = DELAY_TIME, back: Consumer<Long?>?) {
        deleyAction(time, AndroidSchedulers.mainThread(), back)
    }

    @SuppressLint("CheckResult")
    @JvmOverloads
    protected fun deleyAction(time: Long = DELAY_TIME, scheduler: Scheduler?, back: Consumer<Long?>?) {
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            //hintKbTwo();
            onBackPressed()
        }
        return true /* super.onOptionsItemSelected(item) */
    }

    /**
     * 公用组件：进度条
     */
    protected fun initProgress(msg: String) {
        try {
            if (null == progressDialog) {
                progressDialog = newInstance(msg)
            }
            progressDialog!!.setMessage(msg)
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
    //跳转
    @JvmOverloads
    protected fun gotoActivity(cls: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    @JvmOverloads
    protected fun jumptoActivity(cls: Class<*>?, bundle: Bundle? = null) {
        gotoActivity(cls, bundle)
        finish()
    }

    companion object {
        private const val DELAY_TIME: Long = 800
    }

    /*  private static final int             TWO_SECOND   = 2 * 1000;
//双击后退
private long preTime;
@Override
public void onBackPressed()
{
long currentTime = new Date().getTime();
if (currentTime - preTime > TWO_SECOND || preTime == 0)
{
    ToastUtils.info("再点击一次退出");
    preTime = currentTime;
} else
{
    System.exit(0);
}
}*/
}