package io.github.dector.glow.config

import io.github.dector.glow.config.project.CProject
import java.nio.file.Path


interface RuntimeConfig {
    val projectDir: Path

    val glow: GlowConfig
    val website: WebsiteConfig
}

interface GlowConfig {
    val configVersion: String

    val sourceDir: Path
    val outputDir: Path

    val includeDrafts: Boolean
    val overrideFiles: Boolean

    val notes: NotesConfig
    val assets: AssetsConfig

    data class Default(
        override val configVersion: String,
        override val sourceDir: Path,
        override val outputDir: Path,
        override val includeDrafts: Boolean,
        override val overrideFiles: Boolean,
        override val notes: NotesConfig,
        override val assets: AssetsConfig
    ) : GlowConfig
}

interface NotesConfig {
    val sourceDir: Path
    val destinationPath: String

    data class Default(
        override val sourceDir: Path,
        override val destinationPath: String
    ) : NotesConfig
}

interface AssetsConfig {
    val destinationPath: Path

    data class Default(
        override val destinationPath: Path
    ) : AssetsConfig
}

interface WebsiteConfig {
    val title: String
    val footerAuthor: String
    val footerYear: String
    val footerLicenseName: String
    val footerLicenseUrl: String

    val navigation: List<NavigationEntry>

    data class Default(
        override val title: String,
        override val footerAuthor: String,
        override val footerYear: String,
        override val footerLicenseName: String,
        override val footerLicenseUrl: String,
        override val navigation: List<NavigationEntry>
    ) : WebsiteConfig
}

data class NavigationEntry(val path: String, val title: String, val sectionCode: String)

fun buildRuntimeConfig(
    projectDir: Path,
    projectConfig: CProject,
    launchConfig: LaunchConfig
): RuntimeConfig {
    return object : RuntimeConfig {
        override val projectDir = projectDir

        override val glow = GlowConfig.Default(
            configVersion = projectConfig.glow.config.version,
            sourceDir = projectConfig.blog.sourceDir.toPath(),
            outputDir = projectConfig.blog.outputDir.toPath(),

            includeDrafts = launchConfig.includeDrafts,
            overrideFiles = projectConfig.glow.output.overrideFiles,

            notes = NotesConfig.Default(
                sourceDir = projectConfig.plugins.notes.sourceDir.toPath(),
                destinationPath = projectConfig.plugins.notes.path
            ),

            assets = AssetsConfig.Default(
                destinationPath = projectConfig.glow.assets.targetPath
            )
        )

        override val website = WebsiteConfig.Default(
            title = projectConfig.blog.title,
            footerAuthor = projectConfig.blog.footer.author,
            footerLicenseName = projectConfig.blog.footer.licenseName,
            footerLicenseUrl = projectConfig.blog.footer.licenseUrl,
            footerYear = projectConfig.blog.footer.year,
            navigation = listOf(
                NavigationEntry("/notes", "Notes", "notes")
            )
        )
    }
}
