package io.github.dector.glow.core.config

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.config.buildRuntimeConfig
import io.github.dector.glow.config.project.ParsingContext
import java.io.File

fun provideProjectConfig(dir: File): RuntimeConfig =
    buildRuntimeConfig(findConfig(dir), ParsingContext(dir = dir))
