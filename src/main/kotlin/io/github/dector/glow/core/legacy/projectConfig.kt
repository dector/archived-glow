package io.github.dector.glow.core.legacy

import java.io.File

@Deprecated("Used only in pages")
data class ProjectConfig(
    val input: InputConfig,
    val output: OutputConfig
)

@Deprecated("Used only in pages")
data class InputConfig(
    val staticFolder: File,
    val pagesFolder: File
)

@Deprecated("Used only in pages")
data class OutputConfig(
    val staticFolder: File
)
