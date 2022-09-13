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


    /** 最大标题宽度，由布局管理器计算后赋值 */
    internal var maxTitleWidth: Int = -1

    /** 表单项列表 */
    private val items = FormItemList()

    /** RecycleView Adapter 有没有并不会影响到表单组的正常功能 */
    private var adapterRef: WeakReference<FormAdapterProxy>? = null

    private val adapter: FormAdapterProxy?
        get() = adapterRef?.get()

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

    var viewonly: Boolean = false
        set(value) {
            field = value
            items.forEach { it.viewonly = value }
            refresh()
        }

    // region 表单项操作

    /**
     * 表单所在位置
     */
    fun indexOf(formItem: FormItem): Int = items.indexOf(formItem)

    /**
     * 根据表单名获取表单项
     */
    fun getItem(name: String): FormItem? {
        return items.get(name)
    }

    /**
     * 无重复添加表单项
     */
    fun addItem(item: FormItem): Boolean = items.add(item).also {
        adapter?.onFormItemInserted(item, indexOf(item))
    }

    /**
     * 移除表单项
     */
    fun removeItem(item: FormItem): Boolean = items.remove(item).also {
        adapter?.onFormItemRemoved(item, indexOf(item), 1)
    }

    /**
     * 根据表单名移除表单项
     */
    fun removeItem(name: String): Boolean {
        return getItem(name)?.let { this.removeItem(it) } ?: false
    }


    /**
     * 将表单项转换为Map对象，如果是已经执行绑定，一般情况下是不需要调这个方法。
     */
    fun toMap(): Map<String, String> = items.toMap()

    /**
     * 刷新列表
     */
    fun refresh() {
        // TODO 刷新慢
        this.adapter?.onNeedRefresh()
    }

    /**
     * 刷新单个表单项
     */
    fun refreshItem(formItem: FormItem) {
        this.adapter?.onFormItemUpdated(indexOf(formItem))
    }

    /**
     * 只刷新可见的表单项
     */
    fun refreshVisibleItems() {
        this.adapter?.refreshVisibleItems()
    }

    /**
     * 添加子表单
     * @param parentName 父表单名称
     * @param item 子表单
     */
    fun addChildren(parentName: String, item: FormItem) {
        val parent = getItem(parentName) ?: throw NullPointerException("表单组中找不到父表单$parentName")
        this.addChildren(parent, item)
    }

    /**
     * 添加子表单
     * @param parent 父表单
     * @param item 子表单
     */
    @Throws(IllegalStateException::class)
    fun addChildren(parent: FormItem, item: FormItem) {
        val index = items.findChildrenIndex(parent)
        if (index < 0) throw IllegalStateException("表单组中找不到父表单${parent.label}")
        item.parent = parent
        val childrenIndex = index + 1
        items.add(childrenIndex, item)
        this.adapter?.onFormItemInserted(item, childrenIndex)
    }

    /**
     * 移除父表单下的所有子表单
     * @param parentName 父表单名称
     */
    fun removeAllChildren(parentName: String) {
        val parent = getItem(parentName) ?: throw NullPointerException("表单组中找不到父表单$parentName")
        removeAllChildren(parent)
    }

    /**
     * 移除父表单下的所有子表单
     * @param parent 父表单
     */
    fun removeAllChildren(parent: FormItem) {
        val removeList = mutableListOf<FormItem>()
        items.forEach {
            if (it.parent == parent) {
                removeList.add(it)
            }
        }
        if (removeList.size > 0) {
            items.removeAll(removeList.toSet())
            adapter?.onFormItemRemoved(null, indexOf(parent), removeList.size)
        }
    }

    // endregion
}