package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.DataPublisher
import io.github.dector.glow.v2.core.ProcessedData
import io.github.dector.glow.v2.core.RenderedPage
import java.io.File

class MockDataPublisher(
        private val config: ProjectConfig) : DataPublisher {

    override fun publishPage(page: RenderedPage) {
        val file = File(config.output.pagesFolder, pageFileName(page))
        file.parentFile.mkdirs()

        if (file.exists()) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(page.content)
        }

    }

    private fun pageFileName(page: RenderedPage) = "${page.path.path}.html"

    override fun publish(data: ProcessedData) = error("")
}