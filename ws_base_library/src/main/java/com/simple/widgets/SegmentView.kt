/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.simple.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.ws.base.R

/**
 * SegmentView是一个类似于iOS的segment
 * control显示效果的一个控件，使用RadioGroup与RadioButton实现，使用时用户需要调用[.setTabs]
 * 方法设置tabs,然后调用 [.setOnItemCheckedListener]
 * 设置点击每个tab时的回调函数.
 *
 * @author mrsimple
 */
class SegmentView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : RadioGroup(context, attrs) {
    var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 默认的RadioButton id,从0 开始
     */
    var ids = 0

    /**
     * RadioButton的数量
     */
    //var childCount = 0

    /**
     * 绘制分割线时的padding值
     */
    var linePadding = 15

    /**
     * 选中回调
     */
    private var mCheckedListener: OnItemCheckedListener? = null
    private fun setupOnItenClickListener() {
        super.setOnCheckedChangeListener { group, checkedId -> // 包装回调
            if (mCheckedListener != null) {
                val checkedButton = findViewById<View>(checkedId) as RadioButton
                mCheckedListener!!.onCheck(checkedButton, checkedId, checkedButton.text
                        .toString())
            }
        }
    }

    fun setTabs(tabTitles: Array<String?>) {
        removeAllViews()
        for (i in tabTitles.indices) {
            val title = tabTitles[i]
            if (i == 0) {
                addTabLeft(title)
            } else if (i == tabTitles.size - 1) {
                addTabRight(title)
            } else {
                addTabCenter(title)
            }
        }
    }

    fun setTabs(tabTitles: List<String?>) {
        removeAllViews()
        for (i in tabTitles.indices) {
            val title = tabTitles[i]
            if (i == 0) {
                addTabLeft(title)
            } else if (i == tabTitles.size - 1) {
                addTabRight(title)
            } else {
                addTabCenter(title)
            }
        }
    }

    fun addTabLeft(title: String?) {
        val radioButton = LayoutInflater.from(context).inflate(
                R.layout.radio_button_item_left, this, false) as RadioButton
        radioButton.id = ids++
        radioButton.text = title
        radioButton.minimumWidth = 150
        radioButton.isChecked = true
        // 添加到当前ViewGroup中
        this.addView(radioButton)
    }

    fun addTabCenter(title: String?) {
        val radioButton = LayoutInflater.from(context).inflate(
                R.layout.radio_button_item_center, this, false) as RadioButton
        radioButton.id = ids++
        radioButton.minimumWidth = 150
        radioButton.text = title
        // 添加到当前ViewGroup中
        this.addView(radioButton)
    }

    fun addTabRight(title: String?) {
        val radioButton = LayoutInflater.from(context).inflate(
                R.layout.radio_button_item_right, this, false) as RadioButton
        radioButton.id = ids++
        radioButton.minimumWidth = 150
        radioButton.text = title
        // 添加到当前ViewGroup中
        this.addView(radioButton)
    }

    fun addTab(title: String?) {
        val radioButton = LayoutInflater.from(context).inflate(
                R.layout.radio_button_item, this, false) as RadioButton
        radioButton.minimumWidth = 150
        radioButton.id = ids++
        radioButton.text = title
        // 添加到当前ViewGroup中
        this.addView(radioButton)
    }

    /**
     * 绘制tab之间的分割线
     *
     * @param canvas
     */
    private fun drawSeparateLines(canvas: Canvas) {
        var childCount = getChildCount()
        require(childCount != 0) { "SegmentView's child is zero !" }
        if (orientation == HORIZONTAL) {
            val childWidth = this.width / childCount
            for (i in 1 until childCount) {
                val startX = childWidth * i
                canvas.drawLine(startX.toFloat(), linePadding.toFloat(), startX.toFloat(), (this.height
                        - linePadding).toFloat(), mPaint)
            }
        } else {
            val childHeight = this.height / childCount
            for (i in 1 until childCount) {
                val startY = childHeight * i
                canvas.drawLine(linePadding.toFloat(), startY.toFloat(), this.width - linePadding.toFloat(), startY.toFloat(), mPaint)
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawSeparateLines(canvas)
    }

    fun setOnItemCheckedListener(listener: OnItemCheckedListener?) {
        mCheckedListener = listener
    }

    /*
     * 使用 @see setOnItemCheckedListener 来设置回调
     * @see android.widget.RadioGroup#setOnCheckedChangeListener(android.widget.
     * RadioGroup.OnCheckedChangeListener)
     */
    @Deprecated("")
    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
    }

    fun setSelectId(i: Int) {
        val rbtn = findViewById<RadioButton>(i)
        if (rbtn != null) rbtn.isChecked = true
    }

    /**
     * tab点击时的回调接口类
     *
     * @author mrsimple
     */
    interface OnItemCheckedListener {
        /**
         * @param button   被选中的按钮
         * @param position 被选中的按钮所在的位置
         * @param title    被选中的按钮的文本,即标题
         */
        fun onCheck(button: RadioButton?, position: Int, title: String?)
    }

    init {
        orientation = HORIZONTAL
        mPaint.color = resources.getColor(R.color.colorPrimary)
        setupOnItenClickListener()
    }
}