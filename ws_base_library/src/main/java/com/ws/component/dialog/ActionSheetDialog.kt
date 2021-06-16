package com.ws.component.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import com.ws.base.R
import com.ws.support.utils.ScreenUtils.Companion.getScreenHeight
import com.ws.support.utils.ScreenUtils.Companion.getScreenWidth
import java.util.*

/**
 * 描述信息
 *
 * @author ws
 * @date 12/21/20 7:11 PM
 * 修改人：ws
 */
class ActionSheetDialog(private val context: Context) {
    private lateinit var dialog: Dialog
    private lateinit var lLayout_content: LinearLayout
    private lateinit var sLayout_content: ScrollView
    private lateinit var sheetItemList: MutableList<SheetItem>
    fun builder(): ActionSheetDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
                R.layout.dialog_actionsheet, null)
        // 设置Dialog最小宽度为屏幕宽度
        //view.setMinimumWidth(ScreenUtils.getScreenWidth(context));
        // 获取自定义Dialog布局中的控件
        sLayout_content = view.findViewById(R.id.sv)
        lLayout_content = view
                .findViewById(R.id.ll)
        val txt_cancel = view.findViewById<Button>(R.id.btn_cancel)
        txt_cancel.setOnClickListener { dialog.dismiss() }

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog.setContentView(view)
        val dialogWindow = dialog.window
        dialogWindow!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        val lp = dialogWindow.attributes
        lp.x = 0
        lp.y = 0
        lp.width = getScreenWidth(context)
        sheetItemList = ArrayList()
        dialogWindow.attributes = lp
        return this
    }

    fun setCancelable(cancel: Boolean): ActionSheetDialog {
        dialog.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): ActionSheetDialog {
        dialog.setCanceledOnTouchOutside(cancel)
        return this
    }

    /**
     * @param strItem  条目名称
     * @param listener
     * @return
     */
    fun addSheetItem(
            strItem: String,
            listener: OnSheetItemClickListener,
    ): ActionSheetDialog {

        sheetItemList.add(SheetItem(strItem, listener))
        return this
    }

    /**
     * 设置条目布局
     */
    private fun setSheetItems() {
        if (sheetItemList.size <= 0) {
            return
        }
        val size = sheetItemList.size

        // 添加条目过多的时候控制高度
        if (size >= 7) {
            val params = sLayout_content.layoutParams as FrameLayout.LayoutParams
            params.height = getScreenHeight(context) / 2
            sLayout_content.layoutParams = params
        }

        // 循环添加条目
        for (i in 1..size) {
            val sheetItem = sheetItemList[i - 1]
            val strItem = sheetItem.name
            //SheetItemColor color = sheetItem.color;
            val listener = sheetItem.itemClickListener
            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 21f
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.btn_transparent)
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_color_333))
            // 字体颜色
            /* if (color == null)
            {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else
            {
                textView.setTextColor(Color.parseColor(color.getName()));
            }*/

            // 高度
            val scale = context.resources.displayMetrics.density
            val height = (55 * scale + 0.5f).toInt()
            textView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, height)

            // 点击事件
            textView.setOnClickListener {
                listener.onClick(i)
                dialog.dismiss()
            }
            lLayout_content.addView(textView)
        }
    }

    fun show() {
        setSheetItems()
        dialog.show()
    }

    /*    public enum SheetItemColor
    {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        SheetItemColor(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }*/
    interface OnSheetItemClickListener {
        fun onClick(which: Int)
    }

    inner class SheetItem//this.color = color;     //SheetItemColor           color;
    (
            var name: String,
            var itemClickListener: OnSheetItemClickListener,
    )
}