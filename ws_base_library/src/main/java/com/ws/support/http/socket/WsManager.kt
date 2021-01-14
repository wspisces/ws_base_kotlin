package com.ws.support.http.socket

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ws.support.http.socket.WsStatus.CODE
import com.ws.support.http.socket.WsStatus.TIP
import okhttp3.*
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 描述信息
 *
 * @author ws
 * 2020/9/18 17:10
 * 修改人：ws
 */
class WsManager : IWsManager {
    private var mContext: Context? = null
    private var wsUrl: String? = null
    private var mWebSocket: WebSocket? = null
    private var mOkHttpClient: OkHttpClient? = null
    private var mRequest: Request? = null
    private var mCurrentStatus = WsStatus.DISCONNECTED //websocket连接状态
    private var isNeedReconnect //是否需要断线自动重连
            = false
    private var isManualClose = false //是否为手动关闭websocket连接
    private var mLock: Lock? = null
    private val wsMainHandler = Handler(Looper.getMainLooper())
    private var reconnectCount = 0 //重连次数
    private val reconnectRunnable = Runnable {
        Log.e("websocket", "服务器重连接中...")
        buildConnect()
    }
    private val mWebSocketListener: WebSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
            mCurrentStatus = WsStatus.CONNECTED
            EventBus.getDefault().post(WsEvent(WsStatus.CONNECTED))
            connected()
            if (Looper.myLooper() != Looper.getMainLooper()) {
                //send("connect");
            } else {
                Log.e("websocket", "服务器连接成功")
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post { Log.e("websocket", "WsManager-----onMessage") }
            } else {
                Log.e("websocket", "WsManager-----onMessage")
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            EventBus.getDefault().post(WsEvent(WsStatus.CONNECTED, text))
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post { Log.e("websocket", "WsManager-----onMessage=$text") }
            } else {
                Log.e("websocket", "WsManager-----onMessage")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post { Log.e("websocket", "服务器连接关闭中") }
            } else {
                Log.e("websocket", "服务器连接关闭中")
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            EventBus.getDefault().post(WsEvent(WsStatus.DISCONNECTED))
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post { Log.e("websocket", "服务器连接已关闭") }
            } else {
                Log.e("websocket", "服务器连接已关闭")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            try {
                tryReconnect()
                Log.e("liusehngjei", "[走的链接失败这里！！！！！！！！！！！！！！！！]")
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post { Log.e("websocket", "服务器连接失败") }
                } else {
                    Log.e("websocket", "服务器连接失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private constructor() {}
    constructor(builder: Builder) {
        mContext = builder.mContext
        wsUrl = builder.wsUrl
        isNeedReconnect = builder.needReconnect
        mOkHttpClient = builder.mOkHttpClient
        mLock = ReentrantLock()
    }

    private fun initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build()
        }
        if (mRequest == null) {
            mRequest = Request.Builder()
                    .url(wsUrl)
                    .build()
        }
        mOkHttpClient!!.dispatcher().cancelAll()
        try {
            mLock!!.lockInterruptibly()
            try {
                mOkHttpClient!!.newWebSocket(mRequest, mWebSocketListener)
            } finally {
                mLock!!.unlock()
            }
        } catch (e: InterruptedException) {
        }
    }

    override fun getWebSocket(): WebSocket? {
        return mWebSocket
    }

    @Synchronized
    override fun isWsConnected(): Boolean {
        return mCurrentStatus == WsStatus.CONNECTED
    }

    @Synchronized
    override fun getCurrentStatus(): Int {
        return mCurrentStatus
    }

    @Synchronized
    override fun setCurrentStatus(currentStatus: Int) {
        mCurrentStatus = currentStatus
    }

    override fun startConnect() {
        isManualClose = false
        buildConnect()
    }

    override fun stopConnect() {
        isManualClose = true
        disconnect()
    }

    private fun tryReconnect() {
        if (!isNeedReconnect or isManualClose) {
            return
        }
        Log.e("liusehngjei", "reconnectCount2222222[$reconnectCount]")
        if (!isNetworkConnected(mContext)) {
            mCurrentStatus = WsStatus.DISCONNECTED
            Log.e("liusehngjei", "[请您检查网络，未连接]")
            //            return;
        }
        mCurrentStatus = WsStatus.RECONNECT
        Log.e("liusehngjei", "reconnectCount11111111[$reconnectCount]")
        val delay = reconnectCount * RECONNECT_INTERVAL.toLong()
        //        wsMainHandler.postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        wsMainHandler.postDelayed(reconnectRunnable, 10000)
        Log.e("liusehngjei", "reconnectCount[$reconnectCount]")
        reconnectCount++
    }

    private fun cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable)
        reconnectCount = 0
    }

    private fun connected() {
        cancelReconnect()
    }

    private fun disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) {
            return
        }
        cancelReconnect()
        if (mOkHttpClient != null) {
            mOkHttpClient!!.dispatcher().cancelAll()
        }
        if (mWebSocket != null) {
            val isClosed = mWebSocket!!.close(CODE.NORMAL_CLOSE, TIP.NORMAL_CLOSE)
            //非正常关闭连接
            if (!isClosed) {
                Log.e("websocket", "服务器连接失败")
            }
        }
        mCurrentStatus = WsStatus.DISCONNECTED
    }

    @Synchronized
    private fun buildConnect() {
        if (!isNetworkConnected(mContext)) {
            mCurrentStatus = WsStatus.DISCONNECTED
            //            return;
        }
        when (mCurrentStatus) {
            WsStatus.CONNECTED, WsStatus.CONNECTING -> {
            }
            else -> {
                mCurrentStatus = WsStatus.CONNECTING
                initWebSocket()
            }
        }
    }

    //发送消息
    override fun sendMessage(msg: String): Boolean {
        return send(msg)
    }

    override fun sendMessage(byteString: ByteString): Boolean {
        return send(byteString)
    }

    private fun send(msg: Any): Boolean {
        var isSend = false
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg is String) {
                isSend = mWebSocket!!.send(msg)
            } else if (msg is ByteString) {
                isSend = mWebSocket!!.send(msg)
            }
            //发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect()
            }
        }
        return isSend
    }

    //检查网络是否连接
    private fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            @SuppressLint("MissingPermission") val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    class Builder(val mContext: Context) {
        var wsUrl: String? = null
        var needReconnect = true
        var mOkHttpClient: OkHttpClient? = null
        fun wsUrl(`val`: String?): Builder {
            wsUrl = `val`
            return this
        }

        fun client(`val`: OkHttpClient?): Builder {
            mOkHttpClient = `val`
            return this
        }

        fun needReconnect(`val`: Boolean): Builder {
            needReconnect = `val`
            return this
        }

        fun build(): WsManager {
            return WsManager(this)
        }
    } //实例化WsMnanger

    /*WsManager wsManager = new WsManager.Builder(getBaseContext()).client(
            new OkHttpClient().newBuilder()
                    .pingInterval(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build())
            .needReconnect(true)
            .wsUrl("ws://192.168.3.145:80/MayaCloud_Lightpush//ws")
            .build();*/
    //连接可以直接使用
    /*wsManager.startConnect();*/ //停止链接使用下面方法
    /*  if (wsManager != null) {
      wsManager.stopConnect();
      wsManager = null;
    }*/
    //onDestory（）里面要记得停止链接
    /*@Override
    protected void onDestroy() {
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }
    }*/
    companion object {
        private const val RECONNECT_INTERVAL = 10 * 1000 //重连自增步长
        private const val RECONNECT_MAX_TIME = 120 * 1000 //最大重连间隔
                .toLong()
        private var mInstance: WsManager? = null
        fun getInstance(): WsManager {
            if (null == mInstance) {
                synchronized(WebSocketManager::class.java) {
                    if (mInstance == null) {
                        mInstance = WsManager()
                    }
                }
            }
            return mInstance!!
        }
    }
}