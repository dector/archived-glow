package space.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import space.dector.glow.config.RuntimeConfig
import space.dector.glow.core.parser.SimpleMarkdownParser
import space.dector.glow.engine.DataPublisher
import space.dector.glow.engine.GlowEngine
import space.dector.glow.engine.defaults.FileDataPublisher
import space.dector.glow.engine.defaults.PreprocessedDataPublisher
import space.dector.glow.plugins.notes.NotesDataProvider
import space.dector.glow.plugins.notes.NotesDataRenderer
import space.dector.glow.plugins.notes.NotesPathResolver
import space.dector.glow.plugins.notes.NotesPlugin
import space.dector.glow.plugins.notes.NotesWebPathResolver
import space.dector.glow.plugins.notes.providers.FileSystemNotesDataProvider
import space.dector.glow.plugins.notes.renderers.DefaultNotesDataRenderer
import space.dector.glow.plugins.resources.AssetsPlugin2
import space.dector.glow.plugins.resources.ThemeAssetsPlugin

fun buildGlowEngine(
    config: RuntimeConfig = DI.get(),
    publisher: DataPublisher = defaultDataPublisher()
): GlowEngine {
    val provider = FileSystemNotesDataProvider(
        config.glow.notes.sourceDir.toFile())
    val pathResolver = NotesWebPathResolver(DI.get())
    val renderer = DefaultNotesDataRenderer(
        pathResolver,
        SimpleMarkdownParser(),
        HtmlRenderer.builder().build()
    )

    return buildGlowEngine(
        provider, renderer, publisher, pathResolver
    )
}

internal fun buildGlowEngine(
    dataProvider: NotesDataProvider,
    dataRenderer: NotesDataRenderer,
    dataPublisher: DataPublisher,
    pathResolver: NotesPathResolver
): GlowEngine {
    val config = DI.get<RuntimeConfig>()
    return GlowEngine(
        NotesPlugin(dataProvider, dataRenderer, dataPublisher, pathResolver, config, DI.get()),
        AssetsPlugin2(config, DI.get()),
        ThemeAssetsPlugin(config)
    )
}

private fun defaultDataPublisher(config: RuntimeConfig = DI.get()) =
    PreprocessedDataPublisher(FileDataPublisher(config))
