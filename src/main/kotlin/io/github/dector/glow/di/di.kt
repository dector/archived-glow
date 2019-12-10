package io.github.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.FileDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.core.provideProjectConfig
import io.github.dector.glow.pipeline.GlowPipeline
import io.github.dector.glow.pipeline.PipelinedGlowEngine
import io.github.dector.glow.pipeline.PluggablePipeline
import io.github.dector.glow.plugins.notes.DefaultNotesDataProvider
import io.github.dector.glow.plugins.notes.DefaultNotesDataRenderer
import io.github.dector.glow.plugins.notes.NotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataRenderer
import io.github.dector.glow.plugins.notes.NotesPathResolver
import io.github.dector.glow.plugins.notes.NotesPlugin
import io.github.dector.glow.plugins.notes.NotesWebPathResolver
import io.github.dector.glow.plugins.pages.DefaultPagesDataProvider
import io.github.dector.glow.plugins.pages.DefaultPagesDataRenderer
import io.github.dector.glow.plugins.pages.PagesDataProvider
import io.github.dector.glow.plugins.pages.PagesDataRenderer
import io.github.dector.glow.plugins.pages.PagesPathResolver
import io.github.dector.glow.plugins.pages.PagesWebPathResolver
import io.github.dector.glow.ui.StdUiConsole
import io.github.dector.glow.ui.UiConsole
import org.koin.dsl.module

val appModule = module {
    single<GlowPipeline> {
        val logger = UILogger

        PluggablePipeline(
            NotesPlugin(get(), get(), get(), get(), logger)//,
            //RssPlugin()
//                PagesPlugin(get(), get(), get(), logger),
//                StaticResourcesPlugin(get(), logger)
        )
    }
    single<GlowEngine> { PipelinedGlowEngine(get()) }

    single<UiConsole> { StdUiConsole() }

    single<MarkdownParser<Node>> { SimpleMarkdownParser() }
    single<HtmlRenderer> { HtmlRenderer.builder().build() }

    // mocks

    single<Config> { provideProjectConfig() }
    single<DataPublisher> { PreprocessedDataPublisher(FileDataPublisher(get())) }

    // notes

    single<NotesPathResolver>() { NotesWebPathResolver(get()) }
    single<NotesDataProvider>() { DefaultNotesDataProvider(get(), get()) }
    single<NotesDataRenderer>() { DefaultNotesDataRenderer(get(), get(), get()) }

    // pages

    single<PagesPathResolver>() { PagesWebPathResolver(get()) }
    single<PagesDataProvider>() { DefaultPagesDataProvider(get(), get()) }
    single<PagesDataRenderer>() { DefaultPagesDataRenderer(get(), get(), get()) }
}
