package com.raedev.forms.render

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroupAdapter

/**
 * 实体类表单渲染
 * @author RAE
 * @date 2022/09/20
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class EntityFormRender<T : IEntityFormRender>(
    context: Context,
    adapter: FormGroupAdapter,
    fragmentManager: FragmentManager,
    val entity: T
) : FormRender(context, adapter, fragmentManager) {

    override fun render() {
        super.render()
        // 交给实体类渲染
        entity.onRender(this)
    }
}