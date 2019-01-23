package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.DataPublisher
import io.github.dector.glow.v2.core.ProcessedData
import io.github.dector.glow.v2.core.RenderedNote
import io.github.dector.glow.v2.core.WebPage
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