package com.ws.component

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ws.base.R

/**
 * 表单组件-横向排布
 *
 * @author ws
 * 2020/6/19 15:31
 * 修改人：ws
 */
@Suppress("DEPRECATION")
class AgreementRow : ConstraintLayout {

    private lateinit var tv: TextView
    private lateinit var cb: CheckBox
    private var listener: OnAgreementRowClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun getTextView(): TextView {
        return tv
    }

    fun setContent(s: String) {
        tv.text = Html.fromHtml(s)
    }

    fun setListener(listener: OnAgreementRowClickListener?) {
        this.listener = listener

    }

    fun setChecked(checked: Boolean) {
        cb.isChecked = checked
    }

    fun isChecked(): Boolean {
        return cb.isChecked
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = inflater.inflate(R.layout.agreement_row, this, true)
        tv = mView.findViewById(R.id.tv_content)
        cb = mView.findViewById(R.id.cb)
        val a = context.obtainStyledAttributes(attrs, R.styleable.AgreementRow)
        val content = a.getString(R.styleable.AgreementRow_content)
        tv.text = Html.fromHtml(content)
        val isChecked = a.getBoolean(R.styleable.AgreementRow_checked, true)
        cb.isChecked = isChecked
        bidnglistener?.onChange()
        a.recycle()
        cb.setOnCheckedChangeListener { _, b ->
            listener?.onCheckChanged(b)
            bidnglistener?.onChange()
        }
        tv.setOnClickListener { listener?.onAgreementClick() }
    }

    var bidnglistener: InverseBindingListener? = null
}

object AgreementRowBindingAdapters {

    @BindingAdapter(value = ["checked"], requireAll = false)
    @JvmStatic
    fun isChecked(view: AgreementRow, value: Boolean) {
        view.setChecked(value)
    }

    @InverseBindingAdapter(attribute = "checked", event = "checkedAttrChanged")
    @JvmStatic
    fun isChecked(view: AgreementRow): Boolean {
        return view.isChecked()
    }

    @BindingAdapter(value = ["checkedAttrChanged"], requireAll = false)
    @JvmStatic
    fun setListener(view: AgreementRow, listener: InverseBindingListener?) {
        view.bidnglistener = listener
    }
}