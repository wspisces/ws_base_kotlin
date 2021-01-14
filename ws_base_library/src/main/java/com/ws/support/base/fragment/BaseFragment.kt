package com.ws.support.base.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ws.support.base.listener.IActivityOperationCallback
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
abstract class BaseFragment : Fragment() {
    protected var mContext: Context? = null
    private var mDialog: MyProgressDialogFragment? = null
    private var mIActivityOperationCallback: IActivityOperationCallback? = null
    protected abstract fun tag(): String?

    /**
     * 执行该方法时，Fragment与Activity已经完成绑定，该方法有一个Activity类型的参数，
     * 代表绑定的Activity，这时候你可以执行诸如mActivity = activity的操作。
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
        if (activity is IActivityOperationCallback) {
            mIActivityOperationCallback = activity
        }
    }

    /**
     * 初始化Fragment。可通过参数savedInstanceState获取之前保存的值。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = tag
    }

    /**
     * 初始化Fragment的布局。加载布局和findViewById的操作通常在此函数内完成，
     * 但是不建议执行耗时的操作，比如读取数据库数据列表。
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 执行该方法时，与Fragment绑定的Activity的onCreate方法已经执行完成并返回，
     * 在该方法内可以进行与Activity交互的UI操作，所以在该方法之前Activity的onCreate方法并未执行完成，
     * 如果提前进行交互操作，会引发空指针异常。
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * 执行该方法时，Fragment由不可见变为可见状态。
     */
    override fun onStart() {
        super.onStart()
    }

    /**
     * 执行该方法时，Fragment处于活动状态，用户可与之交互。
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * 执行该方法时，Fragment处于暂停状态，但依然可见，用户不能与之交互。
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * 保存当前Fragment的状态。该方法会自动保存Fragment的状态，比如EditText键入的文本，
     * 即使Fragment被回收又重新创建，一样能恢复EditText之前键入的文本。
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * 执行该方法时，Fragment完全不可见。
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * 销毁与Fragment有关的视图，但未与Activity解除绑定，依然可以通过onCreateView方法重新创建视图。
     * 通常在ViewPager+Fragment的方式下会调用此方法。
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * 销毁Fragment。通常按Back键退出或者Fragment被回收时调用此方法。
     */
    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 解除与Activity的绑定。在onDestroy方法之后调用。
     */
    override fun onDetach() {
        super.onDetach()
    }

    /**
     * 页面隐藏状态改变
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
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
        ToastUtils.normal(message)
        //EasyToastUtil.showToast(message);
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
                .subscribe { aLong: Long? -> view.isEnabled = true }
        //new Handler().postDelayed(() -> view.setEnabled(true), delay);
    }

    companion object {
        const val REQUEST_CODE_CHOOSE = 1001
        var TAG: String? = ""
    }
}