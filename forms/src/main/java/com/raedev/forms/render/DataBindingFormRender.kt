package com.raedev.forms.render

import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroup
import com.raedev.forms.internal.binder.FormEntityReference
import com.raedev.forms.items.FormItem
import com.raedev.forms.items.NumberEditTextFormItem

/**
 * 实体绑定的表单渲染
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class DataBindingFormRender(
    formGroup: FormGroup,
    fragmentManager: FragmentManager? = null
) : FormRender(formGroup, fragmentManager), IFormDataBinding {

    lateinit var entity: Any
        private set

    /** 解析后的实体 */
    private lateinit var reference: FormEntityReference

    override fun render() {
        if (!this::reference.isInitialized) throw NullPointerException("请先对表单实体进行bindEntity(entity)绑定")
        reference.fieldMap.forEach {
            val name = it.key
            val fieldInfo = it.value
            val label = fieldInfo.formField.value
            val value = fieldInfo.value
            // 创建表单
            val item = createFormItem(
                fieldInfo.formField.type,
                label,
                name,
                value,
                fieldInfo.formField.required,
                fieldInfo.returnType
            )
            when (item) {
                is NumberEditTextFormItem -> {
                    item.unit = fieldInfo.formField.unit
                }
            }
            addItem(item)
        }
    }

    override fun bindEntity(entity: Any) {
        this.entity = entity
        this.reference = FormEntityReference(entity)
    }

    override fun unbind() {
        reference.clean()
    }

    override fun updateValue(formItem: FormItem) {
        this.reference.setValue(formItem.name, formItem.value)
    }

}