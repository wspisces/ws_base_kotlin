package com.ws.support.http

import android.content.Context
import com.ws.support.utils.NetWorkUtils
import com.ws.support.utils.StringUtils
import com.ws.support.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T> : Observer<T> {
    private var mNeedShowDialog = true
    private var mProgressHandler: ProgressHandler? = null
    private var context: Context

    constructor(context: Context) {
        mProgressHandler = ProgressHandler(context, false)
        this.context = context
    }

    constructor(context: Context, dialogShow: Boolean) {
        mNeedShowDialog = dialogShow
        this.context = context
        if (dialogShow) mProgressHandler = ProgressHandler(context, false)
    }

    override fun onSubscribe(d: Disposable) {
        if (!NetWorkUtils.checkNetworkAvailable()) {
            ToastUtils.error("网络不通")
            onComplete()
            return
        }
        if (mNeedShowDialog && mProgressHandler != null) {
            mProgressHandler!!.showProgressDialog()
        }
    }

    override fun onNext(o: T) {
        if (o is ResultTO && (o as ResultTO).code == 401) {
            ToastUtils.error("401异常")
            return
        }
        onSuccess(o)
    }

    override fun onError(e: Throwable) {
        val errorMsg = e.message
        if (StringUtils.isNotEmptyWithNull(errorMsg)) {
            if (errorMsg!!.contains("Failed to connect to")
                    || errorMsg.contains("Connection refused")) {
                ToastUtils.error("连接服务器失败")
            } else if (errorMsg.contains("Connection timed out")) {
                ToastUtils.error("请求超时")
            } else {
                ToastUtils.error(errorMsg)
            }
        } else {
            ToastUtils.error("请求失败")
        }
        onFailed(e)
        if (mProgressHandler != null) mProgressHandler!!.disMissProgressDialog()
    }

    override fun onComplete() {
        if (mProgressHandler != null) mProgressHandler!!.disMissProgressDialog()
    }

    abstract fun onSuccess(o: T)
    abstract fun onFailed(e: Throwable?)
}