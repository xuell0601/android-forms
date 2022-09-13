package com.raedev.forms

/**
 * 表单字段注解
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FormField(

    /** 字段标题 */
    val value: String,

    /** 分组名称 */
    val group: String = "",

    /** 是否必填 */
    val required: Boolean = false,

    /** 关联字典值 */
    val dictCode: String = "",

    /** 表单类型，默认是文本输入框 */
    val type: FormItemType = FormItemType.Auto,

    /**
     * 取值单位
     */
    val unit: String = "",

    /**
     * 排序
     */
    val order: Int = 0
)
