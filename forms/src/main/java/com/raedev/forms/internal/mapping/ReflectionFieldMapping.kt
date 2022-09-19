package com.raedev.forms.internal.mapping

import com.raedev.forms.FormType

/**
 * 通过反射映射
 * @author RAE
 * @date 2022/09/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class ReflectionFieldMapping : FormFieldMapping() {

    override fun convertToMap(field: FieldItem): FormFieldMap {
        return FormFieldMap(
            field.javaField,
            field.annotation?.type ?: FormType.EditText,
            field.name,
            field.annotation?.value ?: field.name,
            field.annotation?.required ?: false,
            field.annotation?.order ?: 0,
            field.annotation?.group ?: "",
            field.annotation?.dict ?: "",
            field.annotation?.parent ?: ""
        )
    }

}