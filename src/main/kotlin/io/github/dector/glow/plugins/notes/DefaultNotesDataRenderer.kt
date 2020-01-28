package io.github.dector.glow.plugins.notes

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.RenderContext
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.theming.Template
import io.github.dector.glow.formatPublishDate
import io.github.dector.glow.plugins.rss.buildRss
import io.github.dector.glow.plugins.rss.generate
import io.github.dector.glow.templates.hyde.HydeTemplate

class DefaultNotesDataRenderer(
    private val pathResolver: NotesPathResolver,
    private val markdownParser: MarkdownParser<Node>,
    private val htmlRenderer: HtmlRenderer,
    private val template: Template = HydeTemplate()
) : NotesDataRenderer {

    override fun render(blog: BlogVM, note: Note): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(note.content.value))

        val vm = createNoteVM(note, content)

        val renderedPage = template.note(blog, vm)
        return WebPage(
            path = pathResolver.resolve(note),
            content = renderedPage
        )
    }

    override fun renderNotesIndex(notes: List<Note>, context: RenderContext): WebPage {
        val noteVMs = notes.map {
            val markdown = it.previewContent ?: it.content
            val content = htmlRenderer.render(markdownParser.parse(markdown.value))

            createNoteVM(it, content, isTrimmed = it.previewContent != null)
        }
        val renderedPage = template.notesIndex(noteVMs, context)
        return WebPage(
            path = pathResolver.resolveNotesIndex(),
            content = renderedPage
        )
    }

    override fun renderTagPage(blog: BlogVM, notes: List<Note>, tag: String): WebPage {
        val renderedPage = template.tagPage(blog, notes.map {
            val markdown = it.previewContent ?: it.content
            val content = htmlRenderer.render(markdownParser.parse(markdown.value))

            createNoteVM(it, content, isTrimmed = it.previewContent != null)
        }, tag)
        return WebPage(
            path = pathResolver.resolveTagPage(tag),
            content = renderedPage
        )
    }

    override fun renderNotesArchive(blog: BlogVM, notes: List<Note>): WebPage {
        val renderedPage = template.notesArchive(blog, notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        })
        return WebPage(
            path = pathResolver.resolveNotesArchive(),
            content = renderedPage
        )
    }

    override fun renderRss(blog: BlogVM, notes: List<Note>): RssFeed {
        return RssFeed(
            filePath = "/rss/notes.xml",
            content = buildRss(blog, notes).generate()
        )
    }

    private fun createNoteVM(note: Note, content: String, isTrimmed: Boolean = false) = run {
        val htmlContent = HtmlContent(content)

        NoteVM(
            rawModel = note,

            title = note.title,
            path = pathResolver.resolve(note, buildUrlPath = true),
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
}

private fun formatPublishedAndUpdatedStr(note: Note): String {
    val publishedStr = formatPublishDate(note.publishedAt)

    val wasUpdated = note.updatedAt != null && note.updatedAt != note.createdAt
    if (!wasUpdated) return publishedStr

    val updatedStr = formatPublishDate(note.updatedAt)
    return "$publishedStr (updated $updatedStr)"
}

private const val MAX_SYMBOLS_IN_CONTENT_PREVIEW = 500

