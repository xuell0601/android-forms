package com.raedev.forms.internal.filter

import android.text.InputFilter
import android.text.Spanned

/**
 * 保留位数过滤，默认小数点2位
 * @author RAE
 * @date 2022/09/08
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
class RoundInputFilter(
    /**  保留多少小数位 */
    private val keepLength: Int = 2
) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val splits = dest.toString().split(".").toTypedArray()
        return if (splits.size == 2 && splits[1].length >= keepLength) "" else null
    }
}