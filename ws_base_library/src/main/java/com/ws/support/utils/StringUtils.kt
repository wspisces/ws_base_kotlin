@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.ws.support.utils

import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.net.URLEncoder
import java.util.regex.Pattern

object StringUtils {
    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input 预判文案
     * @return boolean
     */
    fun isEmpty(input: String?): Boolean {
        if (input == null || "" == input) return true
        for (i in 0 until input.length) {
            val c = input[i]
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
    fun isNotEmpty(input: String?): Boolean {
        return !isEmpty(input)
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串或'null'字符串，返回true
     *
     * @param input 预判文案
     * @return boolean
     */
    fun isEmptyWithNull(input: String?): Boolean {
        if (input == null || "" == input || "null" == input) return true
        for (i in 0 until input.length) {
            val c = input[i]
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
    fun isNotEmptyWithNull(input: String?): Boolean {
        return !isEmptyWithNull(input)
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 功能：判断字符是否为数字
     *
     * @param c
     * @return
     */
    fun isNumeric(c: Char): Boolean {
        return c >= '0' && c <= '9'
    }

    fun appArray(strings: List<String?>, sep: String?): String {
        val stringBuilder = StringBuilder()
        for (string in strings) {
            if (stringBuilder.length > 0) {
                stringBuilder.append(sep)
            }
            stringBuilder.append(string)
        }
        return stringBuilder.toString()
    }

    /**
     * 去除数字里多余的0
     *
     * @param number
     * @return
     */
    fun getPrettyNumber(number: String): String {
        return BigDecimal.valueOf(number.toDouble()).stripTrailingZeros().toPlainString()
    }

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     *
     * @param c
     * @return
     */
    fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
    }

    /**
     * 判断字符是否为汉字，不包括符号。
     *
     * @param c 待校验的字符。
     * @return 是否为汉字。
     */
    fun isChineseWord(c: Char): Boolean {
        val pattern = Pattern.compile("[\\u4e00-\\u9fa5]")
        val matcher = pattern.matcher(c.toString())
        return matcher.matches()
    }

    /**
     * 判断传递的文本是否仅包含a-Z和A-Z这些字母。
     *
     * @param text
     * @return
     */
    fun isLetters(text: String?): Boolean {
        if (text == null || text.trim { it <= ' ' } === "") return false
        for (i in 0 until text.length) {
            if (!(text[i] <= 'Z' && text[i] >= 'A'
                            || text[i] <= 'z' && text[i] >= 'a')) {
                //字符不在A-Z或a-z之间，那么整个text就不全是字母
                return false
            }
        }
        return true
    }

    /**
     * 判断传递的文本是否仅包含a-Z和A-Z这些字母。
     *
     * @param c
     * @return
     */
    fun isLetters(c: Char): Boolean {
        return (c in 'A'..'Z'
                || c in 'a'..'z')
    }

    /**
     * 根据拼音数组得到对应首字母的缩写，如“ni hao”得到“nh”。
     *
     * @param chinesePinyin 拼音数组。
     * @return 首字母缩写。
     */
    fun getPinyinAcronym(chinesePinyin: Array<String?>?): String? {
        return if (chinesePinyin == null) {
            null
        } else {
            val sb = StringBuilder()
            for (i in chinesePinyin.indices) {
                if (chinesePinyin[i] == null || "" == chinesePinyin[i]!!.trim { it <= ' ' }) {
                    sb.append("@")
                } else {
                    sb.append(chinesePinyin[i]!![0])
                }
            }
            sb.toString()
        }
    }

    /**
     * 计算text的长度，一个汉字按2个字符记。
     *
     * @param text
     * @return
     */
    fun getTextLength(text: String): Int {
        var length = 0
        for (i in 0 until text.length) {
            length += if (isChinese(text[i])) {
                2
            } else {
                1
            }
        }
        return length
    }

    /**
     * 将传递的字符串trim,如果为null返回空字符串。
     *
     * @param src 要转换的字符串。
     * @return trim后的字符串。
     */
    fun trimString(src: String?): String {
        return src?.trim { it <= ' ' } ?: ""
    }

    /**
     * 距离格式化
     * <P>将距离以米为单位进行格式化，最高单位为千米,当距离为负数时返回空字符串</P>
     *
     * @param distance 距离
     */
    fun formartDistance(distance: Double): String {
        return if (distance > 1000) {
            ConvertUtil.reserveDecimalsFormat(distance / 1000.0, 2).toString() + "km"
        } else if (distance >= 0) {
            ConvertUtil.reserveDecimalsFormat(distance, 2).toString() + "m"
        } else {
            ""
        }
    }

    /**
     * 去除字符串后的0
     *
     * @param s
     * @return
     */
    fun subZeroAndDot(str: String): String {
        var s = str
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "") //去掉多余的0
            s = s.replace("[.]$".toRegex(), "") //如最后一位是.则去掉
        }
        return s
    }

    /**
     * 格式化数据
     *
     * @param value
     * @param defaultValue
     * @return
     */
    fun getFormatStr(value: String?, defaultValue: String?): String? {
        return if (isEmpty(value)) {
            defaultValue
        } else value
    }

    fun encodeUTF(text: String?): String? {
        try {
            return URLEncoder.encode(text, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    fun encode(text: String?, enc: String?): String? {
        try {
            return URLEncoder.encode(text, enc)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 验证手机号码
     *
     * @param mobiles 手机号码字符串
     */
    fun isMobileNO(mobiles: String?): Boolean {
        // 正则表达式
        val phone = "^1[3456789]\\d{9}$"
        val pattern = Pattern.compile(phone)
        val matcher = pattern.matcher(mobiles)
        return matcher.matches()
    }
}