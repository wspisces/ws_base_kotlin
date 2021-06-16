@file:Suppress("NAME_SHADOWING")

package com.ws.support.utils

/**
 * http://blog.csdn.net/ma969070578/article/details/41013547
 * http://blog.csdn.net/woddle/article/details/21698891
 *
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 chenhua
 *
 *
 * 地图 					坐标系
 * 百度地图 				百度坐标（BD-09）
 * 腾讯搜搜地图 			火星坐标
 * 搜狐搜狗地图			搜狗坐标*
 * 阿里云地图			火星坐标
 * 图吧MapBar地图 		图吧坐标
 * 高德amap地图 			火星坐标
 * 凯立德地图 			火星坐标（转为K码）
 * 灵图51ditu地图		火星坐标
 *
 * I、 百度坐标：在GCJ02基础上，进行了BD-09二次加密措施，API支持从WGS/GCJ转换成百度坐标，不支持反转。
 *
 * II、凯立德K码： a)
 * K码将地图分成了四块进行编码，中心点在内蒙的阿拉善左旗境内，该点的K码是7uy1yuy1y。以该点为中心分别在东西方向和南北方向画一条线当横纵
 * （XY）坐标轴
 * ，那么第一象限（即东北方向的那块）的K码的第1位全部都是5，第2象限的K码的第一位全是6，第3、4象限的K码的第一位分别全是7、8。并且该点有4个K码
 * ，即用四个K码定位都是这一点，这四个K码分别是7uy1yuy1y、80000uy1y、500000000、6uy1y0000。 　　 b)
 * K码的第2-5位表示东西方向上的坐标
 * ，第6-9位代表南北方向上的坐标。实际上K码就是一个凯立德特有的34进制数，（26个字母加10个阿拉伯数字，再去掉不用的小写L和O共34个字符
 * ），这个34进制数从左向右从低位向高位排列
 * （我们常用的10进制是从右向左由低位向高位排列），其中第2-5位东西方向上的数每一个单位代表2.5m左右，南北方向上的数每一个单位代表实际距离3米左右
 * 。比如80000uy1y向东约2.5米的点的K码就是81000uy1y，向东约34×2.5m的点的K码就是80100uy1y
 *
 * c)K码与火星坐标可相互转换。
 */
object MapCoordinateConvert {
    private const val pi = 3.14159265358979324
    private const val a = 6378245.0
    private const val ee = 0.00669342162296594323
    private const val x_pi = 3.14159265358979324 * 3000.0 / 180.0
    private fun outOfChina(lat: Double, lon: Double): Boolean {
        if (lon < 72.004 || lon > 137.8347) {
            return true
        }
        return if (lat < 0.8293 || lat > 55.8271) {
            true
        } else false
    }

    private fun transformLat(x: Double, y: Double): Double {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + (0.1
                * Math.sqrt(Math.abs(x)))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0
        return ret
    }

    /**
     * wgs84 坐标 转 火星坐标GCJ-02
     *
     * @param wgLon
     * 经度
     * @param wgLat
     * 纬度
     * @return 经度、纬度
     */
    fun wgs84Togcj02(wgLon: Double, wgLat: Double): DoubleArray {
        val result = DoubleArray(2)
        if (outOfChina(wgLat, wgLon)) {
            result[0] = wgLon
            result[1] = wgLat
        } else {
            var dLat = transformLat(wgLon - 105.0, wgLat - 35.0)
            var dLon = transformLon(wgLon - 105.0, wgLat - 35.0)
            val radLat = wgLat / 180.0 * pi
            var magic = Math.sin(radLat)
            magic = 1 - ee * magic * magic
            val sqrtMagic = Math.sqrt(magic)
            dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
            dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
            result[0] = wgLon + dLon
            result[1] = wgLat + dLat
        }
        return result
    }

    /**
     * 火星坐标转原始坐标 采用逆推法
     *
     * @param wgLon
     * 经度
     * @param wgLat
     * 纬度
     * @return
     */
    fun gcj02Towgs84(wgLon: Double, wgLat: Double): DoubleArray {
        val result = DoubleArray(2)
        val pl = wgs84Togcj02(wgLon, wgLat)
        val offsetLat = pl[1] - wgLat
        val offsetLng = pl[0] - wgLon
        result[0] = wgLon - offsetLng
        result[1] = wgLat - offsetLat
        return result
    }

    fun gcj02Towgs84Exact(gcjLon: Double, gcjLat: Double): DoubleArray {
        val result = DoubleArray(2)
        val initDelta = 0.01
        val threshold = 0.000000001
        var dLat = initDelta
        var dLon = initDelta
        var mLat = gcjLat - dLat
        var mLon = gcjLon - dLon
        var pLat = gcjLat + dLat
        var pLon = gcjLon + dLon
        var wgsLat: Double
        var wgsLon: Double
        var i = 0.0
        while (true) {
            wgsLat = (mLat + pLat) / 2
            wgsLon = (mLon + pLon) / 2
            val tmp = wgs84Togcj02(wgsLon, wgsLat)
            dLat = tmp[1] - gcjLat
            dLon = tmp[0] - gcjLon
            if (Math.abs(dLat) < threshold && Math.abs(dLon) < threshold) break
            if (dLat > 0) pLat = wgsLat else mLat = wgsLat
            if (dLon > 0) pLon = wgsLon else mLon = wgsLon
            if (++i > 10000) break
        }
        result[0] = wgsLon
        result[1] = wgsLat
        return result
    }

    /**
     * wgs84 坐标 转 百度Bd09
     *
     * @param wgLon
     * 经度
     * @param wgLat
     * 纬度
     * @return 经度、纬度
     */
    fun wgs84Tobd09(wgLon: Double, wgLat: Double): DoubleArray {
        val gcj = wgs84Togcj02(wgLon, wgLat)
        return gcj02Tobd09(gcj[0], gcj[1])
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标 即谷歌、高德 转 百度
     *
     * @param lon
     * @param lat
     */
    fun gcj02Tobd09(lon: Double, lat: Double): DoubleArray {
        val result = DoubleArray(2)
        val z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * x_pi)
        val theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * x_pi)
        val bd_lon = z * Math.cos(theta) + 0.0065
        val bd_lat = z * Math.sin(theta) + 0.006
        result[0] = bd_lon
        result[1] = bd_lat
        return result
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * 即 百度
     * 转 谷歌、高德
     *
     * @param lon
     * @param lat
     */
    fun bd09Togcj02(lon: Double, lat: Double): DoubleArray {
        val result = DoubleArray(2)
        val x = lon - 0.0065
        val y = lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi)
        val gg_lon = z * Math.cos(theta)
        val gg_lat = z * Math.sin(theta)
        result[0] = gg_lon
        result[1] = gg_lat
        return result
    }

    /**
     * (BD-09)-->84
     *
     * @param lon
     * @param lat
     * @return
     */
    fun bd09Towgs84(lon: Double, lat: Double): DoubleArray {
        val gcj02 = bd09Togcj02(lon, lat)
        return gcj02Towgs84(gcj02[0], gcj02[1])
    }

    /**
     * (mapbar)-->84
     *
     * @param lon
     * @param lat
     * @return
     */
    fun mapbarTowgs84(lon: Double, lat: Double): DoubleArray {
        var lon = lon
        var lat = lat
        val result = DoubleArray(2)
        lon = lon * 100000 % 36000000
        lat = lat * 100000 % 36000000
        val x1 = -(Math.cos(lat / 100000) * (lon / 18000) + Math.sin(lon / 100000) * (lat / 9000)) + lon
        val y1 = -(Math.sin(lat / 100000) * (lon / 18000) + Math.cos(lon / 100000) * (lat / 9000)) + lat
        val x2 = -(Math.cos(y1 / 100000) * (x1 / 18000) + Math.sin(x1 / 100000) * (y1 / 9000)) + lon + if (lon > 0) 1 else -1
        val y2 = -(Math.sin(y1 / 100000) * (x1 / 18000) + Math.cos(x1 / 100000) * (y1 / 9000)) + lat + if (lat > 0) 1 else -1
        result[0] = x2 / 100000.0
        result[1] = y2 / 100000.0
        return result
    }

    /**
     * WGS-84 to Web mercator 墨卡托
     *
     * @param wgsLon
     * @param wgsLat
     * @return
     */
    fun wgs84ToWebMercator(wgsLon: Double, wgsLat: Double): DoubleArray {
        val result = DoubleArray(2)
        val x = wgsLon * 20037508.34 / 180.0
        var y = Math.log(Math.tan((90.0 + wgsLat) * pi / 360.0)) / (pi / 180.0)
        y = y * 20037508.34 / 180.0
        result[0] = x
        result[1] = y
        return result
    }

    /**
     * // Web mercator to WGS-84
     *
     * @param mercatorLon
     * @param mercatorLat
     * @return
     */
    fun webMercatorToWgs84(mercatorLon: Double,
                           mercatorLat: Double): DoubleArray {
        val result = DoubleArray(2)
        val x = mercatorLon / 20037508.34 * 180.0
        var y = mercatorLat / 20037508.34 * 180.0
        y = 180 / pi * (2 * Math.atan(Math.exp(y * pi / 180.0)) - pi / 2)
        result[0] = x
        result[1] = y
        return result
    }

    /**
     * 得到转化方法名
     * @param from
     * @param to
     * @return
     */
    fun getDeclaredMethod(from: String, to: String): String {
        return from + "To" + to
    }
}