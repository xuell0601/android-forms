//package com.raedev.forms.items
//
///**
// * FormItem组合，可以将若干个FormItem关联在一起。
// * @author RAE
// * @date 2022/09/02
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//class GroupFormItem(label: String, name: String, value: String?) : FormItem(label, name, value) {
//
//    override val formType: Int
//        get() = 1
//
//    private val children = mutableListOf<FormItem>()
//
//    val childrenCount: Int
//        get() = children.count()
//
//    /**
//     * 添加子表单
//     */
//    fun addChildren(item: FormItem): Boolean {
//        if (children.contains(item)) return false
//        return children.add(item)
//    }
//
//    /**
//     * 移除子表单
//     */
//    fun removeChildren(item: FormItem) {
//        item.onDetached()
//        children.remove(item)
//    }
//
//    /**
//     * 移除所有子表单
//     */
//    fun removeAll() {
//        children.forEach { it.onDetached() }
//        children.clear()
//    }
//
//
//}