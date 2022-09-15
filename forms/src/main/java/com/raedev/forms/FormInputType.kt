package com.raedev.forms

/**
 * 表单输入类型
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class FormInputType {

    /**
     * 文本
     */
    Text,


    /**
     * 长文本
     */
    MultiText,


    /**
     * 数字
     */
    Number,

    /**
     * 小数，默认为2位小数
     */
    Decimal,

    /**
     * 电话号码
     */
    Phone,
}