package com.raedev.forms.app.entity

import com.raedev.forms.FormField
import com.raedev.forms.FormItemType

/**
 * 测试用实体类
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DemoEntity {

    @FormField("姓名", "基本信息", required = true)
    var name: String? = null

    @FormField("年龄", "基本信息", type = FormItemType.NumberEditText)
    var age: Int? = null

    @FormField("性别", group = "基本信息", dictCode = "common.sex")
    var sex: String? = null

}