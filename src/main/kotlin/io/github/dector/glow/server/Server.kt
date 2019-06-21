package io.github.dector.glow.server

import io.github.dector.glow.core.*
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.InMemoryDataPublisher
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.parentFolder
import io.github.dector.glow.utils.FileWatcher
import io.javalin.Javalin
import org.koin.dsl.module
import java.nio.file.StandardWatchEventKinds.*

class Server {

    private val pagesStorage = mutableSetOf<WebPage>()

    private lateinit var glowEngine: GlowEngine
    private lateinit var app: Javalin

    init {
        provideDependencies()
    }

    fun run() {
        injectDependencies()

        startServer()
        buildAndServeBlog()

        watchForBlogSources {
            restartServer()
            buildAndServeBlog()
        }
    }

    private fun provideDependencies() {
        DI.modify { koin ->
            koin.modules(module {
                single<DataPublisher>(override = true) {
                    InMemoryDataPublisher(pagesStorage)
                }
            })
        }
    }

    private fun injectDependencies() {
        glowEngine = DI.get()
    }

    private fun watchForBlogSources(body: () -> Unit) {
        val sourcesFolder = DI.get<ProjectConfig>()
            .input
            .sourcesFolder
        println("Serving '${sourcesFolder.absolutePath}'")

        FileWatcher().watchRecursively(
            sourcesFolder,
            ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE
        ) { body() }
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        pagesStorage.clear()
        glowEngine.execute()

        pagesStorage.forEach(app::serve)

        println("Ready!")
    }

    private fun restartServer() {
        println("Stopping server...")
        app.stop()

        startServer()
    }

    private fun startServer() {
        print("Running server... ")

        app = Javalin.create().start(9217)

        println("on port ${app.port()}")
    }
}

private fun Javalin.serve(page: WebPage) {
    serve(page.path, page.content)

    if (page.path.isIndex) {
        serve(page.path.parentFolder(), page.content)
    }
}

private fun Javalin.serve(path: WebPagePath, content: HtmlWebPageContent) {
    get(path.value) { ctx ->
        ctx.html(content.value)
    }
}
