package io.github.dector.glow.v2

import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.mockimpl.MockDataProcessor
import io.github.dector.glow.v2.mockimpl.MockDataProvider
import io.github.dector.glow.v2.mockimpl.MockDataPublisher
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


val v2Module = Kodein.Module("V2") {

    import(mockImplementations)

    bind<GlowEngine>() with singleton {
        DefaultGlowEngine()
    }
}

private val mockImplementations = Kodein.Module("V2 mock") {

    bind<DataProvider>() with singleton { MockDataProvider() }
    bind<DataProcessor>() with singleton { MockDataProcessor() }
    bind<DataPublisher>() with singleton { MockDataPublisher() }
}