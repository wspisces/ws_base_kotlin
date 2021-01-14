package com.ws.support.http

import com.ws.support.utils.JsonUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

open class ResultTO {
    // 结果状态
    var code = 0

    // 结果
    var data: Any? = null

    // 结果消息
    var msg: String? = null
    fun getStatus(): Int {
        return code
    }

    fun isSucc(): Boolean {
        return code == 200
    }

    fun data(): String {
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
    }

    override fun toString(): String {
        return JsonUtils.toJson(this)
    }
}