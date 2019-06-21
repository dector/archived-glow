package io.github.dector.glow.server

import io.github.dector.glow.core.*
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.InMemoryDataPublisher
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.parentFolder
import io.javalin.Javalin
import org.koin.dsl.module
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files.walkFileTree
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes

class Server {

    private val pagesStorage = mutableSetOf<WebPage>()
    private lateinit var app: Javalin

    init {
        prepareServerDependencies()
    }

    fun run() {
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
        val watcher = FileSystems.getDefault().newWatchService()
        val path = config.input.sourcesFolder.toPath()

        walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?): FileVisitResult {
                dir.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_MODIFY,
                    ENTRY_DELETE)
                return FileVisitResult.CONTINUE
            }
        })

        var process = true
        while (process) {
            val watcherKey = watcher.take()
            if (watcherKey == null) {
                process = false
                continue
            }

            val events = watcherKey.pollEvents()
            if (events.isNotEmpty())
                body()

            watcherKey.reset()
        }
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        pagesStorage.clear()
        DI.get(GlowEngine::class)
            .execute()

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
