package io.github.dector.glow.server

import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.InMemoryDataPublisher
import io.github.dector.glow.core.isIndex
import io.github.dector.glow.di.DI
import io.github.dector.glow.parentFolder
import io.javalin.Javalin
import org.koin.dsl.module

class Server {

    private val pagesStorage = mutableSetOf<WebPage>()

    init {
        prepareServerDependencies()
    }

    fun run() {
        print("Running server... ")

        val app = runServer()

        println("on port ${app.port()}")

        println("Building blog...")
        DI.get(GlowEngine::class)
            .execute()

        pagesStorage.forEach { page ->
            app.serve(page.path, page.content)

            if (page.path.isIndex) {
                app.serve(page.path.parentFolder(), page.content)
            }
        }
    }

    private fun prepareServerDependencies() {
        DI.modify { koin ->
            koin.modules(module {
                single<DataPublisher>(override = true) {
                    InMemoryDataPublisher(pagesStorage)
                }
            })
        }
    }
}

private fun Javalin.serve(path: WebPagePath, content: HtmlWebPageContent) {
    get(path.value) { ctx ->
        ctx.html(content.value)
    }
}

private fun runServer() = Javalin
    .create()
    .start(9217)
