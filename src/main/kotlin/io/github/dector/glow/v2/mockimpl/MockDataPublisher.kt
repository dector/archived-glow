package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.*
import java.io.File

class MockDataPublisher(
        private val config: ProjectConfig) : DataPublisher {

    override fun publish(webPage: WebPage) {
        val file = File(config.output.pagesFolder, webPage.path.value)

        file.parentFile.mkdirs()

        if (file.exists() && !config.output.overrideFiles) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(webPage.content.value)
        }
    }

    override fun publishPage(page: RenderedPage) {
        val file = File(config.output.pagesFolder, page.path.instancePath)

        file.parentFile.mkdirs()

        if (file.exists() && !config.output.overrideFiles) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(page.content)
        }
    }

    override fun publishNote(note: RenderedNote) {
        val file = File(config.output.notesFolder, noteFileName(note))

        file.parentFile.mkdirs()

        if (file.exists() && !config.output.overrideFiles) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(note.content)
        }
    }

    private fun noteFileName(note: RenderedNote) =
//            if (note.path.path == "index") "index.html"
//            else
            "${note.path.path}.html"

    override fun publishNotesIndex(htmlContent: String) {
        val file = File(config.output.notesFolder, "index.html")

        file.parentFile.mkdirs()

        if (file.exists() && !config.output.overrideFiles) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(htmlContent)
        }
    }

    override fun publish(data: ProcessedData) = error("")
}