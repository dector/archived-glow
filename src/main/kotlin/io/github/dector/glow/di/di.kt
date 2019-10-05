package io.github.dector.glow.di

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.DefaultDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.core.provideProjectConfig
import io.github.dector.glow.pipeline.GlowPipeline
import io.github.dector.glow.pipeline.PipelinedGlowEngine
import io.github.dector.glow.pipeline.PluggablePipeline
import io.github.dector.glow.plugins.notes.*
import io.github.dector.glow.plugins.pages.*
import io.github.dector.glow.ui.StdUiConsole
import io.github.dector.glow.ui.UiConsole
import org.koin.dsl.module

val appModule = module {
    single<GlowPipeline> {
        val logger = UILogger

        PluggablePipeline(
            NotesPlugin(get(), get(), get(), get(), logger)//,
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
    single<DataPublisher> { DefaultDataPublisher(get()) }

    // notes

    single<NotesPathResolver>() { NotesWebPathResolver(get()) }
    single<NotesDataProvider>() { DefaultNotesDataProvider(get(), get()) }
    single<NotesDataRenderer>() { DefaultNotesDataRenderer(get(), get(), get()) }

    // pages

    single<PagesPathResolver>() { PagesWebPathResolver(get()) }
    single<PagesDataProvider>() { DefaultPagesDataProvider(get(), get()) }
    single<PagesDataRenderer>() { DefaultPagesDataRenderer(get(), get(), get()) }
}
