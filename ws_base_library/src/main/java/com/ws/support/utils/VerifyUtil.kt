package com.ws.support.utils

import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 校验工具
 *
 * @author Johnny.xu
 * date 2017/6/28
 */
object VerifyUtil {
    fun verifyIdCard(card: String): Boolean {
        return verForm(card)
    }

    //<------------------身份证格式的正则校验----------------->
    fun verForm(num: String): Boolean {
        val reg: String = "^\\d{15}$|^\\d{17}[0-9Xx]$"
        val matcher: Matcher = Pattern.compile(reg).matcher(num)
        while (matcher.find()) {
            return true
        }
        return false
    }

    //<------------------身份证最后一位的校验算法----------------->
    fun verify(id: CharArray): Boolean {
        var sum = 0
        val w = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
        val ch = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
        for (i in 0 until id.size - 1) {
            sum += (id[i] - '0') * w[i]
        }
        val c = sum % 11
        val code = ch[c]
        var last = id[id.size - 1]
        last = if (last == 'x') 'X' else last
        return last == code
    }
}