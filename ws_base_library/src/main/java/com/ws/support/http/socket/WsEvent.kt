package com.ws.support.http.socket

/**
 * 描述信息
 *
 * @author ws
 * 2020/9/28 15:20
 * 修改人：ws
 */
class WsEvent {
    var content: String? = null
    var status: Int

    constructor(status: Int, content: String?) {
        this.status = status
        this.content = content
    }

    constructor(status: Int) {
        this.status = status
    }
}