package com.ws.support.extension


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


/**
 * Activity扩展
 * @author ws
 * @date 1/21/21 10:16 AM
 * 修改人：ws
 */

inline fun <reified T> AppCompatActivity.startActivity(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivity(intent)
}
