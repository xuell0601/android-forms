package com.raedev.forms.internal

import com.raedev.forms.items.FormItem

/**
 * 表单列表
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class FormItemList : ArrayList<FormItem>() {

    /**
     * 转换为Map对象
     */
    fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        this.forEach {
            val value = it.value ?: return@forEach
            map[it.name] = value
        }
        return map
    }

    override fun add(element: FormItem): Boolean {
        // 不重复添加
        if (this.contains(element)) return false
        return super.add(element).also {
            updateIndex()
        }
    }

    override fun add(index: Int, element: FormItem) {
        // 不重复添加
        if (this.contains(element)) return
        super.add(index, element)
        updateIndex()
    }

    override fun remove(element: FormItem): Boolean {
        return super.remove(element).also {
            element.onFormItemRemoved()
            updateIndex()
        }
    }

    fun remove(name: String): Boolean {
        return get(name)?.let { this.remove(it) } ?: false
    }

    fun get(name: String): FormItem? {
        return this.find { it.name == name }
    }

    /**
     * 更新表单所在索引
     */
    private fun updateIndex() {
        this.forEachIndexed { index, formItem ->
            formItem.position = index
        }
    }

    /**
     * 找父亲的子节点
     */
    fun findChildrenIndex(parent: FormItem): Int {
        val parentIndex = indexOf(parent)
        // 表单组中没有这个父表单
        if (parentIndex < 0) return -1
        // 找到子节点：节点是一个个添加到父表单后面的，找到最后一个即可
        var childrenCount = 0
        this.forEachIndexed { index, formItem ->
            if (formItem.parent == parent) {
                childrenCount++
            }
        }

        return parentIndex + childrenCount
    }
}