package io.github.dector.glow.config

import io.github.dector.glow.config.project.CProject
import java.nio.file.Path


interface RuntimeConfig {
    val projectDir: Path

    val glow: GlowConfig
//    val website: WebsiteConfig

    @Deprecated("Remove it")
    val legacy: CProject
}

interface GlowConfig {
    val configVersion: String

    val includeDrafts: Boolean

    val notes: NotesConfig

    data class Default(
        override val configVersion: String,
        override val includeDrafts: Boolean,
        override val notes: NotesConfig
    ) : GlowConfig
}

interface NotesConfig {
    val sourceDir: Path

    data class Default(
        override val sourceDir: Path
    ) : NotesConfig
}

//interface WebsiteConfig

fun buildRuntimeConfig(
    projectDir: Path,
    projectConfig: CProject,
    launchConfig: LaunchConfig
): RuntimeConfig {
    return object : RuntimeConfig {
        override val projectDir = projectDir

        override val glow = GlowConfig.Default(
            configVersion = projectConfig.glow.config.version,
            includeDrafts = launchConfig.includeDrafts,
            notes = NotesConfig.Default(
                sourceDir = projectConfig.plugins.notes.sourceDir.toPath()
            )
        )

        override val legacy = projectConfig
    }
}
