package com.ws.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ws.base.R

/**
 * 侧滑菜单组件
 *
 * @author ws
 * 2020/6/19 15:31
 * 修改人：ws
 */
class MenuRow : ConstraintLayout {
    private lateinit var iv: ImageView
    fun getIv(): ImageView {
        return iv
    }

    fun getTv(): TextView {
        return tv
    }

    private lateinit var tv: TextView

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = inflater.inflate(R.layout.menu_row, this, true)
        iv = mView.findViewById(R.id.iv_menu)
        tv = mView.findViewById(R.id.tv_menu)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuRow)
        tv.setText(a.getString(R.styleable.MenuRow_title))
        val image = a.getResourceId(R.styleable.MenuRow_icon, R.mipmap.save_icon)
        iv.setImageResource(image)
        a.recycle()
    }

    fun setContent(imgResouce: Int, menu: String?) {
        iv.setImageResource(imgResouce)
        tv.text = menu
    }
}