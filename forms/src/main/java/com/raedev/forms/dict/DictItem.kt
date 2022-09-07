package com.raedev.forms.dict

/**
 * 字典节点
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DictItem(
    /** 字典名称 */
    val name: String,
    /** 字典值 */
    val code: String,
    /** 字典显示名称 */
    val label: String,
    /** 子节点 */
    val children: List<DictItem>? = null
)