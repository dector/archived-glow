package io.github.dector.glow.core.components

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.logger.logger
import io.github.dector.glow.ensureParentDirectoryExists
import java.io.File

class FileDataPublisher(
    config: Config
) : DataPublisher {

    private val outputDir = config.blog.outputDir
    private val overrideFiles = config.glow.output.overrideFiles

    private val log = logger()

    override fun publish(webPage: WebPage) {
        val file = resolveFilePath(webPage.path)
            .ensureParentDirectoryExists()

        if (file.exists() && !overrideFiles) {
            log.warn("File '${file.absolutePath}' exists. Skipping.")
            return
        }

        file.writeText(webPage.content.value)
    }

    private fun resolveFilePath(path: WebPagePath): File {
        return outputDir
            .absoluteFile
            .toPath()
            .resolve(path.value.removePrefix("/"))
            .toFile()
    }
}
