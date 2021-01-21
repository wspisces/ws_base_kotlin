package com.ws.support.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ws.base.R
import com.ws.support.widget.MyProgressDialogFragment
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {
    lateinit var mToolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var mContentLayout: FrameLayout
    lateinit var mContext: Context
    protected var imm: InputMethodManager? = null
    private var mThis: BaseActivity? = null
    private var progressDialog: MyProgressDialogFragment? = null

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
     * 是否启用网络监听
     */
    protected fun enableNetWork(): Boolean {
        return false
    }

    /**
     * 网络重新连接调用
     */
    protected fun WhenNetConnected() {}

    /**
     * 注册网络变化广播
     */
    private fun registerNetWork() {
        //if (enableNetWork()) { }
    }

    /**
     * 取消注册网络变化广播
     */
    private fun unregisterNetwork() {
        //if (enableNetWork()) NetworkManager.getInstance().unRegisterObserver(this)
    }

    /**
     * 进行别的初始化
     */
    protected abstract fun initData()
    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen) {
            //设置无标题
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //设置全屏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        super.onCreate(savedInstanceState)
        mThis = this
        setContentView(R.layout.activity_base)
        initEventBus()
        initData_()
        initToolbar()
        initContent()
        initData()
        registerNetWork()
    }

    protected val isFullScreen: Boolean
        get() = false

    /**
     * 隐藏navigationBar
     */
    private fun hideNavigation() {
        val decorView = this.window.decorView
        val uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/
        decorView.systemUiVisibility = uiOptions
    }

    private fun initData_() {
        mContext = this
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //dbManager = BaseApplication.mDbManager;
        //CnBaseApplication.getInstance().addActivity(this);
    }

    protected fun setRegisterTopStyle() {
        mToolbar.setBackgroundColor(Color.WHITE)
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_333))
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    /**
     * 是否全屏（隐藏Toolbar）
     */
    private fun fullScreen(): Boolean {
        val flag = this.window.attributes.flags
        return (flag and WindowManager.LayoutParams.FLAG_FULLSCREEN
                == WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

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
     * 初始化Toolbar
     */
    private fun initToolbar() {
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (fullScreen() || hideToolbar()) {
            mToolbar.visibility = View.GONE
            return
        }
        mToolbar.title = ""
        if (enableNavigation()) {
            mToolbar.setNavigationIcon(R.drawable.ic_back_white)
        }
        setSupportActionBar(mToolbar)
        mTitle = findViewById(R.id.title)
        setTitle(toolbarTite)
    }

    /**
     * 设置标题
     */
    protected fun setTitle(title: String?) {
        if (title != null) {
            mTitle.text = title
        }
    }

    /**
     * 初始化内容布局
     */
    protected fun initContent() {
        mContentLayout = findViewById(R.id.content)
        val contentId = contentLayout
        if (contentId != 0) {
            LayoutInflater.from(mContext).inflate(contentId, mContentLayout)
        }
    }

    /**
     * 公用组件：进度条
     */
    protected fun initProgress(msg: String?) {
        try {
            if (null == progressDialog) {
                progressDialog = MyProgressDialogFragment.newInstance(msg)
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

    /**
     * 公用组件： Toast
     */
    @SuppressLint("CheckResult")
    fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        Toasty.info(this, message!!)
    }

    /**
     * 公用组件： Toast
     */
    @SuppressLint("CheckResult")
    fun showToast(message: Int) {
        if (message == 0) {
            return
        }
        Toasty.info(this, message)
    }

    /**
     * 隐藏输入法
     */
    fun hideKeyboard(view: View) {
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏输入法
     */
    fun hintKbTwo() {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 打开输入法
     */
    fun showKeyboard(view: View) {
        imm!!.showSoftInputFromInputMethod(view.windowToken, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 初始化EventBus
     */
    private fun initEventBus() {
        if (enableEventBus()) {
            registerEventBus()
        }
    }

    private fun quitEventBus() {
        if (enableEventBus()) {
            unregisterEventBus()
        }
    }

    /**
     * 是否启用EventBus
     */
    protected fun enableEventBus(): Boolean {
        return false
    }

    private fun registerEventBus() {
        EventBus.getDefault().register(this)
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    /**
     * 初始化内容视图
     */
    protected abstract val contentLayout: Int

    /**
     * 设置Toolbar标题
     */
    protected abstract val toolbarTite: String?
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
            hintKbTwo()
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

    protected fun jumptoActivity(cls: Class<*>?) {
        gotoActivity(cls)
        finish()
    }

    fun judgeExist(): Boolean {
        return if (mThis == null || mThis!!.isFinishing) {
            false
        } else {
            !mThis!!.isDestroyed
        }
    }

    override fun onDestroy() {
        hideProgress()
        quitEventBus()
        unregisterNetwork()
        super.onDestroy()
    }

    /*//双击后退
    private long    preTime;
    private static final int        TWO_SECOND = 2 * 1000;
    @Override
    public void onBackPressed()
    {
            long currentTime = new Date().getTime();
            if (currentTime - preTime > TWO_SECOND || preTime == 0)
            {
                showToast("再点击一次退出");
                preTime = currentTime;
            } else
            {
                System.exit(0);
            }
    }*/

    companion object {
        private const val DELAY_TIME: Long = 800
    }
}