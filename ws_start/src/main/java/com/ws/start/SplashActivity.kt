package com.ws.start

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import com.ws.base.databinding.ActivityBaseBinding
import com.ws.start.databinding.ActivitySplashBinding
import com.ws.support.base.activity.BaseViewBindActivity
import com.ws.support.utils.ToastUtils

class SplashActivity(override val toolbarTite: String? = "闪屏页", override val layoutId: Int = R.layout.activity_splash) : BaseViewBindActivity() {

    lateinit var binding: ActivitySplashBinding
    lateinit var countDownTimer: CountDownTimer
    override fun initView(bindView: ActivityBaseBinding) {
        binding = ActivitySplashBinding.inflate(layoutInflater, bindView.content, true)
        binding.tv.setOnClickListener {
            ToastUtils.debug("点击了跳过按钮")
            countDownTimer.cancel()
            jumptoActivity(LoginActivity().javaClass)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countDownTimer = object : CountDownTimer(10 * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (!this@SplashActivity.isFinishing) {
                    binding.tv.text = "${millisUntilFinished / 1000}s 跳过"
                }
            }

            /**
             * 倒计时结束后调用的
             */
            override fun onFinish() {
                ToastUtils.debug("计时结束")
                jumptoActivity(LoginActivity().javaClass)
            }
        }
        countDownTimer.start()
    }

    override fun hideToolbar(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}