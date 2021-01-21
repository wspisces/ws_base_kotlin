package com.ws.support.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * BaseFragmentActivity.class
 * 基础页面
 * time:2018/4/9
 */
internal open class BaseFragmentActivity : AppCompatActivity() {
    private var savedInstanceState: Bundle? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

    }
}