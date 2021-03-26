package com.ws.support.http_coroutines

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody


/**
 * 响应基类
 * @author ws
 * @date 3/25/21 10:00 AM
 * 修改人：ws
 */
data class BaseResp<T>(
        var code: Int = 0,
        var msg: String = "",
        var `data`: T,
)

fun createRquestBody(params: Map<*, *>): RequestBody {
    // JsonUtils.toJson(params)
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params))
}

/*数据解析扩展函数*/
fun <T> BaseResp<T>.dataConvert(): T {
    if (code == 200) {
        return data
    } else {
        throw Exception(msg)
    }
}


data class Fiction(
        var bid: String = "",
        var bookname: String = "",
)
