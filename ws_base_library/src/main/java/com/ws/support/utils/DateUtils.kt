package com.ws.support.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 *
 * @author Johnny.xu
 * date 2017/2/15
 */
object DateUtils {
    /**
     * 时间日期格式化到年月日.
     */
    var dateFormatYMD = "yyyy-MM-dd"

    /**
     * 时间日期格式化到年月.
     */
    var dateFormatYM = "yyyy-MM"

    /**
     * 时分秒.
     */
    var dateFormatHMS = "HH:mm:ss"

    /**
     * 时分.
     */
    var dateFormatHM = "HH:mm"

    /**
     * 小时
     */
    var dateFormatH = "HH"

    /**
     * 分
     */
    var dateFormatM = "mm"

    /**
     * 日
     */
    var dateFormatD = "dd"

    /**
     * 时间日期格式化到年月日时分秒.
     */
    var dateFormatYMDHMS = "yyyy-MM-dd" + " " + dateFormatHMS

    /**
     * 时间日期格式化到年月日时分.
     */
    var dateFormatYMDHM = "yyyy-MM-dd" + " " + dateFormatHM

    /**
     * 时间日期格式化到年月日时.
     */
    var dateFormatYMDH = "yyyy-MM-dd" + " " + dateFormatH

    /**
     * 时间日期格式化到月日时分.
     */
    var dateFormatMDHM_CN = "MM月dd日" + " " + dateFormatHM

    /**
     * 中文的年月日
     */
    var dateFormatYMD_CN = "yyyy年MM月dd日"

    /**
     * 中文的年月
     */
    var dateFormatYM_CN = "yyyy年MM月"

    /**
     * 中文的年月日 时分
     */
    var dateFormatYMDHM_CN = dateFormatYMD_CN + " " + dateFormatHM

    /**
     * .的年月日 时分
     */
    var dateFormatYMD_P = "yyyy.MM.dd"

    /**
     * 时间日期格式化到年月日.
     */
    var dateFormatYMD_S = "yyyy / MM / dd"
    var dateFormatYMD_S2 = "yyyy/MM/dd"

    /**
     * 时间日期格式化到年月日时分
     */
    var dateFormatYMD_HM_S = "yyyy/MM/dd " + dateFormatHM
    var dateFormatYMD_HM_P = "yyyy.MM.dd " + dateFormatHM

    /**
     * 月-日
     */
    var dateFormatMD = "MM-dd"

    /**
     * 年
     */
    var dateFormatY = "yyyy"

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    fun getDateByFormat(strDate: String?, format: String?): Date? {
        val mSimpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        var date: Date? = null
        try {
            date = mSimpleDateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 得到当天凌晨时间
     */
    fun getMorningCurrentTime(): Long {
        return getDateByFormat(getCurrentDateByFormat(dateFormatYMD), dateFormatYMD)!!.time
    }

    /**
     * 得到当前时间时分秒
     */
    fun getCurrentHMSTime(): Long {
        val timeStr = getCurrentDateByFormat(dateFormatHMS)!!.split(":".toRegex()).toTypedArray()
        return if (timeStr.size == 3) {
            (Integer.valueOf(timeStr[0]) * 60 * 60 + Integer.valueOf(timeStr[1]) * 60 + Integer.valueOf(timeStr[2])).toLong()
        } else 0
    }

    /**
     * 匹配时间是否是当天
     */
    fun matchDateIsToday(time: Long, format: String?): Boolean {
        val todayString = getCurrentDateByFormat(format)
        val selectString = formatTimeByMillisecond(time, format)
        return if (todayString == null || selectString == null) {
            false
        } else {
            todayString == selectString
        }
    }

    /**
     * 匹配时间是同一小时
     */
    fun matchDataIsSameHour(time: Long, secondTime: Long): Boolean {
        return (formatTimeByMillisecond(time, dateFormatYMDH)
                == formatTimeByMillisecond(secondTime, dateFormatYMDH))
    }

    /**
     * 匹配时间是同一天
     */
    fun matchDataIsSameDay(time: Long, secondTime: Long): Boolean {
        return (formatTimeByMillisecond(time, dateFormatYMD)
                == formatTimeByMillisecond(secondTime, dateFormatYMD))
    }

    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    fun formatTimeByDate(date: Date?, format: String?): String? {
        val mSimpleDateFormat = SimpleDateFormat(format)
        var strDate: String? = null
        try {
            strDate = mSimpleDateFormat.format(date)
        } catch (e: Exception) {
        }
        return strDate
    }

    /**
     * 描述：long类型转化为String类型.
     *
     * @param millisecond  the date
     * @param formatString the format
     * @return String String类型日期时间
     */
    fun formatTimeByMillisecond(millisecond: Long, formatString: String?): String {
        val date = Date(millisecond)
        val format = SimpleDateFormat(formatString)
        return format.format(date)
    }

    /**
     * 将时间数据转格式化为年月日（yyyy-MM-dd HH:mm:ss）
     *
     * @param object 只能转换Date和Long类型的数据
     * @return 转换为指定格式的数据 yyyy-MM-dd HH:mm:ss
     */
    fun formatTimeToYMDHMS(`object`: Any?): String? {
        return if (`object` == null) {
            throw NullPointerException("转换数据不能为空")
        } else if (`object` is Date || `object` is Long) {
            if (`object` is Date) {
                formatTimeByDate(`object` as Date?, dateFormatYMDHMS)
            } else {
                formatTimeByMillisecond(`object` as Long, dateFormatYMDHMS)
            }
        } else {
            throw UnsupportedOperationException("只能转换Date和Long类型的数据")
        }
    }

    /**
     * 将时间数据转格式化为年月日（yyyy-MM-dd HH:mm）
     *
     * @param object 只能转换Date和Long类型的数据
     * @return 转换为指定格式的数据 yyyy-MM-dd HH:mm
     */
    fun formatTimeToYMDHM(`object`: Any?): String? {
        return if (`object` == null) {
            throw NullPointerException("转换数据不能为空")
        } else if (`object` is Date || `object` is Long) {
            if (`object` is Date) {
                formatTimeByDate(`object` as Date?, dateFormatYMDHM)
            } else {
                formatTimeByMillisecond(`object` as Long, dateFormatYMDHM)
            }
        } else {
            throw UnsupportedOperationException("只能转换Date和Long类型的数据")
        }
    }

    /**
     * 将时间数据转格式化为年月日（yyyy-MM-dd）
     *
     * @param object 只能转换Date和Long类型的数据
     * @return 转换为指定格式的数据 yyyy-MM-dd
     */
    fun formatTimeToYM(`object`: Any?): String? {
        return if (`object` == null) {
            throw NullPointerException("转换数据不能为空")
        } else if (`object` is Date || `object` is Long) {
            if (`object` is Date) {
                formatTimeByDate(`object` as Date?, dateFormatYM)
            } else {
                formatTimeByMillisecond(`object` as Long, dateFormatYM)
            }
        } else {
            throw UnsupportedOperationException("只能转换Date和Long类型的数据")
        }
    }

    /**
     * 将时间数据转格式化为年月日（yyyy-MM-dd）
     *
     * @param object 只能转换Date和Long类型的数据
     * @return 转换为指定格式的数据 yyyy-MM-dd
     */
    fun formatTimeToYMD(`object`: Any?): String? {
        return if (`object` == null) {
            throw NullPointerException("转换数据不能为空")
        } else if (`object` is Date || `object` is Long) {
            if (`object` is Date) {
                formatTimeByDate(`object` as Date?, dateFormatYMD)
            } else {
                formatTimeByMillisecond(`object` as Long, dateFormatYMD)
            }
        } else {
            throw UnsupportedOperationException("只能转换Date和Long类型的数据")
        }
    }

    /**
     * 将时间数据转格式化为年月日（yyyy-MM-dd）
     *
     * @param object 只能转换Date和Long类型的数据
     * @return 转换为指定格式的数据 yyyy-MM-dd
     */
    fun formatTimeToHMS(`object`: Any?): String? {
        return if (`object` == null) {
            throw NullPointerException("转换数据不能为空")
        } else if (`object` is Date || `object` is Long) {
            if (`object` is Date) {
                formatTimeByDate(`object` as Date?, dateFormatHMS)
            } else {
                formatTimeByMillisecond(`object` as Long, dateFormatHMS)
            }
        } else {
            throw UnsupportedOperationException("只能转换Date和Long类型的数据")
        }
    }

    /**
     * 描述：获取指定日期时间的字符串.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    fun getStringByFormat(strDate: String?, format: String?): String? {
        var mDateTime: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(dateFormatYMDHMS)
            c.time = mSimpleDateFormat.parse(strDate)
            val mSimpleDateFormat2 = SimpleDateFormat(format)
            mDateTime = mSimpleDateFormat2.format(c.time)
        } catch (e: Exception) {
        }
        return mDateTime
    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    fun getCurrentDateByFormat(format: String?): String? {
        var curDateTime: String? = null
        try {
            val mSimpleDateFormat = SimpleDateFormat(format)
            val c: Calendar = GregorianCalendar()
            curDateTime = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
        }
        return curDateTime
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的天数
     */
    fun getDifferenceDay(date1: Long, date2: Long): Int {
        return getDifferenceDay(Date(date1), Date(date2))
    }

    internal fun getDifferenceDay(now: Date?, returnDate: Date?): Int {
        val cNow = Calendar.getInstance()
        val cReturnDate = Calendar.getInstance()
        cNow.time = now
        cReturnDate.time = returnDate
        setTimeToMidnight(cNow)
        setTimeToMidnight(cReturnDate)
        val todayMs = cNow.timeInMillis
        val returnMs = cReturnDate.timeInMillis
        val intervalMs = todayMs - returnMs
        val intervalDay = (intervalMs / (1000 * 86400)).toInt()
        return if (intervalDay < 0) -intervalDay else intervalDay
    }

    private fun setTimeToMidnight(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
    }

    /**
     * 描述：计算两个日期所差的月份.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的月份
     */
    fun getDifferenceMonth(date1: Long, date2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = date1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = date2
        //先判断是否同年
        val y1 = calendar1[Calendar.YEAR]
        val y2 = calendar2[Calendar.YEAR]
        val d1 = calendar1[Calendar.MONTH]
        val d2 = calendar2[Calendar.MONTH]
        val month = (y1 - y2) * 12 + d1 - d2
        return if (month < 0) -month else month
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    fun getDifferenceHour(date1: Long, date2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = date1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = date2
        val h1 = calendar1[Calendar.HOUR_OF_DAY]
        val h2 = calendar2[Calendar.HOUR_OF_DAY]
        val h: Int
        val day = getDifferenceDay(date1, date2)
        h = h1 - h2 + day * 24
        return h
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    fun getDifferenceMinutes(date1: Long, date2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = date1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = date2
        val m1 = calendar1[Calendar.MINUTE]
        val m2 = calendar2[Calendar.MINUTE]
        val h = getDifferenceHour(date1, date2)
        val m: Int
        m = m1 - m2 + h * 60
        return m
    }

    /**
     * 描述：根据时间返回几天前或几分钟的描述.
     *
     * @param strDate the str date
     * @return the string
     */
    fun formatDateStr2Desc(strDate: String?, outFormat: String?): String? {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        try {
            c2.time = df.parse(strDate)
            c1.time = Date()
            val d = getDifferenceDay(c1.timeInMillis, c2.timeInMillis)
            if (d == 0) {
                val h = getDifferenceHour(c1.timeInMillis, c2.timeInMillis)
                if (h > 0) {
                    return h.toString() + "小时前"
                } else if (h < 0) {
                    return Math.abs(h).toString() + "小时后"
                } else if (h == 0) {
                    val m = getDifferenceMinutes(c1.timeInMillis, c2.timeInMillis)
                    return if (m > 0) {
                        m.toString() + "分钟前"
                    } else if (m < 0) {
                        Math.abs(m).toString() + "分钟后"
                    } else {
                        "刚刚"
                    }
                }
            } else if (d > 0) {
                if (d == 1) {
                    return "昨天"
                } else if (d == 2) {
                    return "前天"
                }
                return Math.abs(d).toString() + "天前"
            } else if (d < 0) {
                if (d == -1) {
                    return "明天"
                } else if (d == -2) {
                    return "后天"
                }
                return Math.abs(d).toString() + "天后"
            }
            val out = getStringByFormat(strDate, outFormat)
            if (!StringUtils.isEmpty(out)) {
                return out
            }
        } catch (e: Exception) {
        }
        return strDate
    }

    /**
     * 根据毫秒时间返回几天前或几分钟的描述.
     * @param timestamp
     */
    fun formatDateStr2Desc(timestamp: Long): String {
        val currentSeconds = getCurrTimeMillis() / 1000
        val interval = currentSeconds - timestamp / 1000 // 与现在时间相差秒数
        val timeStr: String
        timeStr = if (interval > 24 * 60 * 60) { // 1天以上
            (interval / (24 * 60 * 60)).toString() + "天前"
        } else if (interval > 60 * 60) { // 1小时-24小时
            (interval / (60 * 60)).toString() + "小时前"
        } else if (interval > 60) { // 1分钟-59分钟
            (interval / 60).toString() + "分钟前"
        } else { // 1秒钟-59秒钟
            "刚刚"
        }
        return timeStr
    }

    fun getCurrTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 本系统年转换成天
     */
    const val DAY_OF_YEAR = 360

    /**
     * 本系统月换成天
     */
    const val DAY_OF_MONTH = 30

    /**
     * 计算耗时
     *
     * @param millis
     * @return
     */
    fun convertHMSWithMillis(millis: Long): String {
        return if (millis < 60) {
            millis.toString() + "秒"
        } else if (millis < 3600) {
            "约" + millis / 60 + "分钟"
        } else if (millis < 3600 * 24) {
            "约" + millis / 3600 + "小时"
        } else {
            "约" + millis / 3600 / 24 + "天"
        }
    }

    //判断两个日期是否是同一天
    fun isSameDate(long1: Long?, long2: Long?): Boolean {
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c1.time = Date(long1!!)
        c2.time = Date(long2!!)
        return (c1[Calendar.YEAR] == c2[Calendar.YEAR]
                && c1[Calendar.MONTH] == c2[Calendar.MONTH]
                && c1[Calendar.DAY_OF_MONTH] == c2[Calendar.DAY_OF_MONTH])
    }

    /**
     * 将日期转化成毫秒
     *
     * @param format
     * @return
     */
    fun date2longTime(dateString: String?, format: String?): Long {

        //先把字符串转成Date类型
        val sdf = SimpleDateFormat(format)

        //此处会抛异常
        var date: Date? = null
        try {
            date = sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        //获取毫秒数
        return date!!.time
    }

    /**
     * 根据日期获取星期
     *
     * @param date
     * @return
     */
    fun date2Week(date: String): String {
        val arg = date.split("-".toRegex()).toTypedArray()
        val year = Integer.valueOf(arg[0])
        val month = Integer.valueOf(arg[1])
        val day = Integer.valueOf(arg[2])

        //获得一个日历
        val calendar = Calendar.getInstance()

        //设置当前时间,月份是从0月开始计算
        calendar[year, month - 1] = day

        //星期表示1-7，是从星期日开始，
        val number = calendar[Calendar.DAY_OF_WEEK]
        val strWeek = arrayOf("", "周日", "周一", "周二", "周三", "周四", "周五", "周六")
        return strWeek[number]
    }

    /**
     * 获取之后几天的时间
     */
    fun getNextDay(day: Int): Long {
        return getNextDayByTime(day, -1)
    }

    /**
     * 获取之后几天的时间
     *
     * @param hour 将时间调整为hour:00；当大于23或小于0时则取当前时间
     */
    fun getNextDayByTime(day: Int, hour: Int): Long {
        //获得一个日历
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getCurrTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, day)
        if (hour > 0 && hour < 24) {
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = 0
        }
        return calendar.timeInMillis
    }

    /**
     * 将秒数转换成天具体的天时分秒
     * 比如172800S转换成2天0时0分0秒
     *
     * @param second
     * @return
     */
    fun formatSecond(second: Long): String {
        val timeStr = "0秒"
        if (second > 0) {
            val sb = StringBuilder()
            val days = (second / (60 * 60 * 24)).toInt()
            val hours = (second / (60 * 60) - days * 24).toInt()
            val minutes = (second / 60 - hours * 60 - days * 24 * 60).toInt()
            val seconds = (second - minutes * 60 - hours * 60 * 60 - days * 24 * 60 * 60).toInt()
            if (days > 0) {
                sb.append(days).append("天")
            }
            if (hours > 0) {
                sb.append(hours).append("小时")
            }
            if (minutes > 0) {
                sb.append(minutes).append("分")
            }
            if (seconds > 0) {
                sb.append(seconds).append("秒")
            }
            if (sb.length > 0) {
                return sb.toString()
            }
        }
        return timeStr
    }

    var sdf9 = SimpleDateFormat("yyyy年MM月dd日HH")
    fun format9(d: Date?): String {
        return sdf9.format(d)
    }

    fun getDayStartAndEndString(calendar: Calendar): Array<String?> {
        val strs = arrayOfNulls<String>(2)
        strs[0] = formatTimeToYMD(calendar.time) + " 00:00:00"
        strs[1] = formatTimeToYMD(calendar.time) + " 23:59:59"
        return strs
    }
}