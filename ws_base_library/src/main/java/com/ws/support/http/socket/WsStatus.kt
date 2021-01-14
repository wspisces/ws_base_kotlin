package com.ws.support.http.socket

/**
 * 描述信息
 *
 * @author ws
 * 2020/9/18 17:10
 * 修改人：ws
 */
object WsStatus {
    const val CONNECTED = 1
    const val CONNECTING = 0
    const val RECONNECT = 2
    const val DISCONNECTED = -1

    internal object CODE {
        const val NORMAL_CLOSE = 1000
        const val ABNORMAL_CLOSE = 1001
    }

    internal object TIP {
        const val NORMAL_CLOSE = "normal close"
        const val ABNORMAL_CLOSE = "abnormal close"
    }
}