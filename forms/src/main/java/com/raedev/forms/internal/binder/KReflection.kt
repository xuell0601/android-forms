package com.raedev.forms.internal.binder

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Kotlin 反射
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class KReflection(private val entity: Any) {
    private val kClass: KClass<*> = entity::class

    /**
     * 获取所有的字段
     */
    fun getProperties(): Collection<KProperty1<out Any, *>> {
        return kClass.declaredMemberProperties
    }
}