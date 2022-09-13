package com.raedev.forms.render

import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroup
import com.raedev.forms.FormItemType
import com.raedev.forms.items.EditTextFormItem
import com.raedev.forms.items.FormItem
import com.raedev.forms.items.GroupTitleFormItem
import com.raedev.forms.items.NumberEditTextFormItem
import kotlin.reflect.KClass

/**
 * 表单渲染，我们建议业务代码继承FormRender进行对表单的渲染操作。
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class FormRender(
    /** 表单组 */
    private val formGroup: FormGroup,
    /** 表单项渲染需要依赖的FragmentManager */
    private val fragmentManager: FragmentManager? = null
) {

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
     * 执行渲染，由子类实现
     */
    open fun render() {
        throw IllegalAccessException("子类不需要调用父类的render渲染方法。")
    }

    /**
     * 添加到表单组
     */
    fun addItem(formItem: FormItem): Boolean {
        val result = formGroup.addItem(formItem)
        notifyFormItemAdded(formItem)
        return result
    }

    /**
     * 添加到子表单中
     */
    protected fun addChildren(parent: FormItem, item: FormItem) {
        formGroup.addChildren(parent, item)
        notifyFormItemAdded(item)
    }

    private fun notifyFormItemAdded(formItem: FormItem) {
        val dataBinding: IFormDataBinding? = if (this is IFormDataBinding) this else null
        formItem.onFormItemAdded(
            formGroup.indexOf(formItem),
            formGroup,
            fragmentManager,
            dataBinding
        )
    }

    /**
     * 创建表单项
     */
    fun createFormItem(
        type: FormItemType,
        label: String,
        name: String,
        value: String?,
        required: Boolean = false,
        returnType: KClass<*>? = null
    ): FormItem {
        return when (type) {
            FormItemType.EditText -> newEditText(label, name, value, required)
            FormItemType.NumberEditText -> newNumberEditText(label, name, value, required)
            else -> autoCreateFormItem(label, name, value, required, returnType)
        }
    }

    /**
     * 自动推断类型
     */
    protected open fun autoCreateFormItem(
        label: String,
        name: String,
        value: String?,
        required: Boolean = false,
        returnType: KClass<*>? = null
    ): FormItem {
        return when {
            numberTypes.match(label, returnType) -> {
                newNumberEditText(label, name, value, required)
            }
            radioTypes.match(label, returnType) -> {
                // TODO 单选类型
                newNumberEditText(label, name, value, required)
            }
            dateTypes.match(label, returnType) -> {
                // TODO 日期类型
                newEditText(label, name, value, required)
            }
            else -> newEditText(label, name, value, required)
        }
    }


    /**
     * 添加文本编辑框
     */
    fun newEditText(
        label: String,
        name: String,
        value: String?,
        required: Boolean = false
    ): EditTextFormItem {
        return EditTextFormItem(label, name, value).apply { this.required = required }
    }

    /**
     * 数字编辑框
     */
    fun newNumberEditText(
        label: String,
        name: String,
        value: String?,
        required: Boolean = false,
        unit: String? = null
    ): NumberEditTextFormItem {
        return NumberEditTextFormItem(label, name, value).apply {
            this.required = required
            this.unit = unit
        }
    }

    /**
     * 组标题栏
     */
    fun newGroupTitle(label: String): GroupTitleFormItem {
        return GroupTitleFormItem(label)
    }

}