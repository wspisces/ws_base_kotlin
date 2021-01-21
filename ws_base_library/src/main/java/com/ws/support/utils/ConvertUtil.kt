package com.ws.support.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import java.math.BigDecimal

/**
 * 转换工具类
 *
 * @author Johnny.xu
 * date 2017/3/29
 */
object ConvertUtil {
    /**
     * 字符串转换（将空字符串转换成默认提示）
     * @param str 字符串
     * @param prompt 提示文字
     * @return 转换后的文字
     */
    /**
     * 字符串转换（将空字符串转换成默认提示），如果为空则转换成'暂无'
     * @param str 字符串
     * @return 转换后的文字
     */
    @JvmOverloads
    fun convertDefaultString(str: String?, prompt: String? = "暂无"): String? {
        return if (StringUtils.isEmpty(str)) {
            prompt
        } else {
            str
        }
    }

    /**
     * 转换数字数据
     * @param num 数
     * @param index 保留几位小数，如保留整数则为0
     * @return BigDecimal对象，直接调用其doubleValue()、floatValue()、intValue()、toPlainString()
     */
    fun reserveDecimalsFormat(num: Double, index: Int): BigDecimal {
        val bg: BigDecimal
        bg = if (java.lang.Double.isNaN(num) || java.lang.Double.isInfinite(num)) {
            BigDecimal(0)
        } else {
            BigDecimal(num)
        }
        return bg.setScale(index, BigDecimal.ROUND_HALF_UP)
    }

    /**
     * 去除数据小数后面的0
     */
    fun formatNumber(num: String): String {
        var number = num
        if (number.contains(".")) {
            while (number.endsWith("0")) {
                number = number.substring(0, number.length - 1)
            }
            if (number.endsWith(".")) {
                number = number.substring(0, number.length - 1)
            }
        }
        return number
    }

    /**
     * 自定义文字颜色
     */
    fun getCustomTextStyle(prompt: String, changePrompt: String, color: Int): SpannableStringBuilder {
        val startIndex = prompt.indexOf(changePrompt)
        val fend = startIndex + changePrompt.length
        val style = SpannableStringBuilder(prompt)
        style.setSpan(ForegroundColorSpan(color), startIndex, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return style
    }
}