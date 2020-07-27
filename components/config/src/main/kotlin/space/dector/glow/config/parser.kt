package space.dector.glow.config

import space.dector.glow.config.project.ParsingContext
import space.dector.glow.config.project.parseProjectConfig
import java.io.File


fun buildRuntimeConfig(file: File, context: ParsingContext): RuntimeConfig =
    buildRuntimeConfig(
        projectDir = context.dir.normalize().toPath(),
        config = parseProjectConfig(file, context),
        launchConfig = context.launchConfig
    )
