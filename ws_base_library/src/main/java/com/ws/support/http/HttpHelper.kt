package com.ws.support.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ws.support.base.BaseApplication
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 封装http
 * @author  : curry
 * 2018/11/26
 */
object HttpHelper {
    /**
     * 初始化retrofit
     *
     */
    fun initRetrofit(baseUrl: String): Retrofit {
        val gson = GsonBuilder().setDateFormat("HH:mm:ss").serializeNulls().create()
        return Retrofit.Builder() //接口
                .baseUrl(baseUrl) //设置数据解析器--（需要添加相关依赖，例com.squareup.retrofit2:converter-gson）
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)) //结合RxJava需要，目的在于返回值定义为Observable（需要添加相关依赖，例com.squareup.retrofit2:adapter-rxjava）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpUtil.genericClient())
                .build()
    }

    /**
     * 创建
     */
    fun <B> createService(service: Class<B>): B {
        return BaseApplication.retrofit.create(service)
    }

    /**
     * 订阅（绑定两者）
     * 被观察者Observable（发送）
     * 观察者Observer（接收）
     */
    fun <T> subscribe(observable: Observable<T>, observer: Observer<T>) {
        observable
                .subscribeOn(Schedulers.io()) //发送 在子线程
                .observeOn(AndroidSchedulers.mainThread()) //接受 在主线程
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    fun createRquestBody(params: Map<*, *>?): RequestBody {
        // JsonUtils.toJson(params)
        return Gson().toJson(params)
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}

