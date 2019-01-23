package io.github.dector.glow.v2

import com.vladsch.flexmark.ast.Node
import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.mockimpl.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    bind<PathResolver>() with singleton { WebPathResolver(instance()) }
    bind<MarkdownParser<Node>>() with singleton { SimpleMarkdownParser() }

    import(mockImplementations)

    bind<GlowEngine>() with singleton {
        DefaultGlowEngine(instance(), instance(), instance(), instance())
    }
}

private val mockImplementations = Kodein.Module("V2 mock") {

    bind<ProjectConfig>() with singleton { mockProjectsConfig() }
    bind<DataProvider>() with singleton { MockDataProvider(instance(), instance()) }
    bind<DataRenderer>() with singleton { MockDataRenderer(instance(), instance()) }
    bind<DataPublisher>() with singleton { MockDataPublisher(instance()) }
}