package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.RenderContext
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.pipeline.GlowPipeline
import io.github.dector.glow.templates.hyde.notesNavigationItem
import org.slf4j.Logger


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val config: RuntimeConfig,
    private val runOptions: NotesPluginConfig,
    private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        println("[== Notes ==]")
        print("Loading... ")

        val (notes, loadingStats) = loadNotes()
        println("found ${loadingStats.total}, using: ${loadingStats.used}, dropped: ${loadingStats.dropped}")

        val blog = buildBlogVM(config.website)

        buildNotes(blog, notes)
        buildNotesIndex(blog, notes)
        buildTagsPages(blog, notes)
        buildArchive(blog, notes)
        copyAssets()
        //buildRss(blog, notes)

        println("")
    }

    private fun buildNotes(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotePages) return

        notes.forEach { note ->
            val noteName = note.sourceFile.nameWithoutExtension
            print("File: '$noteName' .")

            print(".") // processing
            val webPage = dataRenderer.render(blog, note)

            print(".") // publishing
            dataPublisher.publish(webPage)

            println()
        }
    }

    private fun buildNotesIndex(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotesIndex) return

        print("Index .")

        print(".") // processing
        val context = RenderContext(
            blog = blog,
            navigationItem = blog.notesNavigationItem()!!
        )

        val webPage = dataRenderer.renderNotesIndex(notes, context)

        print(".") // publishing
        dataPublisher.publish(webPage)

        // FIXME
        webPage.copy(path = WebPagePath("/index.html")).let {
            dataPublisher.publish(it)
        }

        println()
    }

    private fun buildTagsPages(blog: BlogVM, notes: List<Note>) {

        val tags = notes
            .flatMap(Note::tags)
            .distinct()

        tags.forEach { tag ->
            print("Tag: $tag .")

            val taggedNotes = notes.filter { tag in it.tags }

            print(".") // processing
            val webPage = dataRenderer.renderTagPage(blog, taggedNotes, tag)

            print(".") // publishing
            dataPublisher.publish(webPage)

            println()
        }
    }

    private fun buildArchive(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildArchive) return

        "Notes archive".log()
        "Processing...".log()
        val webPage = dataRenderer.renderNotesArchive(blog, notes)

        "Publishing...".log()
        dataPublisher.publish(webPage)
    }

    // FIXME
    private fun copyAssets() {
        if (!runOptions.copyAssets) return

        val src = config.glow.sourceDir.resolve("assets").toFile()
        val dest = config.glow.outputDir.resolve("assets").toFile()

        src.copyRecursively(dest, onError = { file, e ->
            System.err.println("Can't copy asset '${file.absolutePath}' because of ${e.message}")
            OnErrorAction.SKIP
        }, overwrite = config.glow.overrideFiles)
    }

    private fun loadNotes(): Pair<List<Note>, LoadingStats> {
        fun List<Note>.dropDraftsIfNeeded() = when {
            !config.glow.includeDrafts -> filterNot { it.isDraft }
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

        val stats = run {
            val total = allNotes.size
            val used = filteredNotes.size
            val dropped = total - used

            LoadingStats(total, used, dropped)
        }
        return filteredNotes to stats
    }


    private fun String.log() {
        logger.info(this)
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
}

private data class LoadingStats(
    val total: Int,
    val used: Int,
    val dropped: Int
)
