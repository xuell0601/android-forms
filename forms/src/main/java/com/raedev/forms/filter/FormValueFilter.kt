package com.raedev.forms.filter

import com.raedev.forms.items.FormItem

/**
 * 表单值过滤器
 * 当用户在输入或者选择时，我们需要对这个值的正确性进行判断，如果是非法的不允许设置。
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormValueFilter {

    /**
     * 表单值过滤
     * @param value 用户输入的表单值，在更新FormItem的Value之前触发。
     * @return 过滤后的表单值
     */
    fun filter(item: FormItem, value: String?): String?
}