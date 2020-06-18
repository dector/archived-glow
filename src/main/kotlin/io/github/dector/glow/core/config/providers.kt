package io.github.dector.glow.core.config

import io.github.dector.glow.config.ParsingContext
import io.github.dector.glow.config.ProjectConfig
import io.github.dector.glow.config.parseProjectConfig
import java.io.File

fun provideProjectConfig(dir: File): ProjectConfig =
    parseProjectConfig(findConfig(dir), ParsingContext(dir = dir))
