package io.github.dector.glow.v2.pipeline

import io.github.dector.glow.core.logger.UiLogger
import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.WebPage
import io.github.dector.glow.v2.core.components.DataPublisher


class NotesPipeline(
        val dataProvider: NotesDataProvider,
        val dataRenderer: NotesDataRenderer,
        val dataPublisher: DataPublisher
) : GlowPipeline {

    private val log = UiLogger   // TODO provide it

    override fun execute() {
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
        log.info(this)
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