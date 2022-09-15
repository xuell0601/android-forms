package com.raedev.forms.render

import com.raedev.forms.items.FormItem

/**
 * 表单渲染接口
 * @author RAE
 * @date 2022/09/13
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IFormRender {

    /**
     * 渲染
     */
    fun render()

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
     * 高亮当前表单
     */
    fun highlight(formItem: FormItem?)
}