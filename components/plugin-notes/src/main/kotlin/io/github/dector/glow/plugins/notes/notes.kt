package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.GlowPipeline
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.engine.WebPagePath


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val config: RuntimeConfig,
    private val runOptions: NotesPluginConfig
) : GlowPipeline {

    private val currentSection = config.website.navigation
        .first { it.sectionCode == "notes" }

    override fun execute() {
        println("[== Notes ==]")
        print("Loading... ")

        val (notes, loadingStats) = loadNotes(dataProvider, config)
        println("found ${loadingStats.total}, using: ${loadingStats.used}, dropped: ${loadingStats.dropped}")

        val blog = buildBlogVM(config.website)

        buildNotes(blog, notes)
        buildNotesIndex(blog, notes)
        buildTagsPages(blog, notes)
        //buildArchive(blog, notes)
        //buildRss(blog, notes)

        println("")
    }

    private fun buildNotes(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotePages) return

        notes.forEach { note ->
            val noteName = note.sourceFile.nameWithoutExtension
            print("File: '$noteName' .")

            print(".") // processing
            val context = createRenderContext(blog)
            val webPage = dataRenderer.render(note, context)

            print(".") // publishing
            dataPublisher.publish(webPage)

            println()
        }
    }

    private fun buildNotesIndex(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotesIndex) return

        print("Index .")

        print(".") // processing
        val context = createRenderContext(blog)

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
            val context = createRenderContext(blog)
            val webPage = dataRenderer.renderTagPage(taggedNotes, tag, context)

            print(".") // publishing
            dataPublisher.publish(webPage)

            println()
        }
    }

    private fun createRenderContext(blog: BlogVM) = RenderContext(
        blog = blog,
        currentNavSection = blog.navigation.first { it.path == currentSection.path }
    )
}

interface NotesDataProvider {
    fun fetchNotes(): List<Note>
}

interface NotesDataRenderer {
    fun render(note: Note, context: RenderContext): WebPage
    fun renderNotesIndex(notes: List<Note>, context: RenderContext): WebPage
    fun renderNotesArchive(notes: List<Note>, context: RenderContext): WebPage

    fun renderTagPage(notes: List<Note>, tag: String, context: RenderContext): WebPage
}

data class NotesPluginConfig(
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
) {

    companion object {
        val get by lazy { NotesPluginConfig() }
    }
}
