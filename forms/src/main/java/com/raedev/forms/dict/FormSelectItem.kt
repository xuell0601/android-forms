package com.raedev.forms.dict

/**
 * 字典节点
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class FormSelectItem(
    /** 字典显示名称 */
    var label: String,
    /** 字典值 */
    var value: String,
    /** 字典名称 */
    var name: String? = null,
    /** 排序 */
    var order: Int = -1
) {


    /** 父节点 */
    internal var parent: FormSelectItem? = null

    /** 子节点列表 */
    internal var children: MutableList<FormSelectItem>? = null

    /** 是否有子节点 */
    internal val hasChildren: Boolean
        get() = !children.isNullOrEmpty()

    /** 其他数据 */
    private var bundle: MutableMap<String, String>? = null

    /**
     * 添加子节点
     */
    fun addChild(child: FormSelectItem) {
        val list = children ?: mutableListOf<FormSelectItem>().apply { children = this }
        child.parent = this
        list.add(child)
        // 重新排序
        list.sortWith { a, b ->
            when {
                a.hasChildren -> -1
                b.hasChildren -> 1
                else -> 0
            }
        }
    }

    fun putString(name: String, value: String) {
        val map = bundle ?: mutableMapOf<String, String>().apply { bundle = this }
        map[name] = value
    }

    fun getString(name: String): String? {
        return bundle?.get(name)
    }

    override fun toString(): String {
        return "FormDictItem: $label=$value, name=$name, order=$order"
    }


}