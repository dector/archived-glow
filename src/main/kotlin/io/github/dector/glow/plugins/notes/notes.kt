package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.config.RuntimeConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val config: RuntimeConfig,
    private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        "Loading notes...".logn()

        val projectConfig = config.projectConfig

        val notes = dataProvider.fetchNotes()
            .filterDrafts()
            .ensureTitlesArePresent()

        "Found non-draft notes: ${notes.size}".log()

        val blog = buildBlogVM(projectConfig)

        buildNotes(blog, notes)
        buildNotesIndex(blog, notes)
        buildArchive(blog, notes)
        copyAssets()
        buildRss(blog, notes)

        "".log()
    }

    private fun buildNotes(blog: BlogVM, notes: List<Note>) {
        if (!config.notes.buildNotePages) return

        notes.forEach { note ->
            " * ${note.sourceFile.nameWithoutExtension}".log()
            "Processing...".log()

            val webPage = dataRenderer.render(blog, note)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }
        "".log()
    }

    private fun buildNotesIndex(blog: BlogVM, notes: List<Note>) {
        if (!config.notes.buildNotesIndex) return

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

    private fun buildArchive(blog: BlogVM, notes: List<Note>) {
        if (!config.notes.buildArchive) return

        "Notes archive".log()
        "Processing...".log()
        val webPage = dataRenderer.renderNotesArchive(blog, notes)

        "Publishing...".log()
        dataPublisher.publish(webPage)
    }

    // FIXME
    private fun copyAssets() {
        if (!config.notes.copyAssets) return

        val src = config.projectConfig.blog.sourceDir.resolve("assets")
        val dest = config.projectConfig.blog.outputDir.resolve("assets")

        src.copyRecursively(dest, onError = { file, e ->
            System.err.println("Can't copy asset '${file.absolutePath}' because of ${e.message}")
            OnErrorAction.SKIP
        })
    }

    // FIXME implement as a separate plugin
    private fun buildRss(blog: BlogVM, notes: List<Note>) {
        if (!config.notes.buildRss) return

        val rss = dataRenderer.renderRss(blog, notes)

        dataPublisher.publish(rss)
    }

    private fun List<Note>.filterDrafts(): List<Note> {
        if (config.notes.includeDrafts) return this

        return filter { !it.isDraft }
    }

    private fun List<Note>.ensureTitlesArePresent(): List<Note> =
        map {
            if (it.title.isNotBlank()) it
            else it.copy(title = "Untitled ${it.hashCode()}")
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
