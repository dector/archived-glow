package io.github.dector.glow.core.config

data class RuntimeConfig(
    val projectConfig: ProjectConfig,
    val notes: NotesPluginConfig
)

data class NotesPluginConfig(
    val includeDrafts: Boolean = false,
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
)
