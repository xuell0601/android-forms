package com.raedev.forms

import com.raedev.forms.render.FormRender

/**
 * 表单字段注解。
 * + 注意：注解只能帮助处理一些简单的逻辑，过于复杂的关联关系建议使用[FormRender]处理。
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

    /** 表单类型，默认是文本输入框 */
    val type: FormType = FormType.EditText,

    /**
     * 排序
     */
    val order: Int = 0,

    /** 字典Key */
    val dict: String = "",


    /** 父表单名称 */
    val parent: String = ""

)
