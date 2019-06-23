package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.provideBlogVM
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val config: Config,
    private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        "Loading notes...".logn()

        val notes = dataProvider.fetchNotes()
            .filter { !it.isDraft }
            .filterNot { it.title.isEmpty() }

        "Found non-draft notes: ${notes.size}".log()

        val blog = provideBlogVM(config)

        notes.forEach { note ->
            " * ${note.sourceFile.nameWithoutExtension}".log()
            "Processing...".log()

            val webPage = dataRenderer.render(blog, note)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }
        "".log()

        run {
            "Notes index".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesIndex(blog, notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)

            // FIXME
            webPage.copy(path = WebPagePath("/index.html")).let {
                dataPublisher.publish(it)
            }

            "".log()
        }

        run {
            "Notes archive".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesArchive(blog, notes)

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
    fun render(blog: BlogVM, note: Note2): WebPage
    fun renderNotesIndex(blog: BlogVM, notes: List<Note2>): WebPage
    fun renderNotesArchive(blog: BlogVM, notes: List<Note2>): WebPage
}
