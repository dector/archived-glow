package io.github.dector.glow.config

import io.github.dector.glow.core.config.NavItemType
import java.io.File
import java.nio.file.Path


@Deprecated("")
data class LegacyProjectConfig(
    val projectDir: Path,

    val glow: CGlow,
    val blog: CBlog,
    val plugins: CPlugins
)

data class CGlow(
    val config: CConfig,
    val output: COutput,
    val assets: CAssets
)

@Deprecated("")
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
    val id: String,
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
