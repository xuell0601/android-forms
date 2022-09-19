package com.raedev.forms.internal.mapping

import com.raedev.forms.FormField
import java.lang.ref.SoftReference
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaType

/**
 * 表单映射
 * @author RAE
 * @date 2022/09/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FormFieldMapping {

    /** 映射表 */
    private val fieldMapping: MutableMap<String, FormFieldMap> = mutableMapOf()

    /** 字段列表 */
    val fields: List<FormFieldMap>
        get() = fieldMapping.values.toList()

    // region 反射方法

    /**
     * 读取实体类的所有字段
     */
    protected open fun readFields(clz: Class<*>): List<FieldItem> {
        val type = FormField::class.java
        val result = clz.declaredFields.map {
            val annotation = when {
                it.isAnnotationPresent(type) -> it.getAnnotation(type)
                else -> null
            }
            FieldItem(it, it.name, it.type, annotation)
        }.toMutableList()
        if (clz.superclass != null) {
            val fields = readFields(clz.superclass)
            if (fields.isNotEmpty()) result.addAll(fields)
        }
        return result
    }

    protected open fun readKtProperties(clz: KClass<*>): List<FieldItem> {
        val result = clz.declaredMemberProperties.map {
            FieldItem(
                it.javaField!!,
                it.name,
                it.returnType.withNullability(false).javaType,
                it.findAnnotation()
            )
        }
        return result
    }

    // endregion

    // region 抽象方法

    /**
     * 将字段转成映射表
     */
    protected abstract fun convertToMap(field: FieldItem): FormFieldMap

    // endregion

    /**
     * 解析实体类，只需要执行一次并缓存下来。
     */
    internal fun <T : Any> parse(entity: T) {
        readKtProperties(entity::class).forEach {
            val name = it.name
            fieldMapping[name] = convertToMap(it).also { map ->
                map.objRef = SoftReference(entity)
            }

        }
    }

    /**
     * 清除缓存
     */
    fun clean() {
        fieldMapping.forEach { it.value.clear() }
        fieldMapping.clear()
    }

    /**
     * 根据字段名获取字段映射信息
     */
    operator fun get(name: String): FormFieldMap? {
        return fieldMapping[name]
    }

    /**
     * 获取字段对应的Label
     */
    fun getLabel(name: String): String? {
        return this[name]?.label
    }

    /**
     * 设置实体值
     */
    internal open fun setValue(name: String, value: String?) {
        val item = this[name] ?: return
        val entity = item.objRef?.get() ?: return
        val javaField = item.field
        when (item.returnType) {
            String::class.javaObjectType -> javaField.set(entity, value.toString())
            Int::class.javaObjectType -> when {
                "true".equals(value, true) -> javaField.set(entity, 1)
                "false".equals(value, true) -> javaField.set(entity, 0)
                else -> javaField.set(entity, value?.toInt())
            }
            Long::class.javaObjectType -> javaField.setLong(entity, value?.toLong() ?: 0)
            Double::class.javaObjectType -> javaField.setDouble(entity, value?.toDouble() ?: 0.0)
            Float::class.javaObjectType -> javaField.setFloat(entity, value?.toFloat() ?: 0f)
            Boolean::class.javaObjectType -> javaField.setBoolean(entity, value.toBoolean())
            else -> javaField.set(entity, value)
        }
    }

}