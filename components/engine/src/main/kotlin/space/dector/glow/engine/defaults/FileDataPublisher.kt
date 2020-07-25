package space.dector.glow.engine.defaults

import space.dector.glow.config.RuntimeConfig
import space.dector.glow.coordinates.Coordinates
import space.dector.glow.coordinates.inHostPath
import space.dector.glow.coordinates.withFile
import space.dector.glow.engine.DataPublisher
import space.dector.glow.engine.RenderedWebPage
import space.dector.ktx.ensureParentDirectoryExists
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
