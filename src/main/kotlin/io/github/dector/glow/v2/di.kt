package io.github.dector.glow.v2

import com.vladsch.flexmark.ast.Node
import io.github.dector.glow.core.logger.UiLogger
import io.github.dector.glow.v2.core.components.DataPublisher
import io.github.dector.glow.v2.core.components.DataRenderer
import io.github.dector.glow.v2.core.components.GlowEngine
import io.github.dector.glow.v2.core.components.PathResolver
import io.github.dector.glow.v2.implementation.*
import io.github.dector.glow.v2.pipeline.CombinedPipeline
import io.github.dector.glow.v2.pipeline.GlowPipeline
import io.github.dector.glow.v2.pipeline.PipelinedGlowEngine
import io.github.dector.glow.v2.pipeline.notes.DefaultNotesDataProvider
import io.github.dector.glow.v2.pipeline.notes.NotesDataProvider
import io.github.dector.glow.v2.pipeline.notes.NotesPipeline
import io.github.dector.glow.v2.pipeline.pages.DefaultPagesDataProvider
import io.github.dector.glow.v2.pipeline.pages.PagesDataProvider
import io.github.dector.glow.v2.pipeline.pages.PagesPipeline
import io.github.dector.glow.v2.pipeline.resources.StaticResourcesPipeline
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    bind<PathResolver>() with singleton { WebPathResolver(instance()) }
    bind<MarkdownParser<Node>>() with singleton { SimpleMarkdownParser() }

    import(defaultImplementationsModule)

    bind<GlowPipeline>() with singleton {
        val logger = UiLogger

        CombinedPipeline(
                NotesPipeline(instance(), instance(), instance(), logger),
                PagesPipeline(instance(), instance(), instance(), logger),
                StaticResourcesPipeline(instance(), logger)
        )

    }
    bind<GlowEngine>() with singleton {
        PipelinedGlowEngine(instance())
    }
}

private val defaultImplementationsModule = Kodein.Module("V2 mock") {

    bind<ProjectConfig>() with singleton { mockProjectsConfig() }
    bind<DataRenderer>() with singleton { DefaultDataRenderer(instance(), instance(), instance()) }
    bind<DataPublisher>() with singleton { DefaultDataPublisher(instance()) }

    import(notesModule)
    import(pagesModule)
}

private val notesModule = Kodein.Module("Notes pipeline") {
    bind<NotesDataProvider>() with singleton { DefaultNotesDataProvider(instance(), instance()) }
}

private val pagesModule = Kodein.Module("Pages pipeline") {
    bind<PagesDataProvider>() with singleton { DefaultPagesDataProvider(instance(), instance()) }
}