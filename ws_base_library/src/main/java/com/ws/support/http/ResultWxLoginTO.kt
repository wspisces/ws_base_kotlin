package com.ws.support.http

class ResultWxLoginTO : ResultTO() {
    var unbindUser: String? = null
    var token: String? = null
    var openId: String? = null
}