package com.raedev.forms.internal

import com.raedev.forms.items.FormItem

/**
 * 表单列表
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormItemList : ArrayList<FormItem>() {

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
        return super.add(element).also { updateIndex() }
    }

    fun remove(name: String): Boolean {
        return get(name)?.let { this.remove(it).also { updateIndex() } } ?: false
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
}