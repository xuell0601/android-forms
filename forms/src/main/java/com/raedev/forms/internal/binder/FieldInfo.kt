package com.raedev.forms.internal.binder

import com.raedev.forms.FormItemType
import java.lang.reflect.Field
import kotlin.reflect.KClass

internal data class FieldInfo(
    val field: Field,
    val label: String,
    val type: FormItemType,
    val returnType: KClass<*>,
    val required: Boolean,
    var value: String?,
)