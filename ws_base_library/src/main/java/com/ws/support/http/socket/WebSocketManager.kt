package com.ws.support.http.socket

import android.os.Handler
import android.util.Log
import com.orhanobut.logger.Logger
import com.ws.support.utils.StringUtils
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * 描述信息
 *
 * @author ws
 * 2020/9/18 17:02
 * 修改人：ws
 */
// An highlighted block
class WebSocketManager private constructor() {
    private var client: OkHttpClient? = null
    private var request: Request? = null
    private var receiveMessage: IReceiveMessage? = null
    private var mWebSocket: WebSocket? = null
    private var isConnect = false
    private var connectNum = 0

    //心跳包发送时间计时
    private var sendTime = 0L

    // 发送心跳包
    private var mHandler: Handler? = Handler()
    private val WSURL = "你的Websocket地址"
    fun init(message: IReceiveMessage?) {
        client = OkHttpClient.Builder()
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        request = Request.Builder().url(WSURL).build()
        receiveMessage = message
        connect()
    }

    /**
     * 连接
     */
    fun connect() {
        if (isConnect()) {
            Logger.i("WebSocket 已经连接！")
            return
        }
        client!!.newWebSocket(request, createListener())
    }

    /**
     * 重连
     */
    fun reconnect() {
        if (connectNum <= MAX_NUM) {
            try {
                Thread.sleep(MILLIS.toLong())
                connect()
                connectNum++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            Logger.i("reconnect over " + MAX_NUM + ",please check url or network")
        }
    }

    /**
     * 是否连接
     */
    fun isConnect(): Boolean {
        return mWebSocket != null && isConnect
    }

    // 发送心跳包
    private val heartBeatRunnable: Runnable = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                sendTime = System.currentTimeMillis()
                //boolean isSend = sendMessage(App.getInstance().getWSHeart());
                //LogUtils.i("心跳是否发送成功"+isSend);
            }
            mHandler!!.postDelayed(this, HEART_BEAT_RATE) //每隔一定的时间，对长连接进行一次心跳检测
        }
    }

    /**
     * 发送消息
     *
     * @param text 字符串
     * @return boolean
     */
    fun sendMessage(text: String): Boolean {
        return if (!isConnect()) false else mWebSocket!!.send(text)
    }

    /**
     * 发送消息
     *
     * @param byteString 字符集
     * @return boolean
     */
    fun sendMessage(byteString: ByteString): Boolean {
        return if (!isConnect()) false else mWebSocket!!.send(byteString)
    }

    /**
     * 关闭连接
     */
    fun close() {
        if (isConnect()) {
            mWebSocket!!.cancel()
            mWebSocket!!.close(1001, "客户端主动关闭连接")
        }
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
            mHandler = null
        }
    }

    private fun createListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Logger.i("WebSocket 打开:$response")
                mWebSocket = webSocket
                isConnect = response.code() == 101
                if (!isConnect) {
                    reconnect()
                } else {
                    Logger.i("WebSocket 连接成功")
                    if (receiveMessage != null) {
                        receiveMessage!!.onConnectSuccess()
                    }
                    //                    if (sendMessage(App.getInstance().getWSLogin())) {
//                        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
//                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                if (receiveMessage != null) {
                    receiveMessage!!.onMessage(text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                if (receiveMessage != null) {
                    receiveMessage!!.onMessage(bytes.base64())
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                mWebSocket = null
                isConnect = false
                if (mHandler != null) {
                    mHandler!!.removeCallbacksAndMessages(null)
                }
                if (receiveMessage != null) {
                    receiveMessage!!.onClose()
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                mWebSocket = null
                isConnect = false
                if (mHandler != null) {
                    mHandler!!.removeCallbacksAndMessages(null)
                }
                if (receiveMessage != null) {
                    receiveMessage!!.onClose()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                if (response != null) {
                    Logger.i("WebSocket 连接失败：" + response.message())
                }
                Logger.i("WebSocket 连接失败异常原因：" + t.message)
                isConnect = false
                if (mHandler != null) {
                    mHandler!!.removeCallbacksAndMessages(null)
                }
                if (receiveMessage != null) {
                    receiveMessage!!.onConnectFailed()
                }
                if (!StringUtils.isEmpty(t.message) && t.message != "Socket closed") {
                    reconnect()
                }
            }
        }
    }

    companion object {
        //private final static String TAG = WebSocketManager.class.getSimpleName();
        private const val MAX_NUM = 5 // 最大重连数
        private const val MILLIS = 5000 // 重连间隔时间，毫秒
        private var mInstance: WebSocketManager? = null

        // 每隔40秒发送一次心跳包，检测连接没有断开
        private const val HEART_BEAT_RATE = 40 * 1000.toLong()
        fun getInstance(): WebSocketManager? {
            if (null == mInstance) {
                synchronized(WebSocketManager::class.java) {
                    if (mInstance == null) {
                        mInstance = WebSocketManager()
                    }
                }
            }
            return mInstance
        }

        /**
         * 释放单例, 及其所引用的资源
         */
        fun release() {
            try {
                if (mInstance != null) {
                    mInstance = null
                }
            } catch (e: Exception) {
                Log.e("WebSocketManager", "release : $e")
            }
        }
    }
}