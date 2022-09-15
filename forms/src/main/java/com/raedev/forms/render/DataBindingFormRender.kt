package com.raedev.forms.render

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroupAdapter
import com.raedev.forms.FormType
import com.raedev.forms.internal.binder.FormEntityReference
import com.raedev.forms.items.FormItem
import kotlin.reflect.KClass

/**
 * 实体绑定的表单渲染
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class DataBindingFormRender(
    context: Context,
    adapter: FormGroupAdapter,
    fragmentManager: FragmentManager? = null
) : FormRender(context, adapter, fragmentManager), IFormDataBinding {

    lateinit var entity: Any
        private set

    /** 解析后的实体 */
    private lateinit var reference: FormEntityReference

    override fun render() {
        super.render()
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
            item.addToFormGroup()
        }
    }

    private class AutoType(val labels: List<String>, val types: List<KClass<*>> = emptyList()) {
        fun match(label: String, type: KClass<*>? = null): Boolean {
            val count = labels.count { label.contains(it) }
            if (type != null && count == 0) return types.contains(type)
            return count > 0
        }
    }

    /** 数字类型 */
    private val numberTypes by lazy {
        AutoType(
            listOf("面积", "元", "金额", "费用"),
            listOf(Int::class, Long::class, Float::class, Double::class)
        )
    }

    /** 日期类型 */
    private val dateTypes by lazy { AutoType(listOf("时间", "日期", "截止")) }

    /** 单选类型 */
    private val radioTypes by lazy {
        AutoType(listOf("是否"))
    }


    /**
     * 创建表单项
     */
    private fun createFormItem(
        type: FormType,
        label: String,
        name: String,
        value: String?,
        required: Boolean = false,
        returnType: KClass<*>? = null
    ): FormItem {
        return when (type) {
            FormType.EditText -> addEditText(label, name, value, required)
            FormType.NumberEditText -> addNumberEditText(label, name, value, required)
            FormType.RadioGroup -> addRadioGroup(label, name, value, required)
            else -> autoCreate(label, name, value, required, returnType)
        }
    }

    /**
     * 自动推断类型
     */
    protected open fun autoCreate(
        label: String,
        name: String,
        value: String?,
        required: Boolean = false,
        returnType: KClass<*>? = null
    ): FormItem {
        return when {
            numberTypes.match(label, returnType) -> {
                addNumberEditText(label, name, value, required)
            }
            radioTypes.match(label, returnType) -> {
                // TODO 单选类型
                addNumberEditText(label, name, value, required)
            }
            dateTypes.match(label, returnType) -> {
                // TODO 日期类型
                addEditText(label, name, value, required)
            }
            else -> addEditText(label, name, value, required)
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