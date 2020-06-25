package io.github.dector.glow.core.config

import io.github.dector.glow.config.project.CProject

@Deprecated("")
data class LegacyRuntimeConfig(
    val projectConfig: CProject,
    val notes: NotesPluginConfig
)

data class NotesPluginConfig(
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
)
