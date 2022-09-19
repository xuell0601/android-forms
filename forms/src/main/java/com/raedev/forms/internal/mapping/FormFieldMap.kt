package com.raedev.forms.internal.mapping

import com.raedev.forms.FormType
import java.lang.ref.SoftReference
import java.lang.reflect.Field

/**
 * 表单字段实体类映射表
 * @author RAE
 * @date 2022/09/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormFieldMap(

    /** Java字段 */
    val field: Field,

    /** 表单类型 */
    val formType: FormType,

    /** 字段名 */
    val name: String,

    /** 字段显示名称 */
    val label: String,

    /** 是否必填 */
    val required: Boolean = false,

    /** 字段排序 */
    val order: Int = 0,

    /** 字段分组 */
    val group: String = "",

    val dict: String? = "",

    /** 父表单名称 */
    val parentName: String? = null

) {
    /** 表单返回值类型 */
    val returnType: Class<*> = field.type

    internal var objRef: SoftReference<Any>? = null

    /** 反射获取实体字段值 */
    val value: Any?
        get() {
            val obj = objRef?.get() ?: return null
            if (!this.field.isAccessible) this.field.isAccessible = true
            return this.field.get(obj)
        }

    /**
     * 清除缓存
     */
    fun clear() {
        objRef?.clear()
        objRef = null
    }
}