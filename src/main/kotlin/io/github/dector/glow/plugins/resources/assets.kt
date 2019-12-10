package io.github.dector.glow.plugins.resources

import io.github.dector.glow.core.config.Config
import io.github.dector.glow.div
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger
import java.io.File

class ThemeAssetsPlugin(
    private val config: Config,
    private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        copyThemeAssets()

        "Done".logn()
    }

    private fun copyThemeAssets() {
        "Copying theme assets...".log()

        val themeName = config.glow._theme
        val themeResourcesDir = File("templates-$themeName/src/main/res/")

        if (!themeResourcesDir.exists()) {
            logger.error("Can't find theme resources at: ${themeResourcesDir.absolutePath}")
            return
        }

        val targetDir = config.blog.outputDir / "public"
        themeResourcesDir.listFiles()!!
            .forEach { file ->
                val targetFile = targetDir / file.name

                println("${file.absolutePath} -> ${targetFile.absolutePath}")

                file.copyRecursively(targetFile, overwrite = config.glow.output.overrideFiles)
            }
    }

    private fun String.log() {
        logger.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }
}
