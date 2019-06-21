package io.github.dector.glow.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

object DI {

    lateinit var app: KoinApplication

    fun init() {
        app = startKoin {
            modules(appModule)
        }
    }

    inline fun <reified T : Any> get() = app.koin.get<T>()
}
