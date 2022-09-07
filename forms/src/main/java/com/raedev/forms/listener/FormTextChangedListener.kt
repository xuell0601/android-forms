package com.raedev.forms.listener

import com.raedev.forms.view.FormEditText

/**
 * 文本内容改变回调
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormTextChangedListener {

    /**
     * 文本发生改变
     * @param view 视图
     * @param value 值
     */
    fun onTextChanged(view: FormEditText, value: String)
}