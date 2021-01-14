package com.ws.component

import android.text.InputFilter
import android.text.Spanned

/**
 * 描述信息
 *
 * @author ws
 * 2020/9/10 18:45
 * 修改人：ws
 */
class InputFilterFloatMinMax : InputFilter {
    private var min: Float
    private var max: Float

    init {

    }
    constructor(min: Int, max: Int) {
        this.min = min.toFloat()
        this.max = max.toFloat()
    }
    constructor(min: Float, max: Float) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = min.toFloat()
        this.max = max.toFloat()
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            //限制小数点位数
            if (source == "." && dest.toString().length == 0) {
                return "0."
            }
            if (dest.toString().contains(".")) {
                val index = dest.toString().indexOf(".")
                val mlength = dest.toString().substring(index).length
                if (mlength == 3) {
                    return ""
                }
            }
            //限制大小
            val input = (dest.toString() + source.toString()).toFloat()
            if (isInRange(min, max, input)) {
                //ToastUtils.shortT("输入超出范围");
                return ""
            }
        } catch (ignored: Exception) {
        }
        return ""
    }

    private fun isInRange(a: Float, b: Float, c: Float): Boolean {
        return if (b > a) c >= a && c <= b else c >= b && c <= a
    }
}