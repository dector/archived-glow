package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.RenderContext
import io.github.dector.glow.core.config.RuntimeConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    config: RuntimeConfig,
    private val logger: Logger
) : GlowPipeline {

    private val config = config.notes
    private val projectConfig = config.projectConfig

    override fun execute() {
        "Loading notes...".logn()

        val notes = loadNotes()

        val blog = buildBlogVM(projectConfig)

        buildNotes(blog, notes)
        buildNotesIndex(blog, notes)
        buildTagsPages(blog, notes)
        buildArchive(blog, notes)
        copyAssets()
        buildRss(blog, notes)

        "".log()
    }

    private fun buildNotes(blog: BlogVM, notes: List<Note>) {
        if (!config.buildNotePages) return

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
        if (!config.buildNotesIndex) return

        "Notes index".log()
        "Processing...".log()

        val context = RenderContext(
            blog = blog
        )

        val webPage = dataRenderer.renderNotesIndex(notes, context)

        "Publishing...".log()
        dataPublisher.publish(webPage)

        // FIXME
        webPage.copy(path = WebPagePath("/index.html")).let {
            dataPublisher.publish(it)
        }

        "".log()
    }

    private fun buildTagsPages(blog: BlogVM, notes: List<Note>) {
        "Tag pages...".log()

        val tags = notes
            .flatMap(Note::tags)
            .distinct()

        tags.forEach { tag ->
            val notes = notes.filter { tag in it.tags }
            val webPage = dataRenderer.renderTagPage(blog, notes, tag)
            dataPublisher.publish(webPage)
        }

        "".log()
    }

    private fun buildArchive(blog: BlogVM, notes: List<Note>) {
        if (!config.buildArchive) return

        "Notes archive".log()
        "Processing...".log()
        val webPage = dataRenderer.renderNotesArchive(blog, notes)

        "Publishing...".log()
        dataPublisher.publish(webPage)
    }

    // FIXME
    private fun copyAssets() {
        if (!config.copyAssets) return

        val src = projectConfig.blog.sourceDir.resolve("assets")
        val dest = projectConfig.blog.outputDir.resolve("assets")

        src.copyRecursively(dest, onError = { file, e ->
            System.err.println("Can't copy asset '${file.absolutePath}' because of ${e.message}")
            OnErrorAction.SKIP
        }, overwrite = projectConfig.glow.output.overrideFiles)
    }

    // FIXME implement as a separate plugin
    private fun buildRss(blog: BlogVM, notes: List<Note>) {
        if (!config.buildRss) return

        val rss = dataRenderer.renderRss(blog, notes)

        dataPublisher.publish(rss)
    }

    private fun loadNotes(): List<Note> {
        fun List<Note>.dropDraftsIfNeeded() = when {
            !config.includeDrafts -> filterNot { it.isDraft }
            else -> this
        }

        fun List<Note>.dropEmptyNotes() = filter { it.content.value.isNotBlank() }

        fun List<Note>.ensureTitlesArePresent(): List<Note> = map {
            if (it.title.isNotBlank()) it
            else it.copy(title = "Untitled note ${it.hashCode()}")
        }

        val allNotes = dataProvider.fetchNotes()

        val filteredNotes = allNotes
            .dropDraftsIfNeeded()
            .dropEmptyNotes()
            .ensureTitlesArePresent()

        "Loaded ${allNotes.size} notes, using: ${filteredNotes.size}".log()

        return filteredNotes
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
    fun renderNotesIndex(notes: List<Note>, context: RenderContext): WebPage
    fun renderNotesArchive(blog: BlogVM, notes: List<Note>): WebPage

    fun renderTagPage(blog: BlogVM, notes: List<Note>, tag: String): WebPage

    fun renderRss(blog: BlogVM, notes: List<Note>): RssFeed
}
