package io.github.dector.glow.server

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.config.LegacyProjectConfig
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.RuntimeConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.server.components.InMemoryDataPublisher
import io.github.dector.glow.utils.Execution
import io.github.dector.glow.utils.FileWatcher
import io.github.dector.glow.utils.StopWatch.Companion.DefaultSecondsFormatter
import io.github.dector.glow.utils.measureTimeMillis
import io.javalin.Javalin
import org.koin.dsl.module
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY

class Server(private val launchConfig: LaunchConfig) {

    private val pagesStorage = mutableSetOf<WebPage>()

    private lateinit var glowEngine: GlowEngine

    private val app: Javalin = Javalin.create { config ->
        config.showJavalinBanner = false
    }
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
            val runtimeConfig = DI.get<RuntimeConfig>().let { config ->
                config.copy(notes = config.notes.copy(
                    copyAssets = false
                ))
            }

            koin.modules(module {
                single<DataPublisher>(override = true) {
                    PreprocessedDataPublisher(InMemoryDataPublisher(pagesStorage))
                }
                single<LaunchConfig> { launchConfig }
                single<RuntimeConfig>(override = true) { runtimeConfig }
            })
        }
    }

    private fun injectDependencies() {
        glowEngine = DI.get()
    }

    private fun watchForBlogSources(body: () -> Unit) {
        val sourcesFolder = DI.get<LegacyProjectConfig>()
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

        val executionResult: Execution<GlowEngine.ExecutionResult> = measureTimeMillis {
            glowEngine.execute()
        }

        executionResult.error.let { error ->
            if (error != null) {
                System.err.println("Failed: ${error.message}")
            }
        }

        val executionTime = DefaultSecondsFormatter(executionResult.time)
        println("Ready! (finished in $executionTime)")
    }

    private fun startServer(port: Int) {
        println("Running server on port $port... ")

        app.get("/*", rootHandler)
        app.start(port)

        println("Started on: http://localhost:$port")
    }
}

private const val PORT = 9217
