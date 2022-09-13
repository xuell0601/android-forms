package com.raedev.forms.app.entity

import com.raedev.forms.FormField

/**
 * 测试用实体类
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DemoEntity {

    @FormField("姓名（必填）", "基本信息", required = true, order = 1)
    var name: String? = null

    @FormField("年龄", "基本信息")
    var age: Int? = null

    @FormField("性别", group = "基本信息", dictCode = "common.sex")
    var sex: String? = null

    @FormField("房屋面积", group = "基本信息", unit = "㎡")
    var area: String? = null

    @FormField("我是很长很长很长很长很长很长很长的表单")
    var longText: String? = null

}