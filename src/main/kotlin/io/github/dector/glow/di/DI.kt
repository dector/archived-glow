package io.github.dector.glow.di

import io.github.dector.glow.v2.v2Module
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance


object DI {

    val kodein = Kodein.lazy {
        import(v2Module)
    }

    fun init() {
        // Init Kodein automatically
    }

    inline fun <reified T : Any> get() = kodein.direct.instance<T>()
}