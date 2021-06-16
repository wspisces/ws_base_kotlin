package com.ws.start.http

import com.ws.support.http.LoginResult
import com.ws.support.http_coroutines.BaseResp
import com.ws.support.http_coroutines.Fiction
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * 描述信息
 * @author ws
 * @date 3/23/21 11:03 AM
 * 修改人：ws
 */
@JvmSuppressWildcards
interface StartApi {
    //登陆
    //@POST("equipment/v1/equipment/login")
    //fun login(@QueryMap options: Map<String, Any>?): Observable<ResultTO>

    //登录
    @POST("mobile/login")
    suspend fun login(@Body map: RequestBody): LoginResult

    @GET("https://www.apiopen.top/novelApi")
    suspend fun getFictions(): BaseResp<List<Fiction>>
}