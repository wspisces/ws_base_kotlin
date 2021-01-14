package com.ws.support.http.socket

/**
 * 描述信息
 *
 * @author ws
 * @date 2020/9/18 17:27
 * 修改人：ws
 */
interface IRetryStrategy {
    fun retry(url: String)
    fun reset()
}