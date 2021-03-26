package com.ws.support.http

import com.ws.support.utils.StringUtils
import com.ws.support.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver2<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {

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
    }

    override fun onComplete() {
    }

    abstract fun onSuccess(o: T)
    abstract fun onFailed(e: Throwable?)
}