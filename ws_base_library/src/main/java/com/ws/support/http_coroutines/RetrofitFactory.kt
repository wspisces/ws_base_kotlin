package com.ws.support.http_coroutines

import android.util.Log
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.ws.base.BuildConfig
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.MediaType

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * @description: RetrofitFactory
 * @author : yzq
 * @date   : 2018/7/9
 * @time   : 16:44
 *
 */
//https://blog.csdn.net/yuzhiqiang_1993/article/details/101012090
//https://github.com/yuzhiqiang1993/coroutineretofitmvvm
class RetrofitFactory private constructor() {

    private val retrofit: Retrofit


    init {

        val gson = Gson().newBuilder()
            .setLenient()
            .serializeNulls()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .client(initOkhttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }


    companion object {
        val instance: RetrofitFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitFactory()
        }

    }


    private fun initOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(initLogInterceptor())
            .build()
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> Logger.e(throwable, "RxJavaError") }
        trustAllHosts()
        return okHttpClient
    }


    /*
    * 日志拦截器
    * */
    private fun initLogInterceptor(): HttpLoggingInterceptor {

        val interceptor = HttpLoggingInterceptor { message -> Log.i("interceptor", message) }

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    private fun trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.i("trustAllHosts", "checkClientTrusted")
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.i("trustAllHosts", "checkServerTrusted")
            }
        })
        // Install the all-trusting trust manager
        try {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    /*
    * 具体服务实例化
    * */
    fun <T> getService(service: Class<T>): T {

        return retrofit.create(service)
    }

    fun createRquestBody(params: Map<*, *>): RequestBody {
        // JsonUtils.toJson(params)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(params))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RetrofitFactory

        if (retrofit != other.retrofit) return false

        return true
    }

    override fun hashCode(): Int {
        return retrofit.hashCode()
    }
}



