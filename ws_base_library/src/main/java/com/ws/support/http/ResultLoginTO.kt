package com.ws.support.http

import com.ws.support.utils.JsonUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ResultLoginTO : ResultTO() {
    var isNewUser = 0
    var token: String? = null
}

data class LoginResult(
        var code: Int = 0,
        var msg: String = "",
        //var `data`: Any?,
        var token: String = "",
) {
    fun isSucc(): Boolean {
        return code == 200
    }

    /*    fun data(): String {
            val str: String = JsonUtils.toJson(data)
            return if ("null".equals(str, ignoreCase = true) || "{}".equals(str, ignoreCase = true)) {
                ""
            } else str
        }

        @kotlin.Throws(JSONException::class)
        fun toJsonObject(): JSONObject {
            return JSONObject(JsonUtils.toJson(data))
        }

        @kotlin.Throws(JSONException::class)
        fun toJsonArray(): JSONArray {
            return JSONArray(JsonUtils.toJson(data))
        }*/
    fun tokenConvert(): String {
        if (isSucc()) {
            return token
        } else {
            throw Exception(msg)
        }
    }
}

/*数据解析扩展函数*/
/*
fun LoginResult.tokenConvert(): String {
    if (isSucc()) {
        return token
    } else {
        throw Exception(msg)
    }
}*/
