package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.core.vm.buildBlogVM
import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.GlowPipeline
import io.github.dector.glow.engine.Paging
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.ui.UiConsole
import io.github.dector.ktx.progress


class NotesPlugin(
    private val dataProvider: NotesDataProvider,
    private val dataRenderer: NotesDataRenderer,
    private val dataPublisher: DataPublisher,
    private val pathResolver: NotesPathResolver,
    private val config: RuntimeConfig,
    private val runOptions: NotesPluginConfig
) : GlowPipeline {

    private val currentSection = config.website.navigation
        .first { it.sectionCode == "notes" }

    private val ui = UiConsole.get

    private val index = NotesIndex()

    override fun onIndex() {
        ui.println("Indexing notes... ")

        val stats = index.populateFrom(dataProvider,
            includeDrafts = config.glow.includeDrafts,
            includeEmpty = config.glow.includeDrafts
        )

        ui.println("found ${stats.total}, using: ${stats.used}, dropped: ${stats.dropped}")
    }

    override fun onExecute() {
        ui.println("[== Notes ==]")

        val blog = buildBlogVM(config.website)

        val notes = index.notesToPublish
        buildIndividualNotePages(blog, notes)
        buildNotesIndexPages(blog, notes)
        buildTagsPages(blog, notes)
        //buildArchive(blog, notes)
        //buildRss(blog, notes)

        println("")
    }

    private fun buildIndividualNotePages(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotePages) return

        val context = createRenderContext(blog)

        notes.forEach { note ->
            val noteName = note.sourceFile.nameWithoutExtension
            progress("File: '$noteName'") {
                val webPage = +{ dataRenderer.renderIndividualNote(note, context) }
                +{ dataPublisher.publish(webPage) }
            }
        }
    }

    private fun buildNotesIndexPages(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildNotesIndex) return

        val baseContext = createRenderContext(blog)

        val notesByPages = notes.chunked(PageSize)
        val totalPages = notesByPages.size

        progress("Notes pages ($totalPages)") {
            for (pageNum in 1..totalPages) {
                val notesInPage = notesByPages[pageNum - 1]

                val context = baseContext.copy(
                    paging = Paging(pageNum, totalPages,
                        prevPage = when {
                            pageNum > 1 -> pathResolver.coordinatesForNotesPage(pageNum - 1)
                            else -> null
                        },
                        nextPage = when {
                            pageNum < totalPages -> pathResolver.coordinatesForNotesPage(pageNum + 1)
                            else -> null
                        }
                    ))

                val webPage = +{ dataRenderer.renderNotesPage(notesInPage, context) }
                +{ dataPublisher.publish(webPage) }

                if (pageNum == 1) +{
                    val indexHtml = webPage.copy(coordinates = Coordinates.Empty)
                    dataPublisher.publish(indexHtml)
                }
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
        currentNavSection = blog.navigation.first { it.path == currentSection.path },
        paging = Paging(1, 1)
    )

    private companion object {
        const val PageSize = 5
    }
}

interface NotesDataProvider {
    fun fetchNotes(): List<Note>
}

interface NotesDataRenderer {
    fun renderNotesArchive(notes: List<Note>, context: RenderContext): WebPage
    fun renderTagPage(notes: List<Note>, tag: String, context: RenderContext): WebPage

    fun renderIndividualNote(note: Note, context: RenderContext): RenderedWebPage
    fun renderNotesPage(notes: List<Note>, context: RenderContext): RenderedWebPage
}

data class NotesPluginConfig(
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
)
