package com.raedev.forms

import com.raedev.forms.internal.FormItemList
import com.raedev.forms.items.FormItem
import com.raedev.forms.render.FormRender
import com.raedev.forms.render.IFormRender
import com.raedev.forms.validator.FormValidationResult

/**
 * 这是一个表单组用于管理所有的表单项，表单的渲染依赖[IFormRender]
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

    /** 表单项数量 */
    val itemCount: Int
        get() = items.size

    /** 设置表单项只读 */
    var viewonly: Boolean = false
        set(value) {
            field = value
            items.forEach { it.viewonly = value }
            refresh()
        }

    /** 表单渲染接口 */
    private var render: IFormRender? = null

    // region 内部调用方法

    /**
     * 添加到Adapter时调用
     */
    internal fun attachRender(render: FormRender) {
        this.render = render
    }

    /**
     * 从Adapter中移除时调用
     */
    internal fun destroy() {
        // 通知表单释放资源
        items.forEach { it.onFormItemRemoved() }
        items.destroy()
    }

    // endregion

    // region 表单校验

    /**
     * 校验表单
     * @return 返回校验不通过的集合
     */
    fun validateFormAsList(): List<FormValidationResult> {
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
    fun validateForm(highlight: Boolean = true): FormValidationResult {
        items.forEach {
            val itemResult = it.formValidator?.validate(it, it.value)
            if (itemResult?.successfully() == false) {
                // 高亮显示
                if (highlight) render?.highlight(itemResult.formItem)
                return itemResult
            }
        }
        return FormValidationResult.success()
    }

    // endregion


    // region 表单项操作

    /**
     * 根据索引获取表单项
     */
    operator fun get(position: Int): FormItem = items[position]

    /**
     * 根据表单名获取表单
     */
    operator fun get(name: String): FormItem? = items[name]

    /**
     * 表单所在位置
     */
    fun indexOf(formItem: FormItem): Int = items.indexOf(formItem)

    /**
     * 添加表单项，如果表单存在则不会添加返回false
     */
    fun addItem(item: FormItem): Boolean = addItem(-1, item)

    /**
     * 添加表单项，如果表单存在则不会添加返回false
     * @param index 插入的索引
     * @param item 表单项
     */
    fun addItem(index: Int, item: FormItem): Boolean {
        return items.add(index, item).also {
            if (!it) return it
            // 通知刷新
            val indexOf = indexOf(item)
            item.onFormItemAdded(indexOf, this)
            render?.onFormItemInserted(item, indexOf)
        }
    }

    /**
     * 根据表单名移除表单项
     */
    fun removeItem(name: String): Boolean = items[name]?.let { this.removeItem(it) } ?: false

    /**
     * 移除表单项
     */
    fun removeItem(item: FormItem): Boolean = items.remove(item).also {
        // 通知移除
        item.onFormItemRemoved()
        render?.onFormItemRemoved(item, indexOf(item), 1)
    }


    /**
     * 添加子表单
     * @param parentName 父表单名称
     * @param item 子表单
     */
    fun addChildren(parentName: String, item: FormItem): Boolean {
        return items[parentName]?.let { parent -> this.addChildren(parent, item) } ?: false
    }

    /**
     * 添加子表单
     * @param parent 父表单
     * @param item 子表单
     */
    @Throws(IllegalStateException::class)
    fun addChildren(parent: FormItem, item: FormItem): Boolean {
        val index = items.findChildrenIndex(parent)
        if (index < 0) return false
        // 关联父表单
        item.parent = parent
        return addItem(index + 1, item)
    }

    /**
     * 移除父表单下的所有子表单
     * @param parentName 父表单名称
     */
    fun removeAllChildren(parentName: String) {
        items[parentName]?.let { parent -> removeAllChildren(parent) }
    }

    /**
     * 移除父表单下的所有子表单
     * @param parent 父表单
     */
    fun removeAllChildren(parent: FormItem) {
        // 移除父表单列表
        val removeList = mutableListOf<FormItem>()
        searchRemoveList(parent, removeList)
        if (removeList.isNotEmpty()) {
            // 通知移除
            removeList.forEach { it.onFormItemRemoved() }
            // 正在移除
            items.removeAll(removeList.toSet())
            render?.onFormItemRemoved(null, indexOf(parent) + 1, removeList.size)
        }
    }

    /**
     * 根据父表单查找需要移除的表单
     */
    private fun searchRemoveList(parent: FormItem?, result: MutableList<FormItem>) {
        if (parent == null) return
        items.forEach {
            if (it.parent != parent) return@forEach
            result.add(it)
            // 递归找有没有当前表单
            searchRemoveList(it, result)
        }
    }

    /**
     * 刷新列表
     */
    fun refresh() {
        this.render?.onNeedRefresh()
    }

    /**
     * 刷新单个表单项
     */
    fun refreshItem(formItem: FormItem) {
        this.render?.onFormItemUpdated(indexOf(formItem))
    }

    /**
     * 将表单项转换为Map对象，如果是已经执行绑定，一般情况下是不需要调这个方法。
     */
    fun toMap(): Map<String, String> = items.toMap()

    // endregion
}