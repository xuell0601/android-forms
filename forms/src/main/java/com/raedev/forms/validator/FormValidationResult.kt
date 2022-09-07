package com.raedev.forms.validator

import com.raedev.forms.items.FormItem

/**
 * 校验结果
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormValidationResult private constructor(
    /** 当前校验的表单项 */
    val formItem: FormItem?,
    /** 校验结果 */
    private val text: String?
) {

    companion object {

        fun success(formItem: FormItem? = null): FormValidationResult {
            return FormValidationResult(formItem, null)
        }

        fun error(formItem: FormItem, message: String): FormValidationResult {
            return FormValidationResult(formItem, message)
        }
    }

    /**
     * 是否校验成功
     */
    fun successfully(): Boolean {
        return text == null
    }

    /**
     * 校验错误时的错误消息
     */
    fun message(): String {
        return text!!
    }

}