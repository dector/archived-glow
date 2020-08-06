package space.dector.glow.plugins.notes.renderers

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import space.dector.glow.coordinates.inHostPath
import space.dector.glow.core.parser.MarkdownParser
import space.dector.glow.engine.HtmlContent
import space.dector.glow.engine.RenderContext
import space.dector.glow.engine.RenderedWebPage
import space.dector.glow.plugins.notes.Note
import space.dector.glow.plugins.notes.NoteVM
import space.dector.glow.plugins.notes.NotesDataRenderer
import space.dector.glow.plugins.notes.NotesPathResolver
import space.dector.glow.plugins.notes.TagVM
import space.dector.glow.plugins.notes.formatters.formatPublishDate
import space.dector.glow.templates.hyde.DasLightTemplate
import space.dector.glow.theming.Template

class DefaultNotesDataRenderer(
    private val pathResolver: NotesPathResolver,
    private val markdownParser: MarkdownParser<Node>,
    private val htmlRenderer: HtmlRenderer,
    private val template: Template = DasLightTemplate()
) : NotesDataRenderer {

    override fun renderIndividualNote(note: Note, context: RenderContext): RenderedWebPage {
        val coordinates = pathResolver.coordinatesFor(note)

        val renderedPage = run {
            val markdown = note.content.value
            val content = htmlRenderer.render(
                markdownParser.parse(markdown))
            val vm = createNoteVM(note, content)
            template.note(vm, context)
        }

        return RenderedWebPage(
            coordinates = coordinates,
            content = renderedPage
        )
    }

    override fun renderNotesPage(notes: List<Note>, context: RenderContext): RenderedWebPage {
        val coordinates = pathResolver.coordinatesForNotesPage(context.paging.current)

        val renderedPage = run {
            val noteVMs = notes.map {
                val markdown = it.previewContent ?: it.content
                val content = htmlRenderer.render(markdownParser.parse(markdown.value))

                createNoteVM(it, content, isTrimmed = it.previewContent != null)
            }

            template.notesIndexPage(noteVMs, context)
        }

        return RenderedWebPage(
            coordinates = coordinates,
            content = renderedPage
        )
    }

    override fun renderTagPage(notes: List<Note>, tag: String, context: RenderContext): RenderedWebPage {
        val coordinates = pathResolver.coordinatesForTagPage(tag, context.paging.current)

        val renderedPage = run {
            val noteVMs = notes.map {
                val markdown = it.previewContent ?: it.content
                val content = htmlRenderer.render(markdownParser.parse(markdown.value))

                createNoteVM(it, content, isTrimmed = it.previewContent != null)
            }
            val tagVM = createTagVM(tag, pathResolver)
            template.tagPage(noteVMs, tagVM, context)
        }

        return RenderedWebPage(
            coordinates = coordinates,
            content = renderedPage
        )
    }

    private fun createNoteVM(note: Note, content: String, isTrimmed: Boolean = false) = run {
        val htmlContent = HtmlContent(content)

        val coordinates = pathResolver.coordinatesFor(note)
        NoteVM(
            rawModel = note,

            title = buildTitle(note),
            url = coordinates.inHostPath(),

            coordinates = coordinates,
            content = htmlContent,

            createdText = formatPublishDate(note.publishedAt),
            updatedText = note.updatedAt?.let { "updated ${formatPublishDate(it)}" } ?: "",
            publishedAndUpdatedStr = formatPublishedAndUpdatedStr(note),
            //publishedAtStr = formatPublishDate(note.publishedAt),

            // FIXME should be stripped before rendering
            previewContent = if (content.length <= MAX_SYMBOLS_IN_CONTENT_PREVIEW)
                htmlContent
            else HtmlContent(content.substring(0 until MAX_SYMBOLS_IN_CONTENT_PREVIEW)),
            isTrimmed = isTrimmed,

            tags = note.tags.map {
                createTagVM(it, pathResolver)
            }
        )
    }

    private fun buildTitle(note: Note): String {
        return if (note.isDraft) {
            "|Draft| ${note.title}"
        } else {
            note.title
        }
    }
}

private fun formatPublishedAndUpdatedStr(note: Note): String {
    val publishedStr = formatPublishDate(note.publishedAt)

    val wasUpdated = note.updatedAt != null && note.updatedAt != note.createdAt
    if (!wasUpdated) return publishedStr

    val updatedStr = formatPublishDate(note.updatedAt)
    return "$publishedStr (updated $updatedStr)"
}

private fun createTagVM(tag: String, pathResolver: NotesPathResolver) = TagVM(
    name = tag,
    url = pathResolver.coordinatesForTagPage(tag, 1).inHostPath()
)

private const val MAX_SYMBOLS_IN_CONTENT_PREVIEW = 500
