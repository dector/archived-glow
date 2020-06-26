package io.github.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.FileDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.plugins.notes.DefaultNotesDataRenderer
import io.github.dector.glow.plugins.notes.FileSystemNotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataRenderer
import io.github.dector.glow.plugins.notes.NotesPlugin
import io.github.dector.glow.plugins.notes.NotesWebPathResolver
import io.github.dector.glow.plugins.resources.AssetsPlugin2
import io.github.dector.glow.plugins.resources.ThemeAssetsPlugin

fun buildGlowEngine(
    config: RuntimeConfig = DI.get(),
    publisher: DataPublisher = defaultDataPublisher()
): GlowEngine {
    val provider = FileSystemNotesDataProvider(
        config.glow.notes.sourceDir.toFile())
    val renderer = DefaultNotesDataRenderer(
        NotesWebPathResolver(DI.get()),
        SimpleMarkdownParser(),
        HtmlRenderer.builder().build()
    )

    return buildGlowEngine(
        provider, renderer, publisher
    )
}

internal fun buildGlowEngine(
    dataProvider: NotesDataProvider,
    dataRenderer: NotesDataRenderer,
    dataPublisher: DataPublisher
): GlowEngine {
    val config = DI.get<RuntimeConfig>()
    return GlowEngine(
        NotesPlugin(dataProvider, dataRenderer, dataPublisher, config, NotesPluginConfig.get),
        AssetsPlugin2(config, NotesPluginConfig.get),
        ThemeAssetsPlugin(config)
    )
}

private fun defaultDataPublisher(config: RuntimeConfig = DI.get()) =
    PreprocessedDataPublisher(FileDataPublisher(config))
