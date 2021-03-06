@file:Suppress("NAME_SHADOWING")

package com.ws.support.utils

import android.text.TextUtils
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object JsonUtils {

    fun isNotEmpty(mObject: Any?): Boolean {
        return !(mObject == null || TextUtils.equals(mObject.toString(), "null")
                || TextUtils.equals(mObject.toString(), "[]")
                || TextUtils.equals(mObject.toString(), "{}")
                || TextUtils.equals(mObject.toString(), ""))
    }

    private const val TAG = "JsonUtils"
    const val EMPTY = ""

    /** 空的 `JSON` 数据 - `"{}"`。  */
    const val EMPTY_JSON = "{}"

    /** 空的 `JSON` 数组(集合)数据 - `"[]"`。  */
    const val EMPTY_JSON_ARRAY = "[]"

    /** 默认的 `JSON` 日期/时间字段的格式化模式。  */
    const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS"

    /** `Google Gson` 的 @Since 注解常用的版本号常量 - `1.0`。  */
    const val SINCE_VERSION_10 = 1.0

    /** `Google Gson` 的 @Since 注解常用的版本号常量 - `1.1`。  */
    const val SINCE_VERSION_11 = 1.1

    /** `Google Gson` 的 @Since 注解常用的版本号常量 - `1.2`。  */
    const val SINCE_VERSION_12 = 1.2
    /**
     * 将给定的目标对象根据指定的条件参数转换成 `JSON` 格式的字符串。
     *
     *
     * **该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回 `"{}"`； 集合或数组对象返回
     * `"[]"`**
     *
     * @param target
     * 目标对象。
     * @param targetType
     * 目标对象的类型。
     * @param isSerializeNulls
     * 是否序列化 `null` 值字段。
     * @param version
     * 字段的版本号注解。
     * @param datePattern
     * 日期字段的格式化模式。
     * @param excludesFieldsWithoutExpose
     * 是否排除未标注 @Expose 注解的字段。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法只用来转换普通的 `JavaBean`
     * 对象。**
     *
     *  * 该方法只会转换标有 @Expose 注解的字段；
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法会转换所有未标注或已标注 @Since 的字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    @JvmName("toJson1")
    @JvmOverloads
    fun toJson(
            target: Any?, targetType: Type? = null,
            isSerializeNulls: Boolean = false, version: Double? = null, datePattern: String? = null,
            excludesFieldsWithoutExpose: Boolean = true,
    ): String {
        var datePattern = datePattern
        if (target == null) return EMPTY_JSON
        val builder = GsonBuilder()
        if (isSerializeNulls) builder.serializeNulls()
        if (version != null) builder.setVersion(version.toDouble())
        if (isEmpty(datePattern)) datePattern = DEFAULT_DATE_PATTERN
        builder.setDateFormat(datePattern)
        if (excludesFieldsWithoutExpose) builder.excludeFieldsWithoutExposeAnnotation()
        val result: String
        val gson = builder.create()
        result = try {
            if (targetType != null) {
                gson.toJson(target, targetType)
            } else {
                gson.toJson(target)
            }
        } catch (ex: Exception) {
            Log.e(TAG, "目标对象 " + target.javaClass.name
                    + " 转换 JSON 字符串时，发生异常！")
            // log.warn("目标对象 " + target.getClass().getName()
            // + " 转换 JSON 字符串时，发生异常！", ex);
            if (target is Collection<*> || target is Iterator<*>
                    || target is Enumeration<*>
                    || target.javaClass.isArray) {
                EMPTY_JSON_ARRAY
            } else EMPTY_JSON
        }
        return result
    }
    /**
     *
     */
    // public static String toJson(Object target,TypeToken<T> token)
    // {
    // Gson gson = new Gson();
    // return gson.toJson(target);
    // }
    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法只用来转换普通的 `JavaBean`
     * 对象。**
     *
     *  * 该方法只会转换标有 @Expose 注解的字段；
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法会转换所有未标注或已标注 @Since 的字段；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param datePattern
     * 日期字段的格式化模式。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(target: Any?, datePattern: String?): String {
        return toJson(target, null, false, null, datePattern, true)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法只用来转换普通的 `JavaBean`
     * 对象。**
     *
     *  * 该方法只会转换标有 @Expose 注解的字段；
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param version
     * 字段的版本号注解(@Since)。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(target: Any?, version: Double?): String {
        return toJson(target, null, false, version, null, true)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法只用来转换普通的 `JavaBean`
     * 对象。**
     *
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法会转换所有未标注或已标注 @Since 的字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param excludesFieldsWithoutExpose
     * 是否排除未标注 @Expose 注解的字段。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(
            target: Any?,
            excludesFieldsWithoutExpose: Boolean,
    ): String {
        return toJson(target, null, false, null, null,
                excludesFieldsWithoutExpose)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法只用来转换普通的 `JavaBean`
     * 对象。**
     *
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param version
     * 字段的版本号注解(@Since)。
     * @param excludesFieldsWithoutExpose
     * 是否排除未标注 @Expose 注解的字段。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(
            target: Any?, version: Double?,
            excludesFieldsWithoutExpose: Boolean,
    ): String {
        return toJson(target, null, false, version, null,
                excludesFieldsWithoutExpose)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法通常用来转换使用泛型的对象。**
     *
     *  * 该方法只会转换标有 @Expose 注解的字段；
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法会转换所有未标注或已标注 @Since 的字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param targetType
     * 目标对象的类型。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(target: Any?, targetType: Type?): String {
        return toJson(target, targetType, false, null, null, false)
        // Gson gson = new Gson();
        // return gson.toJson(target, targetType);
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法通常用来转换使用泛型的对象。**
     *
     *  * 该方法只会转换标有 @Expose 注解的字段；
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param targetType
     * 目标对象的类型。
     * @param version
     * 字段的版本号注解(@Since)。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(target: Any?, targetType: Type?, version: Double?): String {
        return toJson(target, targetType, false, version, null, true)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法通常用来转换使用泛型的对象。**
     *
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法会转换所有未标注或已标注 @Since 的字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param targetType
     * 目标对象的类型。
     * @param excludesFieldsWithoutExpose
     * 是否排除未标注 @Expose 注解的字段。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(
            target: Any?, targetType: Type?,
            excludesFieldsWithoutExpose: Boolean,
    ): String {
        return toJson(target, targetType, false, null, null,
                excludesFieldsWithoutExpose)
    }

    /**
     * 将给定的目标对象转换成 `JSON` 格式的字符串。**此方法通常用来转换使用泛型的对象。**
     *
     *  * 该方法不会转换 `null` 值字段；
     *  * 该方法转换时使用默认的 日期/时间 格式化模式 - `yyyy-MM-dd HH:mm:ss SSS`；
     *
     *
     * @param target
     * 要转换成 `JSON` 的目标对象。
     * @param targetType
     * 目标对象的类型。
     * @param version
     * 字段的版本号注解(@Since)。
     * @param excludesFieldsWithoutExpose
     * 是否排除未标注 @Expose 注解的字段。
     * @return 目标对象的 `JSON` 格式的字符串。
     */
    fun toJson(
            target: Any?, targetType: Type?, version: Double?,
            excludesFieldsWithoutExpose: Boolean,
    ): String {
        return toJson(target, targetType, false, version, null,
                excludesFieldsWithoutExpose)
    }

    /**
     * 将给定的 `JSON` 字符串转换成指定的类型对象。
     *
     * @param <T>
     * 要转换的目标类型。
     * @param json
     * 给定的 `JSON` 字符串。
     * @param token
     * `com.google.gson.reflect.TypeToken` 的类型指示类对象。
     * @param datePattern
     * 日期格式模式。
     * @return 给定的 `JSON` 字符串表示的指定的类型对象。
    </T> */
    fun <T> fromJson(
            json: String, token: TypeToken<T>,
            datePattern: String? = null,
    ): T? {
        var datePattern = datePattern
        if (isEmpty(json)) {
            return null
        }
        val builder = GsonBuilder()
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN
        }
        val gson = builder.create()
        return try {
            gson.fromJson(json, token.type)
        } catch (ex: Exception) {
            //json +
            Log.e(TAG, "无法转换为 " + token.rawType.name + " 对象!", ex)
            // log.error(json + " 无法转换为 " + token.getRawType().getName() +
            // " 对象!",
            // ex);
            null
        }
    }

    /**
     * 将给定的 `JSON` 字符串转换成指定的类型对象。
     *
     * @param <T>
     * 要转换的目标类型。
     * @param json
     * 给定的 `JSON` 字符串。
     * @param token
     * `com.google.gson.reflect.TypeToken` 的类型指示类对象。
     * @return 给定的 `JSON` 字符串表示的指定的类型对象。
    </T> */
    fun <T> fromJson(json: String, token: TypeToken<T>): T {
        return fromJson(json, token)
    }

    /**
     * 将给定的 `JSON` 字符串转换成指定的类型对象。**此方法通常用来转换普通的 `JavaBean`
     * 对象。**
     *
     * @param <T>
     * 要转换的目标类型。
     * @param json
     * 给定的 `JSON` 字符串。
     * @param clazz
     * 要转换的目标类。
     * @param datePattern
     * 日期格式模式。
     * @return 给定的 `JSON` 字符串表示的指定的类型对象。
    </T> */
    fun <T> fromJson(json: String, clazz: Class<T>, datePattern: String?): T? {
        var datePattern = datePattern
        if (isEmpty(json)) {
            return null
        }
        val builder = GsonBuilder()
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN
        }
        val gson = builder.create()
        return try {
            gson.fromJson(json, clazz)
        } catch (ex: Exception) {
            Log.e(TAG, json + " 无法转换为 " + clazz.name + " 对象!")
            // log.error(json + " 无法转换为 " + clazz.getName() + " 对象!", ex);
            null
        }
    }

    /**
     * 将给定的 `JSON` 字符串转换成指定的类型对象。**此方法通常用来转换普通的 `JavaBean`
     * 对象。**
     *
     * @param <T>
     * 要转换的目标类型。
     * @param json
     * 给定的 `JSON` 字符串。
     * @param clazz
     * 要转换的目标类。
     * @return 给定的 `JSON` 字符串表示的指定的类型对象。
    </T> */
    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return fromJson(json, clazz)
    }

    fun isEmpty(inStr: String?): Boolean {
        var reTag = false
        if (inStr == null || "" == inStr) {
            reTag = true
        }
        return reTag
    }
}