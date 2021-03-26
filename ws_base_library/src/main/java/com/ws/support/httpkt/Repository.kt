package com.ws.support.httpkt


import android.content.Context

/**
 * 接口资源
 * @author ssq
 */
object Repository {

    /**
     * 预处理数据(错误)
     * @param context 跳至登录页的上下文
     */
    private suspend fun <T : BaseBean> preprocessData(baseBean: T): T =
            if (baseBean.errorCode == 0) {// 成功
                // 返回数据
                baseBean
            } else {// 失败
                // 验证登录是否过期
//        validateCode(context, baseBean.code)
                // 抛出接口异常
                throw ApiException(baseBean.errorCode, baseBean.errorMsg)
            }

    /**
     * 获取我的信息（主页——我的页面）
     *
     * @param context 跳至登录页的上下文
     */
    suspend fun getWXArticle(context: Context? = null): ArticleData =
            NetworkService.api.getWXArticle().let {
                preprocessData(it)
            }
}
/**
 * 接口异常，即接口返回的code != 0
 *
 * 原因：1，后台接口有问题；或者，2，请求参数有问题。
 * @param errorCode 接口返回的code(非0)
 * @param msg 错误提示信息
 */
class ApiException(val errorCode: Int,val msg: String): Throwable(msg)
/**
 * 公众号
 * @author ssq
 */
class ArticleData: BaseBean() {
    //    data: [{children: [], courseId: 13, id: 408, name: "鸿洋", order: 190000, parentChapterId: 407,…},…]
//    errorCode: 0
//    errorMsg: ""
    var data = arrayListOf<Chapters>()
}

class Chapters{
    //    children: []
//    courseId: 13
//    id: 408
//    name: "鸿洋"
//    order: 190000
//    parentChapterId: 407
//    userControlSetTop: false
//    visible: 1
    var courseId = ""
    var id = ""
    var name =  ""
    var order = 0
}
/**
 * Json对象基类
 */
open class BaseBean {
    var errorCode = -1
    var errorMsg = ""
}
