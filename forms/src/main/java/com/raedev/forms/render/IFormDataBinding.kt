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
    fun bind(entity: Any)

    /**
     * 解除绑定
     */
    fun unbind()

    /**
     * 更新实体值
     * @param formItem 表单项
     */
    fun setFormValue(formItem: FormItem)

    /**
     * 转换为Map对象
     */
    fun toMap(): Map<String, String?>

    /**
     * 转换为对象JSON
     */
    fun toJson(): String

}