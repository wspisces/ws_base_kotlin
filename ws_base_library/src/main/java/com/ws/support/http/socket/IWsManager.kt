package com.ws.support.http.socket

import okhttp3.WebSocket
import okio.ByteString

/**
 * 描述信息
 *
 * @author ws
 * @date 2020/9/18 17:10
 * 修改人：ws
 */
interface IWsManager {
    fun getWebSocket(): WebSocket?
    fun startConnect()
    fun stopConnect()
    fun isWsConnected(): Boolean
    fun getCurrentStatus(): Int
    fun setCurrentStatus(currentStatus: Int)
    fun sendMessage(msg: String): Boolean
    fun sendMessage(byteString: ByteString): Boolean
}