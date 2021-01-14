package com.ws.support.http.socket

/**
 * 描述信息
 *
 * @author ws
 * @date 2020/9/18 17:03
 * 修改人：ws
 */
interface IReceiveMessage {
    fun onConnectSuccess() // 连接成功
    fun onConnectFailed() // 连接失败
    fun onClose() // 关闭
    fun onMessage(text: String?)
}