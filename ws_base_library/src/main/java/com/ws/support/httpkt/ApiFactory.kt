package com.ws.support.httpkt


/**
 * 描述信息
 * @author ws
 * @date 1/22/21 9:08 AM
 * 修改人：ws
 */
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.logger.Logger
import com.ws.base.BuildConfig
import com.ws.support.http.LogInterceptor
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * 接口请求工厂
 * @author ssq
 */
object ApiFactory {
    // 日志拦截器
    private val mLoggingInterceptor: Interceptor by lazy { LogInterceptor() }
    // OkHttpClient客户端
    private val mClient: OkHttpClient by lazy { newClient() }

    /**
     * 创建API Service接口实例
     */
    fun <T> createService(baseUrl: String, clazz: Class<T>): T =
            Retrofit.Builder().baseUrl(baseUrl).client(mClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(clazz)

    /**
     * OkHttpClient客户端
     */
    private fun newClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)// 连接时间：30s超时
        readTimeout(10, TimeUnit.SECONDS)// 读取时间：10s超时
        writeTimeout(10, TimeUnit.SECONDS)// 写入时间：10s超时
        if (BuildConfig.DEBUG) addInterceptor(mLoggingInterceptor)// 仅debug模式启用日志过滤器
    }.build()

    /**
     * 日志拦截器
     */
/*    private class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = StringBuilder()
            val startTime = System.nanoTime()
            val response: Response = with(chain.request()) {
                builder.append(method + "\n")
                builder.append("Sending request\n$url")
                if (method == "POST") {
                    builder.append("?")
                    when (val body = body) {
                        is FormBody -> {
                            for (j in 0 until body.size) {
                                builder.append(body.name(j) + "=" + body.value(j))
                                if (j != body.size - 1) {
                                    builder.append("&")
                                }
                            }
                        }
                    }
                }
                builder.append("\n").append(headers)
                Logger.v(builder.toString())
                chain.proceed(this)
            }
            builder.clear()
            builder.append("Received response in " + (System.nanoTime() - startTime) / 1e6 + "ms\n")
            builder.append("code" + response.code + "\n")
            Logger.v(builder.toString())
            return response
        }
    }*/
}


