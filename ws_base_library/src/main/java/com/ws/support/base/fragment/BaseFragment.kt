package com.ws.support.base.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.ws.support.utils.ToastUtils
import com.ws.support.widget.MyProgressDialogFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Fragment基础类
 *
 * @author Johnny.xu
 * date 2017/2/16
 */
@Suppress("DEPRECATION")
abstract class BaseFragment : Fragment() {
    protected var mContext: Context? = null
    private var mDialog: MyProgressDialogFragment? = null
    protected abstract fun tag(): String?

    /**
     * 执行该方法时，Fragment与Activity已经完成绑定，该方法有一个Activity类型的参数，
     * 代表绑定的Activity，这时候你可以执行诸如mActivity = activity的操作。
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity

    }

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
                mDialog = MyProgressDialogFragment.Companion.newInstance(msg)
                mDialog!!.isCancelable = true
            }
            mDialog!!.setMessage(msg)
            mDialog!!.show(fragmentManager)
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
        ToastUtils.normal(message!!)
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
        activity!!.finish()
    }

    protected fun jumptoActivity(cls: Class<*>?) {
        gotoActivity(cls)
        activity!!.finish()
    }

    @SuppressLint("CheckResult")
    protected fun delayEnable(view: View, delay: Long) {
        Observable.timer(delay, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { view.isEnabled = true }
        //new Handler().postDelayed(() -> view.setEnabled(true), delay);
    }

    companion object {
        const val REQUEST_CODE_CHOOSE = 1001
        var TAG: String? = ""
    }
}