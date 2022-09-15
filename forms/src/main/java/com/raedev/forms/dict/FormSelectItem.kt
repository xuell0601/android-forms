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
    val value: String,
    /** 字典名称 */
    val name: String? = null,
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

    /**
     * 添加子节点
     */
    fun addChild(child: FormSelectItem) {
        children ?: mutableListOf<FormSelectItem>().apply { children = this }
        child.parent = this
        children!!.add(child)
    }

    override fun toString(): String {
        return "FormDictItem: $label=$value, name=$name, order=$order"
    }

}