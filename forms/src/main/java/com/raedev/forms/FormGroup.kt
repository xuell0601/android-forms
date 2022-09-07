package com.raedev.forms

import com.raedev.forms.internal.FormAdapterProxy
import com.raedev.forms.internal.FormItemList
import com.raedev.forms.items.FormItem
import com.raedev.forms.validator.FormValidationResult
import java.lang.ref.WeakReference

/**
 * 表单组，用于管理所有的表单项
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class FormGroup {

    /** 表单项列表 */
    private val items = FormItemList()
    private var adapterRef: WeakReference<FormAdapterProxy>? = null

    /** 表单项数量 */
    val itemCount: Int
        get() = items.size


    /**
     * 根据索引获取表单项
     */
    operator fun get(position: Int): FormItem {
        return items[position]
    }

    /**
     * 添加到Adapter时调用
     */
    internal fun attachAdapter(formGroupAdapter: FormGroupAdapter) {
        adapterRef = WeakReference(formGroupAdapter)
    }

    /**
     * 从Adapter中移除时调用
     */
    internal fun detachAdapter() {
        items.clear()
        adapterRef?.clear()
        adapterRef = null
    }

    /**
     * 校验表单
     * @return 返回校验不通过的集合
     */
    fun validateToList(): List<FormValidationResult> {
        val result = mutableListOf<FormValidationResult>()
        items.forEach {
            val itemResult = it.formValidator?.validate(it, it.value)
            if (itemResult?.successfully() == false) {
                result.add(itemResult)
            }
        }
        return result
    }

    /**
     * 校验表单，若校验失败，返回第一个校验结果
     */
    fun validate(): FormValidationResult {
        items.forEach {
            val itemResult = it.formValidator?.validate(it, it.value)
            if (itemResult?.successfully() == false) return itemResult
        }
        return FormValidationResult.success()
    }


    // region 表单项操作

    /**
     * 根据表单名获取表单项
     */
    fun getItem(name: String): FormItem? = items.get(name)

    /**
     * 无重复添加表单项
     */
    fun addItem(item: FormItem): Boolean = items.add(item)

    /**
     * 移除表单项
     */
    fun removeItem(item: FormItem): Boolean = items.remove(item)

    /**
     * 根据表单名移除表单项
     */
    fun removeItem(name: String): Boolean = items.remove(name)

    /**
     * 将表单项转换为Map对象，如果是已经执行绑定，一般情况下是不需要调这个方法。
     */
    fun toMap(): Map<String, String> = items.toMap()

    /**
     * 刷新列表
     */
    fun refresh() {
        this.adapterRef?.get()?.onNeedRefresh()
    }

    // endregion
}