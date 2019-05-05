package io.github.dector.glow.v2

import com.vladsch.flexmark.ast.Node
import io.github.dector.glow.v2.core.components.*
import io.github.dector.glow.v2.implementation.*
import io.github.dector.glow.v2.pipeline.GlowPipeline
import io.github.dector.glow.v2.pipeline.NotesPipeline
import io.github.dector.glow.v2.pipeline.PipelinedGlowEngine
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    bind<PathResolver>() with singleton { WebPathResolver(instance()) }
    bind<MarkdownParser<Node>>() with singleton { SimpleMarkdownParser() }

    import(defaultImplementationsModule)

    bind<GlowPipeline>() with singleton {
        NotesPipeline(instance(), instance(), instance())
    }
    bind<GlowEngine>() with singleton {
        PipelinedGlowEngine(instance())
    }
}

private val defaultImplementationsModule = Kodein.Module("V2 mock") {

    bind<ProjectConfig>() with singleton { mockProjectsConfig() }
    bind<DataProvider>() with singleton { DefaultDataProvider(instance(), instance()) }
    bind<DataRenderer>() with singleton { DefaultDataRenderer(instance(), instance(), instance()) }
    bind<DataPublisher>() with singleton { DefaultDataPublisher(instance()) }
}