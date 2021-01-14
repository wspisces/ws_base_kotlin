package com.ws.support.utils

import java.util.*

/**
 *
 * Title:UUIDGenerator.java
 *
 * Description:UUID生成工具类
 * @author Johnny.xu
 * @date 2016年11月21日
 */
object UUIDGenerator {
    fun getUUID(): String {
        return UUID.randomUUID().toString().replace("-".toRegex(), "")
    }

    fun getUUID(number: Int): Array<String?> {
        if (number < 1) {
            return arrayOfNulls(0)
        }
        val ss = arrayOfNulls<String>(number)
        for (i in 0 until number) {
            ss[i] = getUUID()
        }
        return ss
    }
}