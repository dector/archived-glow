package io.github.dector.glow.core.config

import java.io.File

fun provideProjectConfig(dir: File): ProjectConfig =
    parseConfig(findConfig(dir), ParsingContext(dir = dir))
