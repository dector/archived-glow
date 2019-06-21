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
        prepareServerDependencies()
    }

    fun run() {
        glowEngine = DI.get(GlowEngine::class)

        buildAndServeBlog()

        val projectConfig = DI.get<ProjectConfig>()
        println("Serving ${projectConfig.input.sourcesFolder.absolutePath}")

        watchForBlogSources(projectConfig) {
            buildAndServeBlog()
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

    private fun watchForBlogSources(config: ProjectConfig, body: () -> Unit) {
        FileWatcher().watchRecursively(
            config.input.sourcesFolder,
            ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE
        ) { body() }
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        pagesStorage.clear()
        glowEngine.execute()

        restartServer()
        pagesStorage.forEach { page ->
            app.serve(page.path, page.content)

            if (page.path.isIndex) {
                app.serve(page.path.parentFolder(), page.content)
            }
        }

        println("Ready!")
    }

    private fun restartServer() {
        if (::app.isInitialized) {
            println("Stopping server...")

            app.stop()
        }

        print("Running server... ")

        app = Javalin.create().start(9217)

        println("on port ${app.port()}")
    }
}

private fun Javalin.serve(path: WebPagePath, content: HtmlWebPageContent) {
    get(path.value) { ctx ->
        ctx.html(content.value)
    }
}
