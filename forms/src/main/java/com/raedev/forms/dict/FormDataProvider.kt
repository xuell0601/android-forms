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
    protected val dataMap = mutableMapOf<String, FormSelectItem>()

    /** 路径分割符 */
    var pathSeparator: String = " / "

    /** 是允许选择父节点 */
    var enableCheckParent: Boolean = false

    /**
     * 添加数据集合
     */
    open fun addData(items: List<FormSelectItem>) {
        val dataList = mutableListOf<FormSelectItem>()
        items.sort().forEach { expandChildItems(dataList, it) }
        dataList.forEach { item -> dataMap[item.value] = item }
    }

    /**
     * 平铺子项
     */
    protected open fun expandChildItems(result: MutableList<FormSelectItem>, item: FormSelectItem) {
        result.add(item)
        if (item.hasChildren) {
            // 递归添加
            item.children!!.forEach {
                expandChildItems(result, it)
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
        return dataMap.values.filter { it.parent == null }
    }

    /**
     * 找子节点
     */
    open fun findChildren(item: FormSelectItem): List<FormSelectItem> {
        return dataMap.values.filter { it.parent == item }
    }

    /**
     * 找当前节点的所有父节点：me > [0] parent > [1] grandpa > [2] great-grandfather
     */
    open fun findParents(item: FormSelectItem, result: MutableList<FormSelectItem>) {
        val parent = item.parent ?: return
        result.add(parent)
        // 递归
        findParents(parent, result)
    }

    /**
     * 根据选择的项目加载列表
     */
    open fun getItems(selectItem: FormSelectItem? = null): List<FormSelectItem> {
        selectItem ?: return getRootItems()
        if (selectItem.parent == null) return getRootItems()
        val items = findChildren(selectItem)
        if (items.isEmpty()) {
            return selectItem.parent!!.children!!
        }
        return items
    }


    /**
     * 排序
     */
    protected open fun List<FormSelectItem>.sort(): List<FormSelectItem> {
        return this.sortedWith { a, b ->
            when {
                b.hasChildren -> 1
                a.hasChildren -> -1
                else -> 0
            }
        }
    }

    /**
     * 获取导航路径文本
     */
    open fun getNavigationTitle(item: FormSelectItem): String {
        val parentList = mutableListOf<FormSelectItem>()
        findParents(item, parentList)
        // 倒序
        parentList.reverse()
        val sb = StringBuilder()
        parentList.forEachIndexed { index, it ->
            if (index > 0) sb.append(pathSeparator)
            sb.append(it.label)
        }
        return sb.toString()
    }

    /**
     * 获取全路径展示的文本
     */
    open fun getFullLabel(item: FormSelectItem): String {
        val parent = getNavigationTitle(item)
        return if (parent.isEmpty()) item.label else "$parent$pathSeparator${item.label}"
    }
}