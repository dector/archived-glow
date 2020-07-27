package space.dector.glow.config

import org.hjson.JsonValue
import space.dector.glow.config.project.ConfigWrapper
import space.dector.ktx.div
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

    val cname: String?

    val notes: NotesConfig
    val assets: AssetsConfig

    data class Default(
        override val configVersion: String,
        override val sourceDir: Path,
        override val outputDir: Path,
        override val includeDrafts: Boolean,
        override val overrideFiles: Boolean,
        override val cname: String?,
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

    val githubUser: String

    val navigation: List<NavigationEntry>

    data class Default(
        override val title: String,
        override val footerAuthor: String,
        override val footerYear: String,
        override val footerLicenseName: String,
        override val footerLicenseUrl: String,
        override val githubUser: String,
        override val navigation: List<NavigationEntry>
    ) : WebsiteConfig
}

data class NavigationEntry(val path: String, val title: String, val sectionCode: String)

internal fun buildRuntimeConfig(
    projectDir: Path,
    config: ConfigWrapper,
    launchConfig: LaunchConfig
): RuntimeConfig {
    return object : RuntimeConfig {
        override val projectDir = projectDir

        override val glow = GlowConfig.Default(
            configVersion = config["glow"]["config"].str("version"),
            sourceDir = config.path { it["blog"].str("sourceDir") },
            outputDir = config.path { it["blog"].str("outputDir") },

            includeDrafts = launchConfig.includeDrafts,
            overrideFiles = config["glow"]["output"].bool("overrideFiles"),
            cname = config["plugins"]["domain"].str("cname"),

            notes = NotesConfig.Default(
                sourceDir = config.path { it["plugins"]["notes"].str("sourceDir") },
                destinationPath = config["plugins"]["notes"].str("path")
            ),

            assets = AssetsConfig.Default(
                destinationPath = config.path { config["glow"]["assets"].str("targetPath") }
            )
        )

        override val website = WebsiteConfig.Default(
            title = config["blog"].str("title"),
            footerAuthor = config["blog"]["footer"].str("author"),
            footerLicenseName = config["blog"]["footer"].str("licenseName"),
            footerLicenseUrl = config["blog"]["footer"].str("licenseUrl"),
            footerYear = config["blog"]["footer"].int("year").toString(),

            githubUser = config["blog"].str("githubUser"),

            navigation = config["blog"]["navigation"].asArray().map {
                NavigationEntry(
                    path = it.str("path"), title = it.str("title"), sectionCode = it.str("type")
                )
            }
        )
    }
}

internal operator fun ConfigWrapper.get(name: String): JsonValue = root[name]
internal operator fun JsonValue.get(name: String): JsonValue = asObject()[name]

internal fun JsonValue.str(name: String): String =
    asObject()[name].asString() ?: error("Can't find string '$name'")

internal fun JsonValue.int(name: String): Int = asObject().run {
    val value = this[name] ?: error("Can't find int '$name'")

    value.asInt()
}

internal fun JsonValue.bool(name: String): Boolean = asObject().run {
    val value = this[name] ?: error("Can't find boolean '$name'")

    value.asBoolean()
}

internal fun ConfigWrapper.path(accessor: (ConfigWrapper) -> String): Path {
    val filename = accessor(this)
    return (context.dir / filename).toPath()
}
