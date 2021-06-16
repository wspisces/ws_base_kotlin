package com.ws.component

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ws.base.R

/**
 * 表单组件-纵向排布
 *
 * @author ws
 * 2020/6/19 15:31
 * 修改人：ws
 */
class FormColum : ConstraintLayout {
    private lateinit var btnAction: Button
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

    fun getEt(): EditText? {
        return etContent
    }

    fun setListener(listener: OnFormClickListener?) {
        this.listener = listener
        btnAction.visibility = VISIBLE
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = inflater.inflate(R.layout.form_colum, this, true)
        val ivArrow = mView.findViewById<ImageView>(R.id.iv_arrow)
        tvTitle = mView.findViewById(R.id.tv_title)
        etContent = mView.findViewById(R.id.et_content)
        btnAction = mView.findViewById(R.id.btn_action)
        btnAction.setOnClickListener {
            if (listener != null) {
                listener!!.onClick()
            }
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.FormColum)
        tvTitle.setText(a.getString(R.styleable.FormColum_title))
        val hint = a.getString(R.styleable.FormColum_hint)
        val content = a.getString(R.styleable.FormColum_content)
        etContent.setHint(hint)
        etContent.setText(content)
        val type = a.getInt(R.styleable.FormColum_style, 0)
        btnAction.setVisibility(GONE)
        if (type == 0) { //默认输入
            val max = a.getInt(R.styleable.FormColum_maxlengh, -1) //最大输入字符
            //boolean isPwd = a.getBoolean(R.styleable.FormRow_pwd, false);
            ivArrow.visibility = INVISIBLE
            etContent.setEnabled(true)
            if (max > 0) etContent.setFilters(arrayOf<InputFilter>(LengthFilter(max)))
            //if (isPwd) {
            //    etContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //}
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