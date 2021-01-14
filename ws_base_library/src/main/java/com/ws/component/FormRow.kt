package com.ws.component

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.orhanobut.logger.Logger
import com.ws.base.R

/**
 * 表单组件-横向排布
 *
 * @author ws
 * 2020/6/19 15:31
 * 修改人：ws
 */
class FormRow : ConstraintLayout {
    var inputType = 0
    private lateinit var btnAction: Button
    private lateinit var ivUser: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var etContent: EditText
    private var listener: OnFormClickListener? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun getTitle(): TextView {
        return tvTitle
    }

    fun setTitle(s: String?) {
        tvTitle.text = s
    }

    fun getUserImage(): ImageView? {
        return ivUser
    }

    fun getEt(): EditText? {
        return etContent
    }

    fun setListener(listener: OnFormClickListener?) {
        this.listener = listener
        btnAction.visibility = VISIBLE
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = inflater.inflate(R.layout.form_row, this, true)
        ivUser = mView.findViewById(R.id.iv_user)
        val ivArrow = mView.findViewById<ImageView>(R.id.iv_arrow)
        tvTitle = mView.findViewById(R.id.tv_title)
        etContent = mView.findViewById(R.id.et_content)
        btnAction = mView.findViewById(R.id.btn_action)
        btnAction.setOnClickListener { view: View? ->
            listener?.onClick()
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.FormRow)
        tvTitle.setText(a.getString(R.styleable.FormRow_title))
        val hint = a.getString(R.styleable.FormRow_hint)
        etContent.setHint(hint)
        val type = a.getInt(R.styleable.FormRow_style, 0)
        ivUser.setVisibility(GONE)
        if (type == 0) { //默认输入
            val max = a.getInt(R.styleable.FormRow_maxlengh, -1) //最大输入字符
            val isPwd = a.getBoolean(R.styleable.FormRow_pwd, false)
            ivArrow.visibility = INVISIBLE
            etContent.setEnabled(true)
            if (max > 0) etContent.setFilters(arrayOf<InputFilter>(LengthFilter(max)))
            if (isPwd) {
                etContent.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
            inputType = a.getInt(R.styleable.FormRow_input, 0)
            //默认0,问正常文字输入
            if (inputType == 1) { //整数数字
                etContent.setInputType(InputType.TYPE_CLASS_NUMBER)
            } else if (inputType == 2) { //手机号
                etContent.setInputType(InputType.TYPE_CLASS_PHONE)
            } else if (inputType == 3) { //邮箱地址
                etContent.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            }
        } else if (type == 1) { //头像选择
            ivUser.setVisibility(VISIBLE)
            etContent.setEnabled(false)
        } else if (type == 2) { //弹窗
            etContent.setEnabled(false)
        } else if (type == 3) { //信息展示
            etContent.setEnabled(false)
            ivArrow.visibility = INVISIBLE
        }
        a.recycle()
    }

    fun getContent(): String {
        return etContent.text.toString()
    }

    //设置内容
    fun setContent(content: String?) {
        etContent.setText(content)
    }

    //提示文字
    fun setHintText(hint: String?) {
        etContent.hint = hint
    }

    //可输入的最大长度
    fun setMaxLength(i: Int) {
        if (i > 0) etContent.filters = arrayOf<InputFilter>(LengthFilter(i))
    }

    //数字输入是限制输入的最大值
    fun setMax(max: Int) {
        if (inputType != 1) {
            Logger.e("输入类型不为数字,不能做最大值判断")
            return
        }
        etContent.filters = arrayOf(InputFilterFloatMinMax(0, max))
    }

    //数字输入是限制输入的范围
    fun setMax(max: Int, min: Int) {
        if (inputType != 1) {
            Logger.e("输入类型不为数字,不能做最大值判断")
            return
        }
        etContent.filters = arrayOf(InputFilterFloatMinMax(min, max))
    }
}