package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.GlowPipeline
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.engine.WebPagePath
import io.github.dector.ktx.progress


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

        val context = createRenderContext(blog)

        notes.forEach { note ->
            val noteName = note.sourceFile.nameWithoutExtension
            progress("File: '$noteName'") {
                val webPage = +{ dataRenderer.render(note, context) }
                +{ dataPublisher.publish(webPage) }
            }
        }
    }

    private fun buildNotesIndex(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotesIndex) return

        val context = createRenderContext(blog)
        progress("Index") {
            val webPage = +{ dataRenderer.renderNotesIndex(notes, context) }
            +{ dataPublisher.publish(webPage) }
            +{
                val indexHtml = webPage.copy(path = WebPagePath("/index.html"))
                dataPublisher.publish(indexHtml)
            }
        }
    }

    private fun buildTagsPages(blog: BlogVM, notes: List<Note>) {

        val tags = notes
            .flatMap(Note::tags)
            .distinct()

        val context = createRenderContext(blog)

        tags.forEach { tag ->
            val taggedNotes = notes.filter { tag in it.tags }

            progress("Tag: '$tag'") {
                val webPage = +{ dataRenderer.renderTagPage(taggedNotes, tag, context) }
                +{ dataPublisher.publish(webPage) }
            }
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
)
