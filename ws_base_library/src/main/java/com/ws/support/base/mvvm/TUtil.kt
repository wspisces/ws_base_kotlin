package com.ws.support.base.mvvm

import java.lang.reflect.*
import java.lang.reflect.Array

/**
 * @author ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
object TUtil {
    fun <T> getInstance(`object`: Any?, i: Int): T? {
        if (`object` != null) {
            try {
                val superClass = `object`.javaClass.genericSuperclass
                val type = (superClass as ParameterizedType).actualTypeArguments[i]
                val clazz = getRawType(type)
                return clazz.newInstance() as T
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getRawType(type: Type?): Class<*> {
        return if (type is Class<*>) {
            type
        } else if (type is ParameterizedType) {
            val rawType = type.rawType
            rawType as Class<*>
        } else if (type is GenericArrayType) {
            val componentType = type.genericComponentType
            Array.newInstance(getRawType(componentType), 0).javaClass
        } else if (type is TypeVariable<*>) {
            Any::class.java
        } else if (type is WildcardType) {
            getRawType(type.upperBounds[0])
        } else {
            val className = if (type == null) "null" else type.javaClass.name
            throw IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <$type> is of type $className")
        }
    }

    fun <T> checkNotNull(reference: T): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }
}