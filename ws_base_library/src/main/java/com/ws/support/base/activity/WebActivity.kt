package com.ws.support.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ws.base.R
import com.ws.base.databinding.ActivityBaseBinding
import com.ws.base.databinding.ActivityWebBinding
import com.ws.support.utils.NetUtils
import com.ws.support.utils.StringUtils

/**
 * web界面
 */
class WebActivity private constructor(override val toolbarTite: String? = "网页", override val layoutId: Int = R.layout.activity_web) : BaseViewBindActivity() {

    lateinit var binding: ActivityWebBinding
    constructor() : this("网页",R.layout.activity_web) {}

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(bindView: ActivityBaseBinding) {
        binding = ActivityWebBinding.inflate(layoutInflater, bindView.content, true)
        //声明WebSettings子类
        val webSettings = binding.wv.settings
        if (NetUtils.isConnected(applicationContext)) {
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT //根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //没网，则从本地获取，即离线加载
        }
        webSettings.domStorageEnabled = true // 开启 DOM storage API 功能
        webSettings.databaseEnabled = true //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true) //开启 Application Caches 功能

        val cacheDirPath = filesDir.absolutePath + "/cache"
        webSettings.setAppCachePath(cacheDirPath) //设置  Application Caches 缓存目录

        //优先使用缓存:
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //不使用缓存:

        //优先使用缓存:
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //不使用缓存:
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        //如果访问的页面中要与Javascript交互，则webview必须设置开启支持Javascript
        webSettings.javaScriptEnabled = true
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = false //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = true //隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = false //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = false //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        binding.wv.webViewClient = object :WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

        }

        /*binding.wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                //Logger.e(url);
                //initProgress("");
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //hideProgress();
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Logger.e("$description $failingUrl")
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        binding.wv.onResume()
        binding.wv.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        binding.wv.onPause()
        binding.wv.pauseTimers()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.wv.destroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var title = intent.getStringExtra("title")
        var url = intent.getStringExtra("url")
        if (StringUtils.isEmptyWithNull(title)) {
            title = "为传入标题"
        }
        if (StringUtils.isEmptyWithNull(url)) {
            url = "https://www.baidu.com"
        }

        baseBinding.title.text = title
        binding.wv.loadUrl(url!!)
    }


    companion object {
        fun startActivity(content: Context, title: String?, url: String?) {
            content.startActivity(Intent(content,WebActivity().javaClass).putExtra("title",title)
                    .putExtra("url",url))
        }
    }
}