package io.github.dector.glow.config

import java.nio.file.Path


interface ProjectConfig {
    val projectDir: Path

    val glow: GlowConfig
//    val website: WebsiteConfig

    @Deprecated("Remove it")
    val legacy: LegacyProjectConfig
}

interface GlowConfig {
    val configVersion: String

    val includeDrafts: Boolean
}

//interface WebsiteConfig

@Deprecated("Temporary")
class SimpleProjectConfig(
    override val projectDir: Path,
    override val legacy: LegacyProjectConfig,
    launchConfig: LaunchConfig
) : ProjectConfig {

    override val glow = RealGlowConfig(
        configVersion = legacy.glow.config.version,
        includeDrafts = launchConfig.includeDrafts
    )
}

data class RealGlowConfig(
    override val configVersion: String,
    override val includeDrafts: Boolean
) : GlowConfig
