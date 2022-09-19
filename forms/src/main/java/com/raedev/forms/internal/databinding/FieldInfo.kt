package com.raedev.forms.internal.databinding

import com.raedev.forms.FormField
import java.lang.reflect.Field
import kotlin.reflect.KClass

internal data class FieldInfo(
    val field: Field,
    val formField: FormField,
    var value: String?,
    val returnType: KClass<*>,
//    val label: String,
//    val type: FormItemType,
//    val required: Boolean,
//    var unit: String?,
)