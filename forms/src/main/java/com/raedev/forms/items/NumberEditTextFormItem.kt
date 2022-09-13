package com.raedev.forms.items

import android.view.inputmethod.EditorInfo
import com.raedev.forms.FormItemType
import com.raedev.forms.internal.filter.RoundInputFilter

/**
 * 数字类型
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class NumberEditTextFormItem(label: String, name: String, value: String?) :
    EditTextFormItem(label, name, value) {

    override val formType: Int = FormItemType.NumberEditText.ordinal
    var unit: String? = null

    init {
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_SIGNED
        // 默认的保留浮点类型
        listOf("面积", "元", "金额", "费用").forEach {
            if (!label.contains(it)) return@forEach
            inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
            inputFilter = arrayListOf(RoundInputFilter())
        }
    }
}