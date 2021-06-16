package com.ws.support.base.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ws.support.utils.ToastUtils.normal
import com.ws.support.widget.MyProgressDialogFragment
import com.ws.support.widget.MyProgressDialogFragment.Companion.newInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import java.util.concurrent.TimeUnit

/**
 * 描述信息
 *
 * @author ws
 * @date 12/23/20 2:30 PM
 * 修改人：ws
 */
@Suppress("DEPRECATION")
abstract class BaseBindingFragment<DB : ViewDataBinding?> : Fragment() {
    protected lateinit var mContext: Context
    private var mDialog: MyProgressDialogFragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity!!
        val db: DB = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView(db)
        return db!!.root
    }

    /**
     * 获取资源ID
     *
     * @return 布局资源ID
     */
    protected abstract val layoutId: Int

    /**
     * 初始化界面
     *
     * @param bindView 界面绑定对象
     */
    protected abstract fun initView(bindView: DB)
    protected abstract fun tag(): String?

    /**
     * 初始化Fragment。可通过参数savedInstanceState获取之前保存的值。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = tag
    }

    /**
     * 公用组件：进度条
     */
    protected fun initProgress(msg: String) {
        try {
            if (null == mDialog) {
                mDialog = newInstance(msg)
                mDialog!!.isCancelable = true
            }

            mDialog!!.show(requireFragmentManager())
            mDialog!!.setMessage(msg)
        } catch (ignored: Exception) {
        }
    }

    /**
     * 公用组件：关闭进度条
     */
    protected fun hideProgress() {
        val activity = activity
        if (activity != null && !activity.isFinishing) {
            activity.runOnUiThread(Runnable { if (null != mDialog && mDialog!!.dialog != null && mDialog!!.dialog!!.isShowing) mDialog!!.dismiss() })
        }
    }

    /**
     * 公用组件： Toast
     */
    fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        normal(message!!)
    }

    //跳转
    protected fun gotoActivity(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(activity, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun gotoActivity(cls: Class<*>?) {
        startActivity(Intent(activity, cls))
    }

    protected fun jumptoActivity(cls: Class<*>?, bundle: Bundle?) {
        gotoActivity(cls, bundle)
        requireActivity().finish()
    }

    protected fun jumptoActivity(cls: Class<*>?) {
        gotoActivity(cls)
        requireActivity().finish()
    }

    @SuppressLint("CheckResult")
    protected fun delayEnable(view: View, delay: Long) {
        Observable.timer(delay, TimeUnit.MILLISECONDS, mainThread())
                .observeOn(mainThread())
                .subscribeOn(mainThread())
                .subscribe { view.isEnabled = true }
    }

    companion object {
        const val REQUEST_CODE_CHOOSE = 1001
        var TAG: String? = ""
    }
}