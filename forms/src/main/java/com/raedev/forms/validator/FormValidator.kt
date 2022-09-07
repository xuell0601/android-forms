package com.raedev.forms.validator

import com.raedev.forms.items.FormItem

/**
 * 表单校验器
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FormValidator {


    /**
     * 校验表单，当前实现
     */
    abstract fun validate(item: FormItem, value: String?): FormValidationResult

    /**
     * 校验成功
     */
    protected fun success(formItem: FormItem): FormValidationResult {
        return FormValidationResult.success(formItem)
    }

    /**
     * 校验失败
     */
    protected fun error(formItem: FormItem, message: String): FormValidationResult {
        return FormValidationResult.error(formItem, message)
    }
}