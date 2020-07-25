package space.dector.glow.serve

import space.dector.glow.config.LaunchConfig
import space.dector.glow.config.RuntimeConfig
import space.dector.glow.config.provideProjectConfig
import space.dector.glow.di.DI
import space.dector.glow.di.buildGlowEngine
import space.dector.glow.di.get
import space.dector.glow.di.provide
import space.dector.glow.engine.GlowEngine
import space.dector.glow.engine.RenderedWebPage
import space.dector.glow.plugins.notes.NotesPluginConfig
import space.dector.glow.server.Server
import space.dector.glow.server.components.InMemoryDataPublisher
import space.dector.ktx.FileWatcher
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
