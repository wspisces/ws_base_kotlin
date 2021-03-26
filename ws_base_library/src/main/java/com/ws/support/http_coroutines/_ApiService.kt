package com.ws.support.http_coroutines

import retrofit2.http.GET


/**
 * 描述信息
 * @author ws
 * @date 3/25/21 10:03 AM
 * 修改人：ws
 */
interface _ApiService {
    @GET("https://www.apiopen.top/novelApi")
    suspend fun getFictions(): BaseResp<List<Fiction>>
}