package com.ws.component

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
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
import com.ws.base.R

/**
 * 表单组件-行头带图标
 *
 * @author ws
 * 2020/8/13 15:31
 * 修改人：ws
 */
class FormRowWithImage : ConstraintLayout {
    private lateinit var btnAction: Button
    private lateinit var ivUser: ImageView
    private lateinit var ivLeading: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var etContent: EditText
    private var listener: OnFormClickListener? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
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
        val mView = inflater.inflate(R.layout.form_row_image, this, true)
        ivUser = mView.findViewById(R.id.iv_user)
        ivLeading = mView.findViewById(R.id.iv_leading)
        val ivArrow = mView.findViewById<ImageView>(R.id.iv_arrow)
        tvTitle = mView.findViewById(R.id.tv_title)
        etContent = mView.findViewById(R.id.et_content)
        btnAction = mView.findViewById(R.id.btn_action)
        btnAction.setOnClickListener { view: View? ->
            listener?.onClick()
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.FormRowWithImage)
        tvTitle.setText(a.getString(R.styleable.FormRowWithImage_title))
        val hint = a.getString(R.styleable.FormRowWithImage_hint)
        val leadingImage = a.getResourceId(R.styleable.FormRowWithImage_leading, 0)
        ivLeading.setImageResource(leadingImage)
        etContent.setHint(hint)
        val type = a.getInt(R.styleable.FormRowWithImage_style, 0)
        ivUser.setVisibility(GONE)
        if (type == 0) { //默认输入
            val max = a.getInt(R.styleable.FormRowWithImage_maxlengh, -1) //最大输入字符
            val isPwd = a.getBoolean(R.styleable.FormRowWithImage_pwd, false)
            ivArrow.visibility = INVISIBLE
            etContent.setEnabled(true)
            if (max > 0) etContent.setFilters(arrayOf<InputFilter>(LengthFilter(max)))
            if (isPwd) {
                //etContent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                etContent.setTransformationMethod(PasswordTransformationMethod.getInstance())
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
}