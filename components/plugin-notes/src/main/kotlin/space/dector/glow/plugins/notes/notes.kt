package space.dector.glow.plugins.notes

import space.dector.glow.config.RuntimeConfig
import space.dector.glow.coordinates.Coordinates
import space.dector.glow.engine.BlogVM
import space.dector.glow.engine.DataPublisher
import space.dector.glow.engine.GlowPipeline
import space.dector.glow.engine.Paging
import space.dector.glow.engine.RenderContext
import space.dector.glow.engine.RenderedWebPage
import space.dector.glow.ui.UiConsole
import space.dector.glow.vm.buildBlogVM
import space.dector.ktx.progress


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
        val baseContext = createRenderContext(blog)

        index.publishedTags().forEach { tag ->
            progress("Tag: '$tag'") {
                val notesByPages = notes
                    .filter { tag in it.tags }
                    .chunked(PageSize)
                val totalPages = notesByPages.size

                for (pageNum in 1..totalPages) {
                    val notesInPage = notesByPages[pageNum - 1]

                    val context = baseContext.copy(
                        paging = Paging(pageNum, totalPages,
                            prevPage = when {
                                pageNum > 1 -> pathResolver.coordinatesForTagPage(tag, pageNum - 1)
                                else -> null
                            },
                            nextPage = when {
                                pageNum < totalPages -> pathResolver.coordinatesForTagPage(tag, pageNum + 1)
                                else -> null
                            }
                        ))

                    val webPage = +{ dataRenderer.renderTagPage(notesInPage, tag, context) }
                    +{ dataPublisher.publish(webPage) }
                }
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
    fun renderIndividualNote(note: Note, context: RenderContext): RenderedWebPage
    fun renderNotesPage(notes: List<Note>, context: RenderContext): RenderedWebPage
    fun renderTagPage(notes: List<Note>, tag: String, context: RenderContext): RenderedWebPage
}

data class NotesPluginConfig(
    val buildNotePages: Boolean = true,
    val buildNotesIndex: Boolean = true,
    val buildArchive: Boolean = false,
    val buildRss: Boolean = false,
    val copyAssets: Boolean = true
)
