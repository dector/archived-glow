package io.github.dector.glow.serve

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.config.provideProjectConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.buildGlowEngine
import io.github.dector.glow.di.get
import io.github.dector.glow.di.provide
import io.github.dector.glow.engine.GlowEngine
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.plugins.notes.NotesPluginConfig
import io.github.dector.glow.server.Server
import io.github.dector.glow.server.components.InMemoryDataPublisher
import io.github.dector.ktx.FileWatcher
import java.io.File
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY


class ServeApp internal constructor(
    private val storage: MutableSet<RenderedWebPage>,
    private val engine: GlowEngine,
    private val server: Server
) {

    fun execute() {
        buildAndServeBlog()

        server.run()

        watchForBlogSourcesBlocking {
            buildAndServeBlog()
        }
    }

    private fun buildAndServeBlog() {
        println("Building blog...")
        storage.clear()

        engine.execute()
    }

    companion object
}

/**
 * Construct app instance with requested configuration.
 *
 * @param projectDir path to website directory (should contain website configuration file)
 * @param includeDrafts set to `true` to render drafts
 */
fun ServeApp.Companion.create(
    projectDir: File,
    includeDrafts: Boolean
): ServeApp {
    val launchConfig = LaunchConfig(
        includeDrafts = includeDrafts
    )

    DI.provide(provideProjectConfig(projectDir, launchConfig))
    DI.provide(NotesPluginConfig(
        copyAssets = false
    ))

    val storage = mutableSetOf<RenderedWebPage>()
    val engine = provideGlowEngine(storage)

    return ServeApp(
        storage,
        engine,
        Server(storage)
    )
}


private fun provideGlowEngine(pagesStorage: MutableSet<RenderedWebPage>) =
    buildGlowEngine(
        publisher = InMemoryDataPublisher(pagesStorage)
    )

private fun watchForBlogSourcesBlocking(body: () -> Unit) {
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
