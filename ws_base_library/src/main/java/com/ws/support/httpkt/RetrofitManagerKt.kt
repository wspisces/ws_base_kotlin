package com.ws.support.httpkt


/**
 * 描述信息
 * @author ws
 * @date 1/22/21 9:07 AM
 * 修改人：ws
 */
import android.webkit.MimeTypeMap
import com.ws.base.BuildConfig
import com.ws.support.http._ApiUrl
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder
import java.util.*

/**
 * Retrofit 管理类
 * @author ssq
 */
object RetrofitManagerKt {
    // 接口API服务
    val apiService by lazy { ApiFactory.createService(BuildConfig.baseUrl, _ApiUrl::class.java) }

    /**
     * 执行网络请求（结合kotlin 协程使用）
     * @param deferred 请求的接口
     * @param isValidateCode 是否验证code，如：登录是否过期
     * @param context 为null时，登录过期不跳登录页
     */
/*    suspend fun <T : ResultTO> request(deferred: Deferred<Response<T>>, isValidateCode: Boolean = false, context: Context? = null): T? = withContext(Dispatchers.Default) {
        try {
            val response = deferred.await()
            if (response.isSuccessful) {// 成功
                val body = response.body()
                if (isValidateCode && body != null) {
                    validateCode(context, body.code)
                }
                body ?: throw NullBodyException()
            } else {// 处理Http异常
                ExceptionUtil.catchHttpException(response.code())
                null
            }
        } catch (e: Exception) {
            // 这里统一处理错误
            ExceptionUtil.catchException(e)
            null
        }
    }*/

    /**
     * 下载文件
     */
/*    suspend fun downloadFile(fileUrl: String): ResponseBody? = withContext(Dispatchers.IO) {
        try {
            apiService.downloadFileAsync(fileUrl).await()
        } catch (e: Exception) {
            // 这里统一处理错误
            ExceptionUtil.catchException(e)
            null
        }
    }*/

    /**
     * 上传图片文件
     * @param file 图片文件
     * @param type 用途类型
     */
/*    suspend fun uploadImage(file: File, type: Int): UploadImgEntity? =
            request(apiService.uploadImgAsync(imgPath = getUploadImgBodyMap(file), map = getUploadImgMap(type)))*/

    /**
     * 生成上传图片请求的文件参数
     * @param file 上传文件
     */
    fun getUploadImgBodyMap(file: File): HashMap<String, RequestBody> {
        val requestBodyMap = hashMapOf<String, RequestBody>()
        val mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                        MimeTypeMap.getFileExtensionFromUrl(file.path)) ?: "image/jpeg"
        val fileBody = RequestBody.create(MediaType.parse(mimeType), file)
        // （注意：okhttp3 请求头不能为中文）如果url参数值含有中文、特殊字符时，需要使用 url 编码。
        requestBodyMap["myfiles\"; filename=\"${URLEncoder.encode(file.name, "utf-8")}"] = fileBody
        return requestBodyMap
    }

    /**
     * 生成上传图片请求参数
     * @param type 用途类型
     */
    fun getUploadImgMap(type: Int): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        map["type"] = type
        map["time"] = System.currentTimeMillis()
        return map
    }
}

