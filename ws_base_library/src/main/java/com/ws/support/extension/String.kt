package com.ws.support.extension

import android.content.Context
import android.widget.Toast
import com.ws.support.base.BaseApplication
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLEncoder
import java.util.regex.Pattern


/**
 * String扩展类
 * @author ws
 * @date 1/18/21 11:15 AM
 * 修改人：ws
 */
//region extension 自定义扩展
fun String.showToast(duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(BaseApplication.context,this,duration).show()
}

fun String.isMobileNO(): Boolean {
    // 正则表达式
    val phone = "^1[3456789]\\d{9}$"
    val pattern = Pattern.compile(phone)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.encodeUTF(): String? {
    try {
        return URLEncoder.encode(this, "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return null
}

fun String.encode(enc: String?): String? {
    try {
        return URLEncoder.encode(this, enc)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return null
}

/**
 * 去除字符串后的0
 *
 * @param s
 * @return
 */
fun String.subZeroAndDot(): String {
    var s = this
    if (s.indexOf(".") > 0) {
        s = s.replace("0+?$".toRegex(), "") //去掉多余的0
        s = s.replace("[.]$".toRegex(), "") //如最后一位是.则去掉
    }
    return s
}

/**
 * 判断给定字符串是否空白串。
 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
 * 若输入字符串为null或空字符串，返回true
 *
 * @param input 预判文案
 * @return boolean
 */
fun String.isEmpty(): Boolean {
    if ("" == this) return true
    for (element in this) {
        val c = element
        if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
            return false
        }
    }
    return true
}

/**
 * 判断给定字符串是否空白串。
 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
 * 若输入字符串为null或空字符串，返回true
 *
 * @param input 预判文案
 * @return boolean
 */
fun String.isNotEmpty(): Boolean {
    return !isEmpty()
}

/**
 * 判断给定字符串是否空白串。
 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
 * 若输入字符串为null或空字符串或'null'字符串，返回true
 *
 * @param input 预判文案
 * @return boolean
 */
fun String.isEmptyWithNull(): Boolean {
    if ("" == this || "null" == this) return true
    for (element in this) {
        val c = element
        if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
            return false
        }
    }
    return true
}

/**
 * 判断给定字符串是否空白串。
 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
 * 若输入字符串为null或空字符串或'null'字符串，返回true
 *
 * @param input 预判文案
 * @return boolean
 */
fun String.isNotEmptyWithNull(): Boolean {
    return !isEmptyWithNull()
}

/**
 * 功能：判断字符串是否为数字
 *
 * @param str
 * @return
 */
fun String.isNumeric(): Boolean {
    val pattern = Pattern.compile("[0-9]*")
    val isNum = pattern.matcher(this)
    return isNum.matches()
}

fun String.letterCount(): Int {
    var count = 0
    for (char in this) {
        if (char.isLetter()) {
            count++
        }
    }
    return count
}
//endregion

//region operator 重载
/*operator  fun String.times(n:Int):String{
    val builder = StringBuilder()
    repeat(n){
        builder.append(this)
    }
    return builder.toString()
}*/
operator  fun String.times(n:Int) = repeat(n)
//endregion