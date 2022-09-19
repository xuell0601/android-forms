package com.raedev.forms.internal.mapping

import com.raedev.forms.FormField
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * 字段
 * @author RAE
 * @date 2022/09/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FieldItem(
    /** Java 字段 */
    val javaField: Field,
    /** 字段名 */
    val name: String,
    /** 字段返回类型 */
    val returnType: Type,
    /** 表单字段注解 */
    val annotation: FormField? = null
)