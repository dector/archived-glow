package io.github.dector.glow.core.components

import io.github.dector.glow.core.RssFeed
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
        publish(webPage.path, webPage.content.value)
    }

    override fun publish(rss: RssFeed) {
        publish(WebPagePath(rss.filePath), rss.content)
    }

    private fun publish(path: WebPagePath, content: String) {
        val file = resolveFilePath(path)
            .ensureParentDirectoryExists()

        if (file.exists() && !overrideFiles) {
            log.warn("File '${file.absolutePath}' exists. Skipping.")
            return
        }

        file.writeText(content)
    }

    private fun resolveFilePath(path: WebPagePath): File {
        return outputDir
            .absoluteFile
            .toPath()
            .resolve(path.value.removePrefix("/"))
            .toFile()
    }
}
