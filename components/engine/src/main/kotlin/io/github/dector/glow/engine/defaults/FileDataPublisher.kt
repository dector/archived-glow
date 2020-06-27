package io.github.dector.glow.engine.defaults

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.coordinates.inHostPath
import io.github.dector.glow.coordinates.withFile
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.engine.RssFeed
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.engine.WebPagePath
import io.github.dector.glow.logger.logger
import io.github.dector.ktx.ensureParentDirectoryExists
import java.io.File

class FileDataPublisher(
    config: RuntimeConfig
) : DataPublisher {

    private val outputDir = config.glow.outputDir.toFile()
    private val overrideFiles = config.glow.overrideFiles

    private val log = logger()

    override fun publish(webPage: WebPage) {
        publish(webPage.path, webPage.content.value)
    }

    override fun publish(rss: RssFeed) {
        publish(WebPagePath(rss.filePath), rss.content)
    }

    @Deprecated("")
    private fun publish(path: WebPagePath, content: String) {
        val file = resolveFilePath(path)
            .ensureParentDirectoryExists()

        if (file.exists() && !overrideFiles) {
            log.warn("File '${file.absolutePath}' exists. Skipping.")
            return
        }

        file.writeText(content)
    }

    @Deprecated("Use `resolveFile(Coordinates.Resource)`")
    private fun resolveFilePath(path: WebPagePath): File {
        return outputDir
            .absoluteFile
            .toPath()
            .resolve(path.value.removePrefix("/"))
            .toFile()
    }

    override fun publish(webPage: RenderedWebPage) {
        val file = resolveFile(webPage.coordinates)
            .ensureParentDirectoryExists()

        file.writeText(webPage.content.value)
    }

    /**
     * Get reference to `index.html` file to write web page content.
     */
    private fun resolveFile(coordinates: Coordinates.Endpoint): File {
        val relativePath = coordinates
            .withFile("index.html")
            .inHostPath(useLeadingSlash = false)

        return outputDir
            .resolve(relativePath)
            .normalize()
    }
}
