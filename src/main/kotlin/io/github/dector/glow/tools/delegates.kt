package io.github.dector.glow.tools

import kotlin.reflect.KProperty

class AnotherDelegate<out T>(private val producer: () -> T) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = producer()
}

fun <T> another(producer: () -> T) = AnotherDelegate(producer)