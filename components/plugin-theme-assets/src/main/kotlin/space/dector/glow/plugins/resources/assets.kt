package space.dector.glow.plugins.resources

import space.dector.glow.config.RuntimeConfig
import space.dector.glow.engine.GlowPipeline
import space.dector.glow.logger.logger
import space.dector.glow.utils.ThemeResourcesPath
import space.dector.ktx.div
import java.io.File

class ThemeAssetsPlugin(
    private val config: RuntimeConfig
) : GlowPipeline {

    override fun onExecute() {
        println("[=== Theme Assets ===]")
        copyThemeAssets()
        copyFavicon()
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

    private fun copyFavicon() {
        val file = File(ThemeResourcesPath).parentFile.resolve("favicon.ico")

        if (!file.exists()) {
            logger().warn("favicon not found at '${file.absolutePath}'")
            return
        }

        val dest = config.glow.outputDir.resolve("favicon.ico").toFile()
        file.copyTo(dest, overwrite = config.glow.overrideFiles)
    }
}
