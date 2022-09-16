package com.raedev.forms.render

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroupAdapter
import com.raedev.forms.FormInputType
import com.raedev.forms.dict.FormDataProvider
import com.raedev.forms.internal.TopLinearSmoothScroller
import com.raedev.forms.items.*

/**
 * 表单渲染，我们建议业务代码继承FormRender进行对表单的渲染操作。
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
open class FormRender(
    private val context: Context,
    /** 表单组 */
    private val adapter: FormGroupAdapter,
    /** 表单项渲染需要依赖的FragmentManager */
    private val fragmentManager: FragmentManager? = null
) : IFormRender {

    protected companion object {
        const val TAG = "RAE.FormRender"
    }

    /** 表单组 */
    protected val formGroup = adapter.formGroup

    protected val smoothScroller by lazy { TopLinearSmoothScroller(context) }

    /**
     * 添加到表单组
     */
    protected fun <T : FormItem> T.addToFormGroup(
        parent: FormItem? = null,
        block: ((T) -> Unit)? = null
    ): T {
        block?.invoke(this)
        val rootFormGroup = this@FormRender.formGroup
        this.formGroup = rootFormGroup
        val binding = if (this@FormRender is IFormDataBinding) this@FormRender else null
        this.onFormRender(fragmentManager, binding)
        if (parent == null) rootFormGroup.addItem(this)
        else rootFormGroup.addChildren(parent, this)
        return this
    }

    /**
     * 分组标题栏
     */
    open fun addGroupTitle(label: String): GroupTitleFormItem {
        return GroupTitleFormItem(label).addToFormGroup()
    }

    /**
     * 添加文本编辑框
     */
    protected open fun addEditTextInternal(
        inputType: FormInputType,
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null,
    ): EditTextFormItem {
        return EditTextFormItem(inputType, label, name, value, required).addToFormGroup(parent)
    }


    /**
     * (子表单) 添加文本编辑框
     */
    open fun addEditTextChild(
        parent: FormItem,
        label: String, name: String, value: String? = null, required: Boolean = false,
    ): EditTextFormItem {
        return addEditText(label, name, value, required, parent)
    }


    /**
     * 添加文本编辑框
     */
    open fun addEditText(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null,
    ): EditTextFormItem {
        return addEditTextInternal(FormInputType.Text, label, name, value, required, parent)
    }


    /**
     * 添加文本编辑框
     */
    open fun addMultiEditText(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null,
    ): EditTextFormItem {
        return addEditTextInternal(FormInputType.MultiText, label, name, value, required)
    }


    /**
     * 数字编辑框
     */
    open fun addNumberEditText(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null
    ): EditTextFormItem {
        val inputType = when {
            label.isPhone() -> FormInputType.Phone
            label.isDecimalInput() -> FormInputType.Decimal
            else -> FormInputType.Number
        }
        return addEditTextInternal(inputType, label, name, value, required, parent)
    }

    /**
     * (子表单) 数字编辑框
     */
    open fun addNumberEditTextChild(
        parent: FormItem,
        label: String, name: String, value: String? = null, required: Boolean = false,
    ): EditTextFormItem {
        return addNumberEditText(label, name, value, required, parent)
    }


    /**
     * 添加单选框
     */
    open fun addRadioGroup(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null
    ): RadioGroupFormItem {
        return RadioGroupFormItem(label, name, value, required).addToFormGroup(parent)
    }

    /**
     * 添加单选框
     */
    open fun addRadioGroupChild(
        parent: FormItem,
        label: String, name: String, value: String? = null, required: Boolean = false,
    ): RadioGroupFormItem = addRadioGroup(label, name, value, required, parent)


    /**
     * 添加单选框
     */
    open fun addCheckBox(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null
    ): CheckBoxFormItem {
        return CheckBoxFormItem(label, name, value, required).addToFormGroup(parent)
    }

    /**
     * 日期选择框
     */
    open fun addDate(
        label: String, name: String, value: String? = null, required: Boolean = false,
        parent: FormItem? = null
    ): DateFormItem {
        return DateFormItem(label, name, value, required).addToFormGroup(parent)
    }


    /**
     * 选择框
     */
    open fun addSelect(
        provider: FormDataProvider,
        label: String, name: String, value: String? = null, required: Boolean = false,
        dialogTitle: String? = null
    ): SelectFormItem {
        return SelectFormItem(provider, label, name, value, required).addToFormGroup {
            it.dialogTitle = dialogTitle
        }
    }


    // region 辅助方法

    private fun String.isDecimalInput(): Boolean {
        return this.contains("面积") || this.contains("金额")
    }

    private fun String.isPhone(): Boolean {
        return this.contains("手机") || this.contains("电话")
    }

    // endregion


    // region IFormRender 接口实现

    /**
     * 执行渲染
     */
    override fun render() {
        formGroup.attachRender(this)
    }

    /**
     * 是否可以刷新列表
     */
    private fun canRefresh(): Boolean {
        val recyclerView = adapter.recyclerView ?: return false
        if (recyclerView.isComputingLayout) return false
        if (recyclerView.layoutManager?.isSmoothScrolling == true) return false
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNeedRefresh() {
        if (!canRefresh()) return
        adapter.notifyDataSetChanged()
    }

    override fun onFormItemInserted(insertItem: FormItem?, index: Int) {
        if (!canRefresh()) return
        adapter.notifyItemInserted(index)
    }

    override fun onFormItemRemoved(removeItem: FormItem?, index: Int, count: Int) {
        if (!canRefresh()) return
        if (count > 0) {
            adapter.notifyItemRangeRemoved(index, count)
        } else {
            adapter.notifyItemRemoved(index)
        }
    }

    override fun onFormItemUpdated(index: Int) {
        if (!canRefresh()) return
        adapter.notifyItemChanged(index)
    }

    override fun highlight(formItem: FormItem?) {
        formItem ?: return
        val recyclerView = adapter.recyclerView ?: return
        smoothScroller.scrollNow(recyclerView, formItem.position)
    }

    // endregion

}