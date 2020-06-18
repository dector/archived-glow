package io.github.dector.glow.config

import java.io.File


fun parseProjectConfig(file: File, context: ParsingContext): ProjectConfig =
    SimpleProjectConfig(
        projectDir = context.dir.normalize().toPath(),
        legacy = parseLegacyConfig(file, context),
        launchConfig = context.launchConfig
    )
