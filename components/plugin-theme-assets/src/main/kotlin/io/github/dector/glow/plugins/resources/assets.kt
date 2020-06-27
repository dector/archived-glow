package io.github.dector.glow.plugins.resources

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.engine.GlowPipeline
import io.github.dector.glow.logger.logger
import io.github.dector.glow.utils.ThemeResourcesPath
import io.github.dector.ktx.div
import java.io.File

class ThemeAssetsPlugin(
    private val config: RuntimeConfig
) : GlowPipeline {

    override fun onExecute() {
        println("[=== Theme Assets ===]")
        copyThemeAssets()
    }

    private fun copyThemeAssets() {
        val themeResourcesDir = File(ThemeResourcesPath)

        if (!themeResourcesDir.exists()) {
            logger().error("Can't find theme resources at: ${themeResourcesDir.absolutePath}")
            return
        }

        val targetDir = config.glow.outputDir.let { dirPath ->
            val childPath = config.glow.assets.destinationPath
                .let { if (it.isAbsolute) it.root.relativize(it) else it }
            dirPath.resolve(childPath)
        }.toFile()
        themeResourcesDir.listFiles()!!
            .forEach { file ->
                val targetFile = targetDir / file.name

                println("${file.absolutePath} -> ${targetFile.absolutePath}")

                file.copyRecursively(targetFile, overwrite = config.glow.overrideFiles)
            }
    }
}
