package io.github.dector.glow.plugins.notes

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.engine.HtmlContent
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.plugins.notes.formatters.formatPublishDate
import io.github.dector.glow.templates.hyde.HydeTemplate
import io.github.dector.glow.theming.Template

class DefaultNotesDataRenderer(
    private val pathResolver: NotesPathResolver,
    private val markdownParser: MarkdownParser<Node>,
    private val htmlRenderer: HtmlRenderer,
    private val template: Template = HydeTemplate()
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
            template.tagPage(noteVMs, tag, context)
        }

        return RenderedWebPage(
            coordinates = coordinates,
            content = renderedPage
        )
    }

    override fun renderNotesArchive(notes: List<Note>, context: RenderContext): WebPage {
        val renderedPage = template.notesArchive(notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        }, context)
        return WebPage(
            path = pathResolver.resolveNotesArchive(),
            content = renderedPage
        )
    }

    private fun createNoteVM(note: Note, content: String, isTrimmed: Boolean = false) = run {
        val htmlContent = HtmlContent(content)

        NoteVM(
            rawModel = note,

            title = buildTitle(note),
            path = pathResolver.coordinatesFor(note).asWebPagePath(),
            content = htmlContent,

            publishedAndUpdatedStr = formatPublishedAndUpdatedStr(note),
            //publishedAtStr = formatPublishDate(note.publishedAt),

            // FIXME should be stripped before rendering
            previewContent = if (content.length <= MAX_SYMBOLS_IN_CONTENT_PREVIEW)
                htmlContent
            else HtmlContent(content.substring(0 until MAX_SYMBOLS_IN_CONTENT_PREVIEW)),
            isTrimmed = isTrimmed
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

private const val MAX_SYMBOLS_IN_CONTENT_PREVIEW = 500

