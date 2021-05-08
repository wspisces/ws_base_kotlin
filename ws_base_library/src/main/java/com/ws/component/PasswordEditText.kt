package com.ws.component

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ws.base.R


/**
 * 描述信息
 * @author ws
 * @date 3/25/21 7:09 PM
 * 修改人：ws
 */
//https://blog.csdn.net/BFELFISH/article/details/103306706
class PasswordEditText : ConstraintLayout {

    lateinit var ivLeading: ImageView
    lateinit var et: EditText
    lateinit var ibtnToggle: ImageButton
    lateinit var ibtnClear: ImageButton

    //true为隐藏，false为显示
    private var mode = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    //设置提示文字
    fun setHint(hint: String) {
        et.hint = hint
    }

    //获得输入框文字
    fun getText(): String {
        return et.text.toString()
    }

    @JvmName("getEt1")
    fun getEt(): EditText {
        return et
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = inflater.inflate(R.layout.layout_input_pwd, this, true)
        ivLeading = mView.findViewById(R.id.iv_leading)
        et = mView.findViewById(R.id.et)
        ibtnToggle = mView.findViewById(R.id.ibtn_toggle)
        ibtnClear = mView.findViewById(R.id.ibtn_clear)
        ibtnClear.setOnClickListener {
            et.setText("")
            bidnglistener?.onChange()
        }
        ibtnToggle.setOnClickListener {
            if (mode) {//从显示变隐藏
                //et.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                et.transformationMethod = PasswordTransformationMethod.getInstance()
                ibtnToggle.setImageResource(R.drawable.ic_eye_close)
            } else {//从隐藏变显示
                et.inputType = InputType.TYPE_CLASS_TEXT
                et.transformationMethod = null
                ibtnToggle.setImageResource(R.drawable.ic_eye_open)
            }
            //为了点击之后输入框光标不变
            et.setSelection(et.text.length)
            mode = !mode
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)

        val max = a.getInt(R.styleable.PasswordEditText_maxlengh, -1) //最大输入字符
        if (max > 0) et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max))
        val imeAction = a.getInt(R.styleable.PasswordEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        et.imeOptions = imeAction

        val hint = a.getString(R.styleable.PasswordEditText_hint)
        et.hint = hint

        val txt = a.getString(R.styleable.PasswordEditText_pwdstr)
        et.setText(txt)
        bidnglistener?.onChange()
        val leadingImage = a.getResourceId(R.styleable.PasswordEditText_leading, R.drawable.ic_account)
        ivLeading.setImageResource(leadingImage)

        val style= a.getInt(R.styleable.PasswordEditText_style,0)
        //默认0,密码输入
        if (style != 0){
            ibtnToggle.visibility = INVISIBLE
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.transformationMethod = null
        }


        a.recycle()
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                bidnglistener?.onChange()
            }

        })
    }

    fun getContent(): String {
        return et.text.toString()
    }

    //设置内容
    fun setContent(content: String?) {
        et.setText(content)
        bidnglistener?.onChange()
    }
    var bidnglistener: InverseBindingListener? = null
}

object PasswordEditTextBindingAdapters {

    @BindingAdapter(value = ["pwdstr"], requireAll = false)
    @JvmStatic
    fun setPwdstr(view: PasswordEditText, value: String) {
        view.setContent(value)
    }

    @InverseBindingAdapter(attribute = "pwdstr", event = "pwdstrAttrChanged")
    @JvmStatic
    fun getPwdstr(view: PasswordEditText): String {
        val text = view.getEt().text
        return text.toString()
    }

    @BindingAdapter(value = ["pwdstrAttrChanged"], requireAll = false)
    @JvmStatic
    fun setListener(view: PasswordEditText, listener: InverseBindingListener?) {
        view.bidnglistener = listener
    }
}