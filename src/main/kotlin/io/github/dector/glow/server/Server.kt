package io.github.dector.glow.server

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.InMemoryDataPublisher
import io.github.dector.glow.core.isIndex
import io.github.dector.glow.di.DI
import io.github.dector.glow.pathToFolder
import io.javalin.Javalin
import org.koin.dsl.module

fun main() {
    val pagesStorage = mutableSetOf<WebPage>()

    DI.init()
    DI.app.modules(module {
        single<DataPublisher>(override = true) { InMemoryDataPublisher(pagesStorage) }
    })

    val app = Javalin
        .create()
        .start(9217)

    println("Running app")
    DI.get<GlowEngine>()
        .execute()

    println("Publishing")
    pagesStorage.forEach { page ->
        if (page.path.isIndex) {
            app.get(page.path.pathToFolder()) { ctx ->
                ctx.html(page.content.value)
            }
        }

        app.get(page.path.value) { ctx ->
            ctx.html(page.content.value)
        }
    }
}
