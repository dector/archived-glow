package io.github.dector.glow.core.config

import io.github.dector.glow.config.LegacyProjectConfig

@Deprecated("")
data class LegacyRuntimeConfig(
    val projectConfig: LegacyProjectConfig,
    val notes: NotesPluginConfig
)

data class NotesPluginConfig(
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
)
