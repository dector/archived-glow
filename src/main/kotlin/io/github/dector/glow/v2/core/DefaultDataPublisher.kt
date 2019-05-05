package io.github.dector.glow.v2.core

import io.github.dector.glow.core.logger.logger
import io.github.dector.glow.v2.core.components.DataPublisher
import java.io.File

class DefaultDataPublisher(
        private val config: ProjectConfig) : DataPublisher {

    private val log = logger()

    override fun publish(webPage: WebPage) {
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