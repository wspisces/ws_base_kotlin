package com.websocket

import com.ws.support.http.socket.IRetryStrategy
import com.ws.support.http.socket.WsManager
import io.reactivex.Flowable
import io.reactivex.subscribers.DisposableSubscriber
import java.util.concurrent.TimeUnit


/**
 * 描述信息
 * @author ws
 * @date 2020/9/18 17:28
 * 修改人：ws
 */
class FibonacciRetryStrategyImpl : IRetryStrategy {

    private var lastTimeInterval: Long = 0
    private var currentTimeInterval: Long = 1
    private var subscriber: DisposableSubscriber<Long>? = null

    override fun retry(url: String) {
        var retryInterval = lastTimeInterval + currentTimeInterval
        lastTimeInterval = currentTimeInterval
        currentTimeInterval = retryInterval

        if (retryInterval > MAX_INTERVAL) {
            retryInterval = MAX_INTERVAL
        }

        //logi { "retryInterval = $retryInterval" }

        subscriber?.dispose()
        subscriber = Flowable.intervalRange(0, retryInterval, 0, 1, TimeUnit.SECONDS)
                .subscribeWith(object : DisposableSubscriber<Long>() {
                    override fun onComplete() {
                        WsManager.getInstance().startConnect()
                    }

                    override fun onNext(t: Long?) {}
                    override fun onError(t: Throwable?) {}
                })
    }

    override fun reset() {
        lastTimeInterval = 0
        currentTimeInterval = 1
        subscriber?.dispose()
        subscriber = null
    }

    companion object {
        private const val MAX_INTERVAL = 3600L   //即一个小时
    }
}

