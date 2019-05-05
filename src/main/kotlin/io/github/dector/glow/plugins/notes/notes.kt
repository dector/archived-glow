package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class NotesPlugin(
        private val dataProvider: NotesDataProvider,
        private val dataRenderer: NotesDataRenderer,
        private val dataPublisher: DataPublisher,
        private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        "Loading notes...".logn()

        val notes = dataProvider.fetchNotes()
                .filter { !it.isDraft }

        "Found non-draft notes: ${notes.size}".log()

        notes.forEach { note ->
            " * ${note.sourceFile.nameWithoutExtension}".log()
            "Processing...".log()

            val webPage = dataRenderer.render(note)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }
        "".log()

        run {
            "Notes index".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesIndex(notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)

            "".log()
        }

        run {
            "Notes archive".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesArchive(notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }

        "".log()
    }

    private fun String.log() {
        logger.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }

}

interface NotesDataProvider {
    fun fetchNotes(): List<Note2>
}

interface NotesDataRenderer {
    fun render(note: Note2): WebPage
    fun renderNotesIndex(notes: List<Note2>): WebPage
    fun renderNotesArchive(notes: List<Note2>): WebPage
}