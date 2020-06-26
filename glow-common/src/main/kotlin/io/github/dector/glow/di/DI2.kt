package io.github.dector.glow.di

import kotlin.reflect.KClass


object DI2 {

    private val registry = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> get(klass: KClass<T>): T {
        return registry.getValue(klass) as T
    }

    fun <T : Any> provide(klass: KClass<T>, value: T) {
        registry[klass] = value
    }

    inline fun <reified T : Any> get(): T = get(T::class)

    inline fun <reified T : Any> provide(value: T) = provide(T::class, value)
}
