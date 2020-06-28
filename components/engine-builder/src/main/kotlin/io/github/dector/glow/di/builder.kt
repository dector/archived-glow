package io.github.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.GlowEngine
import io.github.dector.glow.engine.defaults.FileDataPublisher
import io.github.dector.glow.engine.defaults.PreprocessedDataPublisher
import io.github.dector.glow.plugins.notes.NotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataRenderer
import io.github.dector.glow.plugins.notes.NotesPathResolver
import io.github.dector.glow.plugins.notes.NotesPlugin
import io.github.dector.glow.plugins.notes.NotesWebPathResolver
import io.github.dector.glow.plugins.notes.providers.FileSystemNotesDataProvider
import io.github.dector.glow.plugins.notes.renderers.DefaultNotesDataRenderer
import io.github.dector.glow.plugins.resources.AssetsPlugin2
import io.github.dector.glow.plugins.resources.ThemeAssetsPlugin

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
