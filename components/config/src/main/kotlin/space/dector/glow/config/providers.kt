package space.dector.glow.config

import space.dector.glow.config.project.ParsingContext
import java.io.File


fun provideProjectConfig(
    dir: File,
    launchConfig: LaunchConfig
): RuntimeConfig =
    buildRuntimeConfig(
        findConfig(dir),
        ParsingContext(
            dir = dir,
            launchConfig = launchConfig
        )
    )

fun findConfig(dir: File): File {
    require(dir.isDirectory)

    val candidates = dir.listFiles()!!
        .filter { it.extension == "glow" }

    require(candidates.isNotEmpty()) { "No `.glow` file found in ${dir.absolutePath}" }
    require(candidates.size == 1)

    return candidates.first()
}
