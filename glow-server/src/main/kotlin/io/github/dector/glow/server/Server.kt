package io.github.dector.glow.server

import arrow.core.Either
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.InMemoryDataPublisher
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.utils.Execution
import io.github.dector.glow.utils.FileWatcher
import io.github.dector.glow.utils.StopWatch.Companion.DefaultSecondsFormatter
import io.github.dector.glow.utils.measureTimeMillis
import io.javalin.Javalin
import org.koin.dsl.module
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY

class Server {

    private val pagesStorage = mutableSetOf<WebPage>()

    private lateinit var glowEngine: GlowEngine

    private val app: Javalin = Javalin.create()
    private val rootHandler = RootHandler(DI.get(), pagesStorage)

    init {
        provideDependencies()
    }

    fun run() {
        injectDependencies()

        startServer(PORT)
        buildAndServeBlog()

        watchForBlogSources {
            buildAndServeBlog()
        }
    }

    private fun provideDependencies() {
        DI.reset()
        DI.modify { koin ->
            koin.modules(module {
                single<DataPublisher>(override = true) {
                    PreprocessedDataPublisher(InMemoryDataPublisher(pagesStorage))
                }
            })
        }
    }

    private fun injectDependencies() {
        glowEngine = DI.get()
    }

    private fun watchForBlogSources(body: () -> Unit) {
        val sourcesFolder = DI.get<Config>()
            .blog
            .sourceDir
        println("Serving '${sourcesFolder.absolutePath}'")

        FileWatcher().watchRecursively(
            sourcesFolder,
            ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE
        ) { body() }
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        pagesStorage.clear()

        val executionResult: Execution<Unit> = measureTimeMillis {
            glowEngine.execute()
        }

        val result = executionResult.result
        if (result is Either.Left) {
            System.err.println("Failed: ${result.a.message}")
        }

        val executionTime = DefaultSecondsFormatter(executionResult.time)
        println("Ready! (finished in $executionTime)")
    }

    private fun startServer(port: Int) {
        println("Running server on port $port... ")

        app.get("/*", rootHandler)
        app.start(port)
    }
}

private const val PORT = 9217
