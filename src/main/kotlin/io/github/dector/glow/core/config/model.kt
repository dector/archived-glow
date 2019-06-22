package io.github.dector.glow.core.config

import io.github.dector.glow.core.NavItemType

data class Config(
    val glow: CGlow,
    val blog: CBlog,
    val plugins: CPlugins
)

data class CGlow(val config: CConfig)

data class CConfig(val version: String)

data class CBlog(
    val title: String,
    val navigation: List<CNavigation>,
    val footer: CFooter,
    val sourceDir: String   // File
)

data class CNavigation(
    val title: String,
    val path: String,
    val type: NavItemType
)

data class CFooter(
    val author: String,
    val year: String,
    val licenseName: String
)

data class CPlugins(val notes: CNotesPlugin)

data class CNotesPlugin(val sourceDir: String)
