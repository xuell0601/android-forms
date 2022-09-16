package com.raedev.forms

/**
 * 支持的表单类型
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class FormType {

    /**
     * 分组标题
     */
    GroupTitle,

    /**
     * 文本框
     */
    EditText,

    /**
     * 数字文本框
     */
    NumberEditText,

    /**
     * 单选框
     */
    RadioGroup,

    /**
     * 复选框
     */
    CheckBox,

    /**
     * 日期选择
     */
    Date,

    /**
     * 选择框
     */
    Select,
}