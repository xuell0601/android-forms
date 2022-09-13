package com.raedev.forms.render

import com.raedev.forms.items.FormItem

/**
 * 表单数据绑定
 * @author RAE
 * @date 2022/09/08
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IFormDataBinding {
    /**
     * 绑定实体
     */
    fun bindEntity(entity: Any)

    /**
     * 解除绑定
     */
    fun unbind()

    /**
     * 更新实体值
     * @param formItem 表单项
     */
    fun updateValue(formItem: FormItem)
}