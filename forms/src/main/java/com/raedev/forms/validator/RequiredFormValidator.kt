package com.raedev.forms.validator

import com.raedev.forms.items.FormItem

/**
 * 必填项校验
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class RequiredFormValidator(private val validator: FormValidator? = null) : FormValidator() {

    override fun validate(item: FormItem, value: String?): FormValidationResult {
        val text = value?.trim() // 去除空格
        if (text.isNullOrEmpty()) return error(item, "${item.label}不能为空")

        // 装饰者模式
        val result = validator?.validate(item, value)
        if (result?.successfully() == false) return result

        return success(item)
    }
}