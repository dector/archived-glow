package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.config.ProjectConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val config: ProjectConfig,
    private val logger: Logger
) : GlowPipeline {

    private val steps = object {
        val notesIndex = true
        val archive = false
        val rss = false
        val copyAssets = true
    }

    override fun execute() {
        "Loading notes...".logn()

        val notes = dataProvider.fetchNotes()
            .filter { !it.isDraft }
            .filterNot { it.title.isEmpty() }

        "Found non-draft notes: ${notes.size}".log()

        val blog = buildBlogVM(config)

        notes.forEach { note ->
            " * ${note.sourceFile.nameWithoutExtension}".log()
            "Processing...".log()

            val webPage = dataRenderer.render(blog, note)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }
        "".log()

        if (steps.notesIndex) {
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

        if (steps.archive) {
            "Notes archive".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesArchive(blog, notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }

        // FIXME
        if (steps.copyAssets) {
            val src = config.blog.sourceDir.resolve("assets")
            val dest = config.blog.outputDir.resolve("assets")

            src.copyRecursively(dest, onError = { file, e ->
                System.err.println("Can't copy asset '${file.absolutePath}' because of ${e.message}")
                OnErrorAction.SKIP
            })
        }

        // FIXME implement as a separate plugin
        if (steps.rss) {
            val rss = dataRenderer.renderRss(blog, notes)

            dataPublisher.publish(rss)
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
    fun fetchNotes(): List<Note>
}

interface NotesDataRenderer {
    fun render(blog: BlogVM, note: Note): WebPage
    fun renderNotesIndex(blog: BlogVM, notes: List<Note>): WebPage
    fun renderNotesArchive(blog: BlogVM, notes: List<Note>): WebPage

    fun renderRss(blog: BlogVM, notes: List<Note>): RssFeed
}
