package com.raedev.forms.app.entity

import com.raedev.forms.FormField
import com.raedev.forms.FormType

/**
 * 测试用实体类
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DemoEntity {

    @FormField("姓名（必填）", "基本信息", required = true, order = 1)
    var name: String? = null

    @FormField("年龄", "基本信息", order = 2, type = FormType.NumberEditText)
    var age: Int? = null

    @FormField("性别", group = "基本信息", order = 3, type = FormType.RadioGroup)
    var sex: Int? = null

    @FormField("是否应届生", group = "基本信息", order = 4, type = FormType.CheckBox)
    var sfbd: Int? = null

    @FormField("出生日期", group = "基本信息", order = 5, type = FormType.Date)
    var birthday: String? = null

    @FormField("工作地点", group = "基本信息", order = 6, type = FormType.Select, dict = "WORK")
    var workCity: String? = null

    @FormField(
        "测试选项",
        group = "基本信息",
        order = 7,
        type = FormType.Select,
        dict = "TEST",
        required = true
    )
    var testSelection: String? = null

    @FormField("上一家工作地点", group = "基本信息", order = 8, type = FormType.Select, dict = "WORK")
    var lastWorkCity: String? = null

    @FormField("房屋面积", group = "房产信息", type = FormType.NumberEditText)
    var area: String? = null

    @FormField("默认表单")
    var longText: String? = null

}