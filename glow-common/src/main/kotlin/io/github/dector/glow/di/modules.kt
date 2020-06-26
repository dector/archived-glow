package io.github.dector.glow.di

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.core.config.provideProjectConfig
import org.koin.dsl.module
import java.io.File


fun configModule(projectDir: File, launchConfig: LaunchConfig) = module {
    single<RuntimeConfig> { provideProjectConfig(projectDir, launchConfig) }
    single<NotesPluginConfig> { NotesPluginConfig() }
}
