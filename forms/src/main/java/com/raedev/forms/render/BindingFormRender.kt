package com.raedev.forms.render

import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroup
import com.raedev.forms.FormItemType
import com.raedev.forms.internal.binder.FormEntityReference
import com.raedev.forms.items.EditTextFormItem
import com.raedev.forms.items.NumberEditTextFormItem

/**
 * 实体绑定的表单渲染
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class BindingFormRender(
    entity: Any,
    formGroup: FormGroup,
    fragmentManager: FragmentManager? = null
) : FormRender(formGroup, fragmentManager) {

    /** 解析后的实体 */
    private val formEntity: FormEntityReference by lazy {
        FormEntityReference(entity)
    }

    override fun onRender() {
        formEntity.fieldMap.forEach {
            val name = it.key
            val fieldInfo = it.value
            val label = fieldInfo.label
            val value = fieldInfo.value
            // 创建表单
            val item = when (fieldInfo.type) {
                FormItemType.EditText -> EditTextFormItem(label, name, value)
                FormItemType.NumberEditText -> NumberEditTextFormItem(label, name, value)
            }
            item.formEntity = formEntity
            item.required = fieldInfo.required
            formGroup.addItem(item)
        }
    }


}