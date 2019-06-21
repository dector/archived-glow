package io.github.dector.glow.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import kotlin.reflect.KClass

object DI {

    private lateinit var app: KoinApplication

    fun init() {
        app = startKoin {
            modules(appModule)
        }
    }

    fun modify(block: (KoinApplication) -> Unit) = block(app)

    fun <T : Any> get(clazz: KClass<T>): T = app.koin.get(clazz, null, null) as T
}

inline fun <reified T : Any> DI.get(): T = DI.get(T::class)

