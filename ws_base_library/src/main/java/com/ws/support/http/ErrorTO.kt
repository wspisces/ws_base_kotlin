package com.ws.support.http

class ErrorTO {
    // 字段名称
    private var fieldName: String? = null

    // 错误消息
    private var errorMsg: String? = null
    fun getFieldName(): String? {
        return fieldName
    }

    fun setFieldName(fieldName: String?) {
        this.fieldName = fieldName
    }

    fun getErrorMsg(): String? {
        return errorMsg
    }

    fun setErrorMsg(errorMsg: String?) {
        this.errorMsg = errorMsg
    }
}