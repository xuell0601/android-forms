package com.raedev.forms.internal.binder

import com.raedev.forms.FormField
import com.raedev.forms.FormItemType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

/**
 * 表单实体绑定
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class FormEntityReference(private val entity: Any) {

    internal val fieldMap = mutableMapOf<String, FieldInfo>()

    internal operator fun get(name: String): FieldInfo? = fieldMap[name]

    init {
        initFieldMap()
    }

    /**
     * 初始化字段映射关系
     */
    private fun initFieldMap() {
        val reflection = KReflection(entity)
        reflection.getProperties().forEach { p ->
            val name = p.name
            val javaField = p.javaField!!.also { it.isAccessible = true }
            var label: String = name
            var type: FormItemType = FormItemType.EditText
            var required = false
            val value: String? = javaField.get(entity)?.toString()

            // 注解
            p.findAnnotation<FormField>()?.let { annotation ->
                label = annotation.value
                type = annotation.type
                required = annotation.required
            }

            fieldMap[name] = FieldInfo(
                javaField,
                label,
                type,
                p.returnType.withNullability(false).jvmErasure,
                required,
                value
            )

        }
    }

    /**
     * 设置表单实体字段值
     */
    fun setValue(name: String, value: String?): Boolean = runCatching {
        val field = fieldMap[name] ?: return false
        val javaField = field.field
        if (value == null) {
            // 空值设置
            javaField.set(entity, null)
            return true
        }
        when (field.returnType) {
            String::class -> javaField.set(entity, value.toString())
            Int::class -> javaField.set(entity, value.toInt())
            Long::class -> javaField.setLong(entity, value.toLong())
            Double::class -> javaField.setDouble(entity, value.toDouble())
            Float::class -> javaField.setFloat(entity, value.toFloat())
            Boolean::class -> javaField.setBoolean(entity, value.toBoolean())
            else -> javaField.set(entity, value)
        }
        true
    }.getOrDefault(false)

    override fun toString(): String {
        return "FormEntity: entity=${entity.javaClass.simpleName}"
    }

}
