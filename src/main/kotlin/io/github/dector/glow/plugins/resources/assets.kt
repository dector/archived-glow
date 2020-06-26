package io.github.dector.glow.plugins.resources

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.div
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger
import java.io.File

class ThemeAssetsPlugin(
    private val config: RuntimeConfig,
    private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        copyThemeAssets()

        "Done".logn()
    }

    private fun copyThemeAssets() {
        "Copying theme assets...".log()

        val themeResourcesDir = File("templates-hyde/src/main/res/")

        if (!themeResourcesDir.exists()) {
            logger.error("Can't find theme resources at: ${themeResourcesDir.absolutePath}")
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

    private fun String.log() {
        logger.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }
}
