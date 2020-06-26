package io.github.dector.glow.serve

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.config.provideProjectConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.buildGlowEngine
import io.github.dector.glow.di.get
import io.github.dector.glow.di.provide
import io.github.dector.glow.engine.GlowEngine
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.server.Server
import io.github.dector.glow.server.components.InMemoryDataPublisher
import io.github.dector.glow.utils.Execution
import io.github.dector.glow.utils.StopWatch
import io.github.dector.glow.utils.measureTimeMillis
import io.github.dector.ktx.FileWatcher
import java.io.File
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY


class ServeApp private constructor(
    private val storage: MutableSet<WebPage>,
    private val engine: GlowEngine,
    private val server: Server
) {

    fun execute() {
        buildAndServeBlog()

        watchForBlogSources {
            buildAndServeBlog()
        }
        server.run()
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        storage.clear()

        val executionResult: Execution<GlowEngine.ExecutionResult> = measureTimeMillis {
            engine.execute()
        }

        executionResult.error.let { error ->
            if (error != null) {
                System.err.println("Failed: ${error.message}")
            }
        }

        val executionTime = StopWatch.DefaultSecondsFormatter(executionResult.time)
        println("Ready! (finished in $executionTime)")
    }

    companion object {
        fun create(
            projectDir: File,
            includeDrafts: Boolean
        ): ServeApp {
            val launchConfig = LaunchConfig(
                includeDrafts = includeDrafts
            )

            DI.provide(provideProjectConfig(projectDir, launchConfig))

            val storage = mutableSetOf<WebPage>()
            val engine = provideGlowEngine(storage)

            return ServeApp(
                storage,
                engine,
                Server(storage)
            )
        }
    }
}

private fun provideGlowEngine(pagesStorage: MutableSet<WebPage>) =
    buildGlowEngine(
        publisher = InMemoryDataPublisher(pagesStorage)
    )

private fun watchForBlogSources(body: () -> Unit) {
    val sourcesFolder = DI.get<RuntimeConfig>()
        .glow
        .sourceDir
        .toFile()
    println("Serving '${sourcesFolder.absolutePath}'")

    FileWatcher().watchRecursively(
        sourcesFolder,
        ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE
    ) { body() }
}
