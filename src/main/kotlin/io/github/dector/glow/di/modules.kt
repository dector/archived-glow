package io.github.dector.glow.di

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.core.ProjectConfig
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.DefaultDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.mockProjectsConfig
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.pipeline.GlowPipeline
import io.github.dector.glow.pipeline.PipelinedGlowEngine
import io.github.dector.glow.pipeline.PluggablePipeline
import io.github.dector.glow.plugins.notes.*
import io.github.dector.glow.plugins.pages.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    bind<MarkdownParser<Node>>() with singleton { SimpleMarkdownParser() }
    bind<HtmlRenderer>() with singleton { HtmlRenderer.builder().build() }

    import(defaultImplementationsModule)

    bind<GlowPipeline>() with singleton {
        val logger = UILogger

        PluggablePipeline(
                NotesPlugin(instance(), instance(), instance(), logger)/*,
                PagesPlugin(instance(), instance(), instance(), logger),
                StaticResourcesPlugin(instance(), logger)*/
        )

    }
    bind<GlowEngine>() with singleton {
        PipelinedGlowEngine(instance())
    }
}

private val defaultImplementationsModule = Kodein.Module("V2 mock") {

    bind<ProjectConfig>() with singleton { mockProjectsConfig() }
    bind<DataPublisher>() with singleton { DefaultDataPublisher(instance()) }

    import(notesModule)
    import(pagesModule)
}

private val notesModule = Kodein.Module("Notes pipeline") {
    bind<NotesPathResolver>() with singleton { NotesWebPathResolver(instance()) }
    bind<NotesDataProvider>() with singleton { DefaultNotesDataProvider(instance(), instance()) }
    bind<NotesDataRenderer>() with singleton { DefaultNotesDataRenderer(instance(), instance(), instance(), instance()) }
}

private val pagesModule = Kodein.Module("Pages pipeline") {
    bind<PagesPathResolver>() with singleton { PagesWebPathResolver(instance()) }
    bind<PagesDataProvider>() with singleton { DefaultPagesDataProvider(instance(), instance()) }
    bind<PagesDataRenderer>() with singleton { DefaultPagesDataRenderer(instance(), instance(), instance(), instance()) }
}