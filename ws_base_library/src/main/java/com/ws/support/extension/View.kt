package com.ws.support.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar


/**
 * View的扩展
 * @author ws
 * @date 1/19/21 3:28 PM
 * 修改人：ws
 */
fun View.showSnakeBar(text: String, actionText: String? = null, duration: Int = Snackbar.LENGTH_SHORT, block: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, text, duration)
    if (actionText != null && block != null) {
        snackbar.setAction(actionText) {
            block()
        }
    }
    snackbar.show()
}

fun View.showSnakeBar(text: Int, actionText: String? = null, duration: Int = Snackbar.LENGTH_SHORT, block: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, text, duration)
    if (actionText != null && block != null) {
        snackbar.setAction(actionText) {
            block()
        }
    }
    snackbar.show()
}

//内联函数相当于把带有inline声明的方法体代码粘到调用处了
inline fun View.setOnSingleClickListener(crossinline onClick: () -> Unit, delayMillis: Long) {
    this.setOnClickListener {
        this.isClickable = false
        onClick()
        this.postDelayed({
            this.isClickable = true
        }, delayMillis)
    }
}