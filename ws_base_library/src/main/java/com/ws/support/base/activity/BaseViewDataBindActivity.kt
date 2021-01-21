package com.ws.support.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ws.base.R
import com.ws.base.databinding.ActivityDataBaseBinding
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
abstract class BaseViewDataBindActivity<T : ViewDataBinding?> : AppCompatActivity() {
    protected var baseBinding: ActivityDataBaseBinding? = null
    protected var mContext: Context? = null
    protected var progressDialog: MyProgressDialogFragment? = null
    protected var mActivity: AppCompatActivity? = null
    var toast: Toast? = null
    var toatTv: TextView? = null
    protected fun initContent(): T? {
        val layoutId = layoutId
        return if (layoutId != 0) {
            DataBindingUtil.inflate(layoutInflater, layoutId, baseBinding!!.fl, true)
        } else null
    }

    private fun initData_() {
        mActivity = this
        mContext = this
    }

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
        hideKeyBoard()
    }

    /**
     * 初始化界面
     *
     * @param bindView 界面绑定对象
     */
    protected abstract fun initView(bindView: ActivityDataBaseBinding?, binding: T?)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuLayout = menuLayout
        return if (menuLayout != 0) {
            menuInflater.inflate(menuLayout, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    protected val menuLayout: Int
        get() = 0

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            //hintKbTwo();
            onBackPressed()
        }
        return true /* super.onOptionsItemSelected(item) */
    }

    //跳转
    protected fun gotoActivity(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun gotoActivity(cls: Class<*>?) {
        startActivity(Intent(this, cls))
    }

    protected fun jumptoActivity(cls: Class<*>?, bundle: Bundle?) {
        gotoActivity(cls, bundle)
        finish()
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
    protected fun jumptoActivity(cls: Class<*>?) {
        gotoActivity(cls)
        finish()
    }

    protected fun showKeyBoard(et: EditText) {
        et.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    protected fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    protected val isPortrait: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen) {
            //设置无标题
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //设置全屏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        if (isPortrait) {
            // 强制切换为竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else {
            // 强制切换为横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        }
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_base)
        initData_()
        initToolbar()
        initToast()
        initView(baseBinding, initContent())
    }

    private fun initToast() {
        //获取样式布局
        val toastRoot = LayoutInflater.from(this).inflate(R.layout.layout_toast, null)
        //声明Toast
        toast = Toast(this)
        //给Toast设置布局
        toastRoot.also { toast!!.view = it }
        //设置布局文件里的控件属性
        toatTv = toastRoot.findViewById(R.id.tv_toast)
        toast!!.duration = Toast.LENGTH_SHORT
    }

    /**
     * 公用组件： Toast
     */
    @SuppressLint("CheckResult")
    fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        toatTv!!.text = message
        toast!!.show()
    }

    /**
     * 公用组件： Toast
     */
    @SuppressLint("CheckResult")
    fun showToast(message: Int) {
        if (message == 0) {
            return
        }
        toatTv!!.setText(message)
        toast!!.show()
    }

    companion object {
        private const val DELAY_TIME: Long = 800
    }
}