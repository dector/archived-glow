package io.github.dector.glow.v2

import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.mockimpl.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    bind<MarkdownParser>() with singleton { SimpleMarkdownParser() }

    import(mockImplementations)

    bind<GlowEngine>() with singleton {
        DefaultGlowEngine()
    }
}

private val mockImplementations = Kodein.Module("V2 mock") {

    bind<ProjectConfig>() with singleton { mockProjectsConfig() }
    bind<DataProvider>() with singleton { MockDataProvider(instance(), instance()) }
    bind<DataProcessor>() with singleton { MockDataProcessor() }
    bind<DataPublisher>() with singleton { MockDataPublisher() }
}