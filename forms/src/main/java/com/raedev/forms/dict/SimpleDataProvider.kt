package com.raedev.forms.dict

/**
 * 简单的数据提供
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class SimpleDataProvider(data: List<FormSelectItem>) : FormDataProvider() {
    init {
        this.addData(data)
    }
}