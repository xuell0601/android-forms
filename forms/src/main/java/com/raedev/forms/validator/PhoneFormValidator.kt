package com.raedev.forms.validator

import com.raedev.forms.items.FormItem
import java.util.regex.Pattern

/**
 * 手机号码必填项
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PhoneFormValidator : FormValidator() {

    companion object {
        val pattern: Pattern =
            Pattern.compile("^1(([3,5,8]\\d{9})|(4[5,7]\\d{8})|(7[0,6-8]\\d{8}))\$")
    }

    override fun validate(item: FormItem, value: String?): FormValidationResult {
        // 不是必填的时候不校验
        if (!item.required) return success(item)
        // 去除空格
        val text = value?.trim() ?: ""
        if (pattern.matcher(text).find()) return success(item)
        return error(item, "请输入正确的手机号码")
    }
}