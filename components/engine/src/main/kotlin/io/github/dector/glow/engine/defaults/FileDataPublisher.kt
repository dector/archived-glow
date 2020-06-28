package io.github.dector.glow.engine.defaults

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.coordinates.inHostPath
import io.github.dector.glow.coordinates.withFile
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.ktx.ensureParentDirectoryExists
import java.io.File

class FileDataPublisher(
    config: RuntimeConfig
) : DataPublisher {

    private val outputDir = config.glow.outputDir.toFile()

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
