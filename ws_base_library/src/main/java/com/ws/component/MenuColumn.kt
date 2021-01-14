package com.ws.component

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ws.base.R

/**
 * 主页菜单组件
 *
 * @author ws
 * 2020/6/19 15:31
 * 修改人：ws
 */
class MenuColumn : ConstraintLayout {
    var listener: OnMenuClickListener? = null
    private val onClickListener = OnClickListener { listener?.onClick() }

    private lateinit var mContext: Context
    private lateinit var mView: View
    private lateinit var mBtn: Button
    private lateinit var iv: ImageView
    private lateinit var tv: TextView

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @JvmName("setOnMenuClickListener")
    fun setListener(listener: OnMenuClickListener?) {
        this.listener = listener
    }

    fun getIv(): ImageView? {
        return iv
    }

    fun getTv(): TextView? {
        return tv
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.menu_column, this, true)
        iv = mView.findViewById(R.id.iv_menu)
        tv = mView.findViewById(R.id.tv_menu)
        mBtn = mView.findViewById(R.id.btn_menu)
        mBtn.setOnClickListener(onClickListener)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuColumn)
        tv.setText(a.getString(R.styleable.MenuColumn_title))
        val image = a.getResourceId(R.styleable.MenuColumn_icon, R.mipmap.save_icon)
        iv.setImageResource(image)
        a.recycle()
    }

    fun setEnable(enable: Boolean) {
        if (enable) {
            mBtn.setOnClickListener(onClickListener)
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_333))
            val tipsArrow = iv.drawable
            tipsArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.text_color_333), PorterDuff.Mode.SRC_ATOP)
            iv.setImageDrawable(tipsArrow)
        } else {
            mBtn.setOnClickListener(null)
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_999))
            val tipsArrow = iv.drawable
            tipsArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.text_color_999), PorterDuff.Mode.SRC_ATOP)
            iv.setImageDrawable(tipsArrow)
        }
    }

    fun setContent(imgResouce: Int, menu: String?) {
        iv.setImageResource(imgResouce)
        tv.text = menu
    }
}