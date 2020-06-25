package io.github.dector.glow.config

import io.github.dector.glow.config.project.ParsingContext
import io.github.dector.glow.config.project.parseProjectConfig
import java.io.File


fun buildRuntimeConfig(file: File, context: ParsingContext): RuntimeConfig =
    buildRuntimeConfig(
        projectDir = context.dir.normalize().toPath(),
        projectConfig = parseProjectConfig(file, context),
        launchConfig = context.launchConfig
    )
