package io.github.dector.glow.di

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.config.NotesPluginConfig
import org.koin.dsl.module


fun configModule() = module {
    single<RuntimeConfig> { DI2.get() }
    single<NotesPluginConfig> { NotesPluginConfig() }
}
