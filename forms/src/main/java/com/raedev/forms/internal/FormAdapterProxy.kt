package com.raedev.forms.internal

import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.items.FormItem

/**
 *
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormAdapterProxy {

    /**
     * 获取 RecyclerView
     * @return RecyclerView
     */
    fun getRecyclerView(): RecyclerView?

    /**
     * 需要刷新列表
     */
    fun onNeedRefresh()

    /**
     * 当表单添加的时候回调
     * @param insertItem 已经插入的表单项
     * @param index 所在索引
     */
    fun onFormItemInserted(insertItem: FormItem?, index: Int)

    /**
     * 当表单项已经移除的时候回调
     * @param removeItem 已移除的表单项
     * @param index 所在索引
     * @param count 删除的个数
     */
    fun onFormItemRemoved(removeItem: FormItem?, index: Int, count: Int)

    /**
     * 更新单个表单
     */
    fun onFormItemUpdated(index: Int)

    /**
     * 表单发生错误，一般忽略即可，也可以打Log看日志
     * @param message 错误信息
     */
    fun onFormError(message: String)

    /**
     * 刷新可见的表单项
     */
    fun refreshVisibleItems()
}