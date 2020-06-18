package io.github.dector.glow.plugins.resources

import io.github.dector.glow.config.LegacyProjectConfig
import io.github.dector.glow.div
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger
import java.io.File

class ThemeAssetsPlugin(
    private val config: LegacyProjectConfig,
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

        val targetDir = config.blog.outputDir.toPath().let { dirPath ->
            val childPath = config.glow.assets.targetPath
                .let { if (it.isAbsolute) it.root.relativize(it) else it }
            dirPath.resolve(childPath)
        }.toFile()
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
