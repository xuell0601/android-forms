package com.raedev.forms.listener

import com.raedev.forms.items.FormItem

/**
 * 表单改变监听器
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormChangedListener {

    /**
     * 表单发生改变回调
     * @param item 当前表单
     * @param value 当前表单的值
     * @param old 当前表单改变之前的值
     */
    fun onValueChanged(item: FormItem, value: String?, old: String?)
}