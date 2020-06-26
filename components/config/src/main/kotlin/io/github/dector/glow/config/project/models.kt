package io.github.dector.glow.config.project

import java.io.File
import java.nio.file.Path


internal data class CProject(
    val glow: CGlow,
    val blog: CBlog,
    val plugins: CPlugins
)

internal data class CGlow(
    val config: CConfig,
    val output: COutput,
    val assets: CAssets
)

internal data class CConfig(val version: String)

internal data class COutput(val overrideFiles: Boolean)

internal data class CAssets(val targetPath: Path)

internal data class CBlog(
    val title: String,
    val navigation: List<CNavigation>,
    val footer: CFooter,
    val sourceDir: File,
    val outputDir: File
)

internal data class CNavigation(
    val id: String,
    val title: String,
    val path: String
)

internal data class CFooter(
    val author: String,
    val year: String,
    val licenseName: String,
    val licenseUrl: String
)

internal data class CPlugins(val notes: CNotesPlugin)

internal data class CNotesPlugin(
    val sourceDir: File,
    val path: String
)
