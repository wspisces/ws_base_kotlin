package com.ws.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.ws.base.R

/**
 * 描述信息
 *
 * @author ws
 * @date 12/21/20 3:52 PM
 * 修改人：ws
 */
class CodeEditText(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val box: Int
    private var boxWidth = 120
    private var boxHeight = 120
    private var childHPadding = 14
    private var childVPadding = 14
    private var inputType: String? = TYPE_PASSWORD
    private var boxBgFocus: Drawable? = null
    private var boxBgNormal: Drawable? = null
    private var listener: Listener? = null
    //private var widthC: Int = 0
    //private var heightC: Int = 0
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun initViews() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {
                } else {
                    focus()
                    checkAndCommit()
                }
            }
        }
        val onKeyListener = OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                backFocus()
            }
            false
        }
        //widthC = getWidth()
        //heightC = getHeight()
        for (i in 0 until box) {
            val editText = EditText(context)
            val layoutParams = LinearLayout.LayoutParams(boxWidth, boxHeight)
            layoutParams.bottomMargin = childVPadding
            layoutParams.topMargin = childVPadding
            layoutParams.leftMargin = childHPadding
            layoutParams.rightMargin = childHPadding
            layoutParams.gravity = Gravity.CENTER
            editText.setOnKeyListener(onKeyListener)
            setBg(editText, false)
            editText.setTextColor(Color.BLACK)
            editText.layoutParams = layoutParams
            editText.gravity = Gravity.CENTER
            editText.filters = arrayOf<InputFilter>(LengthFilter(1))
            if (TYPE_NUMBER == inputType) {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (TYPE_PASSWORD == inputType) {
                editText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else if (TYPE_TEXT == inputType) {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            } else if (TYPE_PHONE == inputType) {
                editText.inputType = InputType.TYPE_CLASS_PHONE
            }
            editText.id = i
            editText.setEms(1)
            editText.addTextChangedListener(textWatcher)
            editText.onFocusChangeListener = OnFocusChangeListener { _: View?, b: Boolean -> setBg(editText, b) }
            addView(editText, i)
        }
    }

    private fun backFocus() {
        val count = childCount
        var editText: EditText
        for (i in count - 1 downTo 0) {
            editText = getChildAt(i) as EditText
            if (editText.text.length == 1) {
                editText.requestFocus()
                editText.setSelection(1)
                return
            }
        }
    }

    private fun focus() {
        val count = childCount
        var editText: EditText
        for (i in 0 until count) {
            editText = getChildAt(i) as EditText
            if (editText.text.length < 1) {
                editText.requestFocus()
                return
            }
        }
    }

    private fun setBg(editText: EditText, focus: Boolean) {
        if (boxBgNormal != null && !focus) {
            editText.background = boxBgNormal
        } else if (boxBgFocus != null && focus) {
            editText.background = boxBgFocus
        }
    }

    private fun checkAndCommit() {
        val stringBuilder = StringBuilder()
        var full = true
        for (i in 0 until box) {
            val editText = getChildAt(i) as EditText
            val content = editText.text.toString()
            if (content.length == 0) {
                full = false
                break
            } else {
                stringBuilder.append(content)
            }
        }
        Log.d(TAG, "checkAndCommit:$stringBuilder")
        if (full) {
            if (listener != null) {
                listener!!.onComplete(stringBuilder.toString())
                isEnabled = false
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    fun setOnCompleteListener(listener: Listener?) {
        this.listener = listener
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LinearLayout.LayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var parentWidth = layoutParams.width
        if (parentWidth == LayoutParams.MATCH_PARENT) {
            parentWidth = screenWidth
        }
        Log.d(javaClass.name, "onMeasure width $parentWidth")
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        if (count > 0) {
            val child = getChildAt(0)
            val cWidth = child.measuredWidth
            if (parentWidth != LayoutParams.WRAP_CONTENT) {
                // 重新计算padding
                childHPadding = (parentWidth - cWidth * count) / (count + 1)
            }
            val cHeight = child.measuredHeight
            val maxH = cHeight + 2 * childVPadding
            val maxW = cWidth * count + childHPadding * (count + 1)
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec))
        }
    }

    private val screenWidth: Int
        get() {
            val resources = this.resources
            val dm = resources.displayMetrics
            return dm.widthPixels
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(javaClass.name, "onLayout width = $measuredWidth")
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.visibility = VISIBLE
            val cWidth = child.measuredWidth
            val cHeight = child.measuredHeight
            val cl = childHPadding + i * (cWidth + childHPadding)
            val cr = cl + cWidth
            val ct = childVPadding
            val cb = ct + cHeight
            child.layout(cl, ct, cr, cb)
        }
    }

    interface Listener {
        fun onComplete(content: String?)
    }

    companion object {
        private const val TYPE_NUMBER = "number"
        private const val TYPE_TEXT = "text"
        private const val TYPE_PASSWORD = "password"
        private const val TYPE_PHONE = "phone"
        private const val TAG = "CodeEditText"
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CodeEditText)
        box = a.getInt(R.styleable.CodeEditText_box, 6)
        childHPadding = a.getDimension(R.styleable.CodeEditText_child_h_padding, 0f).toInt()
        childVPadding = a.getDimension(R.styleable.CodeEditText_child_v_padding, 0f).toInt()
        boxBgFocus = a.getDrawable(R.styleable.CodeEditText_box_bg_focus)
        boxBgNormal = a.getDrawable(R.styleable.CodeEditText_box_bg_normal)
        inputType = a.getString(R.styleable.CodeEditText_inputType)
        boxWidth = a.getDimension(R.styleable.CodeEditText_child_width, boxWidth.toFloat()).toInt()
        boxHeight = a.getDimension(R.styleable.CodeEditText_child_height, boxHeight.toFloat()).toInt()
        initViews()
    }
}