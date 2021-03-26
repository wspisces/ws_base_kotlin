package com.ws.start.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ws.start.http.StartApi
import com.ws.support.base.mvvm.repository.BaseRepository
import com.ws.support.base.mvvm.viewmodel.BaseViewModel
import com.ws.support.extension.LoadState
import com.ws.support.http_coroutines.RetrofitFactory
import com.ws.support.http_coroutines.createRquestBody
import com.ws.support.utils.StringUtils
import kotlinx.coroutines.launch


/**
 * 描述信息
 * @author ws
 * @date 3/24/21 1:08 PM
 * 修改人：ws
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginModel : BaseViewModel<BaseRepository>() {

    val isRememberAccount: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = true }
    }

    val isAutoLogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }


    val isAgreement: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = true }
    }

//    val token: MutableLiveData<String> by lazy {
//        MutableLiveData<String>().apply { value = "" }
//    }

    var account = ""
        set(value) {
            field = value
            _account.postValue(value)
        }
    var pwd = ""
        set(value) {
            field = value
            _pwd.postValue(value)
        }

    private val _account: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { value = "" }
    }

    private val _pwd: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { value = "" }
    }

    fun login() {
        if (StringUtils.isEmptyWithNull(account)) {
            error.postValue("请输入账号")
            return
        }
        if (StringUtils.isEmptyWithNull(pwd)) {
            error.postValue("请输入密码")
            return
        }
        if (!isAgreement.value!!) {
            error.postValue("你需要先同意隐私协议")
            return
        }
        val params = mutableMapOf<String, Any>()
        params["username"] = account
        params["password"] = pwd
        params["code"] = ""
        params["uuid"] = ""
        loadState.postValue(LoadState.Loading("正在登录"))
        viewModelScope.launch {//自动解绑
            //isLoading.postValue(true)
            try {
                /*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*/
                val data = RetrofitFactory.instance.getService(StartApi::class.java)
                        .login(createRquestBody(params)).tokenConvert()
                /*给LiveData赋值  ui会自动更新*/
                token.value = data
                loadState.postValue(LoadState.Success("登录成功"))
            } catch (e: Exception) {
                /*请求异常的话在这里处理*/
                e.printStackTrace()
                loadState.postValue(LoadState.Fail(e.localizedMessage))
                Log.i("请求失败", "${e.message}")
            }
            //isLoading.postValue(false)
        }
    }

    var token = MutableLiveData<String>()

/*    //demo
    var fictions = MutableLiveData<List<Fiction>>()
    fun getFictions() {
        *//*viewModelScope是一个绑定到当前viewModel的作用域  当ViewModel被清除时会自动取消该作用域，所以不用担心内存泄漏为问题*//*
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                *//*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*//*
                val data = RetrofitFactory.instance.getService(StartApi::class.java)
                        .getFictions().dataConvert()
                *//*给LiveData赋值  ui会自动更新*//*
                fictions.value = data
            } catch (e: Exception) {
                *//*请求异常的话在这里处理*//*
                e.printStackTrace()
                Log.i("请求失败", "${e.message}")
            }
            isLoading.postValue(false)
        }
    }*/
}