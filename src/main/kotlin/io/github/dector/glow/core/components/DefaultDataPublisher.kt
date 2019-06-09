package io.github.dector.glow.core.components

import io.github.dector.glow.core.ProjectConfig
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.isLost
import io.github.dector.glow.core.logger.logger
import java.io.File

class DefaultDataPublisher(
        private val config: ProjectConfig) : DataPublisher {

    private val log = logger()

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages
        if (webPage.path.isLost) return

        val file = resolveFilePath(webPage.path)

        file.parentFile.mkdirs()

        if (file.exists() && !config.output.overrideFiles) {
            log.warn("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(webPage.content.value)
        }
    }

    private fun resolveFilePath(path: WebPagePath): File {
        return config.output.outputFolder
                .absoluteFile
                .toPath()
                .resolve(path.value.removePrefix("/"))
                .toFile()
    }
}
