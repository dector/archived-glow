package io.github.dector.glow.core.utils

import kotlin.reflect.KProperty

class AnotherDelegate<out T>(private val producer: () -> T) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = producer()
}

fun <T> another(producer: () -> T) = AnotherDelegate(producer)