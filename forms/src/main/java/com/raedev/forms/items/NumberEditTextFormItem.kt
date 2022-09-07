package com.raedev.forms.items

import android.view.inputmethod.EditorInfo
import com.raedev.forms.view.FormEditText

/**
 * 数字类型
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class NumberEditTextFormItem(label: String, name: String, value: String?) :
    EditTextFormItem(label, name, value) {

    override fun onBindEditText(editText: FormEditText) {
        super.onBindEditText(editText)
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_SIGNED
    }
}