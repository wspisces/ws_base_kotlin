package com.ws.support.http

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
@JvmSuppressWildcards
interface _ApiUrl {
    //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
    //JsonUtil.toJson(param));
    //登陆
//    @POST("equipment/v1/equipment/login")
//    fun login(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //初始化-保存设备
//    @POST("equipment/v1/c/equipment/save")
//    fun save(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //商家信息
//    @POST("equipment/v1/c/equipment/merchant")
//    fun title(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //设备报警 equipmentId
//    @POST("equipment/v1/c/batch/equipment/warning")
//    fun machineAlert(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //设备状态
//    @POST("equipment/v1/c/equipment/status")
//    fun status(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-上药商品列表
//    @POST("equipment/v1/c/equipment/type/slot/product/paging")
//    fun productList(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //省市区
//    @POST("equipment/v1/area/list")
//    fun areaList(): Observable<ResultTO?>?
//
//    //设备类型
//    @POST("equipment/v1/dict/type")
//    fun equipmentType(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-清零
//    @POST("equipment/v1/c/equipment/type/slot/delete")
//    fun slotDelete(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-上药商品扫码
//    @POST("equipment/v1/c/equipment/type/slot/product/item")
//    fun slotProductScan(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-设备药槽商品库存详情
//    //    @POST("equipment/v1/c/equipment/type/slot/product/stock/item")
//    //    Observable<ResultTO> slotProductDetail(@QueryMap Map<String, Object> options);
//    //配送员-设备药槽商品库存录入
//    @POST("equipment/v1/c/equipment/type/slot/product/stock/save")
//    fun productStockSave(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //配送员-上药商品清空
//    @POST("equipment/v1/c/equipment/type/slot/product/delete")
//    fun productDelete(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-上药商品清空扫码
//    @POST("equipment/v1/c/equipment/type/slot/product/info")
//    fun productInfo(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-设备下架商品列表
//    @POST("equipment/v1/c/equipment/type/slot/product/undercarriage/paging")
//    fun undercarriage(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //配送员-下架
//    @POST("equipment/v1/c/equipment/type/slot/product/undercarriage/delete")
//    fun undercarriageDelete(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-同批次商品录入信息
//    @POST("equipment/v1/c/equipment/type/slot/product/info/item")
//    fun productInfoItem(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-设备商品模板列表
//    @POST("equipment/v1/c/equipment/type/slot/product/model/paging")
//    fun modelList(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //配送员-设备商品模板制作详情
//    @POST("equipment/v1/c/equipment/type/slot/product/model/item")
//    fun modelDetail(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //配送员-设备商品模板制作保存
//    @POST("equipment/v1/c/equipment/type/slot/product/model/save")
//    fun modelSave(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //上传图片
//    @Multipart
//    @POST("equipment/v1/upload/image")
//    fun uploadFile(@PartMap headPortrait: Map<String?, RequestBody?>?): Observable<ResultTO?>?
//
//    //备货完成
//    @POST("equipment/v1/c/equipment/type/slot/product/stock/finish")
//    fun stockFinish(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //备货清单
//    @POST("equipment/v1/c/equipment/type/slot/product/stock/list")
//    fun stockList(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //    //备货清单
//    //    @POST("equipment/v1/c/equipment/type/slot/product/stock/list")
//    //    Observable<ResultTO> stockList(@Body RequestBody map);
//    //whoami
//    @POST("equipment/v1/whoami")
//    fun whoami(): Observable<ResultTO?>?
//
//    //强制更新 废弃 ws
//    @Deprecated("")
//    @GET("equipment/v1/dict/type")
//    fun update(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //版本检查
//    @POST("equipment/v1/dict/version")
//    fun version(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //版本检查
//    @POST("equipment/v1/dict/version")
//    fun version(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //获取温湿度预警阈值
//    @POST("equipment/v1/c/batch/temperature/warning/threshold")
//    fun thresholdForTempAndHumi(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //温湿度报警
//    @POST("equipment/v1/c/batch/temperature/warning")
//    fun alarmForTempAndHumi(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    @POST("equipment/v1/c/batch/temperature/warning")
//    fun alarmForTempAndHumi(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //上传温湿度 {“equipmentId”-设备主键编号；“temperature”-温度；“humidity”-湿度}
//    @POST("equipment/v1/c/batch/temperature/record")
//    fun updateTempAndHumi(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    //图片接口
//    @POST("equipment/v1/c/equipment/product/templates")
//    fun dragImages(@QueryMap options: Map<String?, Any?>?): Observable<ResultTO?>?
//
//    //判断商品和槽位是否可以绑定
//    @POST("equipment/v1/c/equipment/type/slot/check")
//    fun checkSlot(@Body map: RequestBody?): Observable<ResultTO?>?
//
//    /*@Streaming
//    @GET("/api/services/app/SystemUpdate/DownloadCurrentVersion")
//    Observable<ResponseBody> DownloadCurrentVersion();*/
//    //url 下载链接
//    @Streaming
//    @GET
//    fun download(@Url url: String?): Observable<ResponseBody?>?
//
//    //获取当前版本号
//    @GET("api/mobile/latest/version")
//    fun GetCurrentVersion(): Observable<ResultTO?>?
}