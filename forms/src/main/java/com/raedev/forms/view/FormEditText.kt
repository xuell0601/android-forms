package com.raedev.forms.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.raedev.forms.listener.FormTextChangedListener

/**
 * 文本编辑框，目的是让RecycleView复用监听器
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormEditText(context: Context, attrs: AttributeSet?) :
    AppCompatEditText(context, attrs) {

    internal var textChangedListener: FormTextChangedListener? = null

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        textChangedListener?.onTextChanged(this, text.toString())
    }
}