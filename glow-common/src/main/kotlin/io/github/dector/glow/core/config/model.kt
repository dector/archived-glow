package io.github.dector.glow.core.config

import java.io.File
import java.nio.file.Path

data class ProjectConfig(
    val glow: CGlow,
    val blog: CBlog,
    val plugins: CPlugins
)

data class CGlow(
    val config: CConfig,
    val output: COutput,
    val assets: CAssets,
    val _theme: String
)

data class CConfig(val version: String)

data class COutput(val overrideFiles: Boolean)

data class CAssets(val targetPath: Path)

data class CBlog(
    val title: String,
    val navigation: List<CNavigation>,
    val footer: CFooter,
    val sourceDir: File,
    val outputDir: File
)

data class CNavigation(
    val title: String,
    val path: String,
    val type: NavItemType
)

data class CFooter(
    val author: String,
    val year: String,
    val licenseName: String,
    val licenseUrl: String
)

data class CPlugins(val notes: CNotesPlugin)

data class CNotesPlugin(
    val sourceDir: File,
    val path: String
)
