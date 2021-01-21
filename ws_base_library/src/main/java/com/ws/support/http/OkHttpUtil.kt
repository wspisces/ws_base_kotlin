@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.ws.support.http

import android.annotation.SuppressLint
import com.orhanobut.logger.Logger
import com.ws.support.utils.SharePreferUtil
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

object OkHttpUtil {
    val trustAllCert: X509TrustManager = object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @kotlin.Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @kotlin.Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }
    private const val DEFAULT_TIMEOUT = 30

    /**
     * 拦截器,添加头部
     */
    fun genericClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS) //设置超时时间
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS) //添加日志拦截器
                .addInterceptor(LogInterceptor()
                        .setLevel(LogInterceptor.Level.BASIC))
                .addInterceptor { chain: Interceptor.Chain ->
                    val token = SharePreferUtil.getAccessToken()
                    //                    if (!token.isEmpty()) {
//                        token = "Bearer " + token;
//                    }
                    val request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "*/*")
                            .addHeader("Authorization", token)
                            .build()
                    chain.proceed(request)
                } //               .sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert), trustAllCert)
                .build()
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> Logger.e(throwable, "RxJavaError") }
        return httpClient
    }
}