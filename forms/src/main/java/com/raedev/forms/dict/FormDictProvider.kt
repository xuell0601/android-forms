package com.raedev.forms.dict

/**
 * 表单字段接口提供
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormDictProvider {

    /**
     * 根据字典值或者字典项目
     */
    fun getItem(name: String): DictItem

    /**
     * 根据字典名获取所属的字典列表
     */
    fun getItems(name: String): List<DictItem>
}