package com.raedev.forms.internal

import com.raedev.forms.items.FormItem

/**
 * 表单列表，封装对表单项列表的增删改查操作。
 * 这里对列表的操作都不会进行回调通知，而是在外部执行。
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class FormItemList : Iterable<FormItem> {

    /** 原始列表 */
    private val items = mutableListOf<FormItem>()

    /** 表单数量 */
    val size: Int
        get() = items.size

    /** 获取索引所在的表单 */
    operator fun get(position: Int): FormItem = items[position]

    /** 根据表单名获取表单 */
    operator fun get(name: String): FormItem? = items.find { it.name == name }

    /** 迭代器 */
    override fun iterator(): Iterator<FormItem> = items.iterator()

    /** 移除所有 */
    fun removeAll(set: Set<FormItem>) = items.removeAll(set)

    /** 移除表单 */
    fun remove(element: FormItem): Boolean = items.remove(element)

    /** 表单所在索引 */
    fun indexOf(item: FormItem): Int = items.indexOf(item)


    /** 清除所有表单项 */
    fun clear() = items.clear()

    /**
     * 根据表单名移除表单
     */
    fun remove(name: String): Boolean {
        return get(name)?.let { this.remove(it) } ?: false
    }


    /**
     * 添加表单，重复添加表单返回false
     */
    fun add(element: FormItem): Boolean = add(-1, element)

    /**
     * 添加表单到指定的位置
     */
    fun add(index: Int, element: FormItem): Boolean {
        // 因为FormItem重写equals方法，这里可以直接使用contains进行判断
        if (items.contains(element)) return false
        if (index == -1) return items.add(element)
        items.add(index, element)
        return true
    }


    /**
     * 找父表单中最后一个子表单所在的索引
     */
    fun findChildrenIndex(parent: FormItem): Int {
        val parentIndex = items.indexOf(parent)
        // 表单组中没有这个父表单
        if (parentIndex < 0) return -1
        // 找到子节点：节点是一个个添加到父表单后面的，找到最后一个即可
        var childrenCount = 0
        items.forEach { formItem ->
            if (formItem.parent == parent) {
                childrenCount++
            }
        }
        return parentIndex + childrenCount
    }

    /**
     * 转换为Map对象
     */
    fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        items.forEach { item -> item.value?.let { map[item.name] = it } }
        return map
    }

    /**
     * 释放资源
     */
    fun destroy() {
        items.clear()
    }
}