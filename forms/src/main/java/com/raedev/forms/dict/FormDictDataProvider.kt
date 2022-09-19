package com.raedev.forms.dict

import android.content.Context

/**
 * 表单字典数据提供
 * 1. 从接口中获取数据字典
 * 2. 保存到本地
 * 3. 从本地加载数据
 * 4. 数据字典过滤
 * @author RAE
 * @date 2022/09/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FormDictDataProvider(context: Context, dictKey: String? = null) :
    FormDataProvider() {

    fun loadDict() {
        //
    }
}