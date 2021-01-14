package com.ws.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import com.ws.base.R

/**
 * 描述信息
 *
 * @author ws
 * @date 12/18/20 1:14 PM
 * 修改人：ws
 */
class ClearEditText @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.editTextStyle)
    : AppCompatEditText(context!!, attrs, defStyle), OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null
    private var isHideDeleteBtn = false

    /**
     * 控件是否有焦点
     */
    private var hasFoucs = false
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        if (mClearDrawable == null) {
            mClearDrawable = resources.getDrawable(R.drawable.ic_clear, null)
        }
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
        mClearDrawable!!.setBounds(0, 0, mClearDrawable!!.intrinsicWidth, mClearDrawable!!.intrinsicHeight)
        isLongClickable = false
        setTextIsSelectable(false)
        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    /**
     * 由于不能直接给EditText设置点击事件，所以用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置在 EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val touchable = (event.x > width - totalPaddingRight && event.x < width - paddingRight
                        && event.y > paddingRight && event.y < height - paddingRight)
                if (touchable) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        hasFoucs = hasFocus
        if (hasFocus) {
            setClearIconVisible(text!!.length > 0)
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    fun setClearIconVisible(visible: Boolean) {
        if (isHideDeleteBtn) {
            return
        }
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1], right, compoundDrawables[3])
    }

    fun setHideDeleteBtn(isHide: Boolean) {
        isHideDeleteBtn = isHide
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (null == text) {
            return
        }
        if (hasFoucs) {
            setClearIconVisible(text!!.length > 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}
    override fun onTextContextMenuItem(id: Int): Boolean {
        return true
    }

    init {
        init()
    }
}