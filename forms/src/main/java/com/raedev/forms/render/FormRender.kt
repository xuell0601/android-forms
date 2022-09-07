package com.raedev.forms.render

import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroup

/**
 * 表单渲染
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FormRender(
    /** 表单组 */
    protected val formGroup: FormGroup,
    /** 表单项渲染需要依赖的FragmentManager */
    protected val fragmentManager: FragmentManager? = null
) {

    /**
     * 执行渲染
     */
    fun render() {
        onRender()
    }

    // 实现渲染方法
    abstract fun onRender()

}