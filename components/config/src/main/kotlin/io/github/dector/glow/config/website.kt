package io.github.dector.glow.config

import java.nio.file.Path


interface ProjectConfig {
    val projectDir: Path

//    val glow: GlowConfig
//    val website: WebsiteConfig

    @Deprecated("Remove it")
    val legacy: LegacyProjectConfig
}

//interface GlowConfig
//interface WebsiteConfig

@Deprecated("Temporary")
class SimpleProjectConfig(
    override val projectDir: Path,
    override val legacy: LegacyProjectConfig
) : ProjectConfig
