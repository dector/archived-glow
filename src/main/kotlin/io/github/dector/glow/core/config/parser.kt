package io.github.dector.glow.core.config

import io.github.dector.glow.config.buildRuntimeConfig
import io.github.dector.glow.config.project.ParsingContext
import java.io.File


fun findConfig(dir: File): File {
    require(dir.isDirectory)

    val candidates = dir.listFiles()!!
        .filter { it.extension == "glow" }

    require(candidates.isNotEmpty()) { "No `.glow` file found in ${dir.absolutePath}" }
    require(candidates.size == 1)

    return candidates.first()
}

fun main() {
    val dir = File("blog")

    val context = ParsingContext(
        dir = dir
    )

    val configFile = findConfig(dir)
    val config = buildRuntimeConfig(configFile, context)

    println(config)
}
