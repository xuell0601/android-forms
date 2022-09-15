package com.raedev.forms.dict

/**
 * 表单数据提供
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class FormDataProvider {

    /** 字典值为Key的数据列表 */
    private val dataMap = mutableMapOf<String, FormSelectItem>()

    /**
     * 添加数据集合
     */
    open fun addData(items: List<FormSelectItem>) {
        val dataList = mutableListOf<FormSelectItem>()
        for (item in items) {
            loadItems(dataList, item)
        }
        dataList.forEach { item ->
            dataMap[item.value] = item
        }
    }

    private fun loadItems(result: MutableList<FormSelectItem>, item: FormSelectItem) {
        result.add(item)
        if (item.hasChildren) {
            // 递归添加
            item.children!!.forEach {
                loadItems(result, it)
            }
        }
    }

    /**
     * 根据字典值获取子项
     */
    open fun getItem(value: String): FormSelectItem? = dataMap[value]

    /**
     * 根据字典值获取标题
     */
    open fun getItemLabel(value: String): String? = getItem(value)?.label


    /**
     * 获取根节点的列表
     */
    protected open fun getRootItems(): List<FormSelectItem> {
        return dataMap.values.filter { it.parent == null }.sort()
    }

    /**
     * 找子节点
     */
    open fun findChildren(item: FormSelectItem): List<FormSelectItem> {
        return dataMap.values.filter { it.parent == item }
    }

    /**
     * 根据选择的项目加载列表
     */
    open fun getItems(selectItem: FormSelectItem?): List<FormSelectItem> {
        selectItem ?: return getRootItems()
        if (selectItem.parent == null) return getRootItems()
        val items = findChildren(selectItem)
        if (items.isEmpty()) {
            return selectItem.parent!!.children!!
        }
        return items.sort()
    }


    private fun List<FormSelectItem>.sort(): List<FormSelectItem> {
        return this.sortedWith { a, b ->
            val defaultOrder = when {
                // 有排序的优先
                a.order != -1 && b.order != -1 -> a.order - b.order
                else -> a.value.compareTo(b.value)
            }
            when {
                a.hasChildren && b.hasChildren -> defaultOrder
                a.hasChildren -> -1
                b.hasChildren -> 1
                // 有排序的优先
                a.order != -1 && b.order != -1 -> a.order - b.order
                else -> defaultOrder
            }
        }
    }

    open fun getNavigationTitle(item: FormSelectItem): String {
        return appendParentTitle(
            generateNavigationTitle(if (item.parent == null) item.label else null),
            item.parent
        )
    }


    private fun appendParentTitle(title: String, parent: FormSelectItem?): String {
        if (parent == null) return title
        val currentTitle = generateNavigationTitle(parent.label)
        if (parent.parent != null) return appendParentTitle(currentTitle, parent.parent)
        return currentTitle
    }

    protected open fun generateNavigationTitle(title: String?): String {
        return title?.let { " / $title" } ?: ""
    }
}