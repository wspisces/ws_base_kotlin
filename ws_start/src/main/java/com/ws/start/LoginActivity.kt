package com.ws.start

import android.os.Bundle
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.ws.base.databinding.ActivityDataBaseBinding
import com.ws.component.OnAgreementRowClickListener
import com.ws.start.databinding.ActivityLoginBinding
import com.ws.start.viewmodel.LoginModel
import com.ws.support.base.activity.WebActivity
import com.ws.support.extension.LoadState
import com.ws.support.utils.ToastUtils
import com.ws.support.base.activity.BaseViewDataBindActivity as ActivityBaseViewDataBindActivity

/**
 * 登录
 */
class LoginActivity(override val toolbarTite: String? = "登录", override val layoutId: Int = R.layout.activity_login) : ActivityBaseViewDataBindActivity<ActivityLoginBinding>() {

    lateinit var binding: ActivityLoginBinding
    val loginModel: LoginModel by viewModels()
    override fun initView(bindView: ActivityDataBaseBinding, binding: ActivityLoginBinding?) {
        this.binding = binding!!
        this.binding.agreement.setListener(onAgreementRowClickListener)
        this.binding.agreement.setContent("选中即表示同意<font color=\"blue\">隐私协议</font>")
    }

    private var onAgreementRowClickListener = object : OnAgreementRowClickListener {
        override fun onAgreementClick() {
            ToastUtils.debug("点击了文字")
            WebActivity.startActivity(mContext, "协议", "https://www.baidu.com")
        }

        override fun onCheckChanged(isChecked: Boolean) {
            ToastUtils.debug("点击了checkbox=$isChecked")
        }
    }

    override fun hideToolbar(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.model = loginModel
        loginModel.error.observe(this, {
            ToastUtils.error(it)
        })
        loginModel.loadState.observe(this, {
            when (it) {
                is LoadState.Loading -> {
                    initProgress(it.msg, delayTime = 10)
                }
                is LoadState.Success -> {
                    ToastUtils.success(it.msg)
                    hideProgress()
                }
                is LoadState.Fail -> {
                    ToastUtils.error(it.msg)
                    hideProgress()
                }
            }
        })

        /*数据发生变化时更新ui*/
        loginModel.token.observe(this, Observer {
            Logger.i(it)
        })

        binding.btnLogin.setOnClickListener {
            loginModel.login()
        }
        binding.formPwd.getEt().setOnEditorActionListener { _, i, _ ->
            if (IME_ACTION_DONE == i) {
                loginModel.login()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }
    }
}