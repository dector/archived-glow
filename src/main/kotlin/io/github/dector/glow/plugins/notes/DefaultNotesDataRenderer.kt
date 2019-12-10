package io.github.dector.glow.plugins.notes

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.formatPublishDate
import io.github.dector.glow.plugins.rss.buildRss
import io.github.dector.glow.plugins.rss.generate
import io.github.dector.glow.templates.Templates

class DefaultNotesDataRenderer(
    private val pathResolver: NotesPathResolver,
    private val markdownParser: MarkdownParser<Node>,
    private val htmlRenderer: HtmlRenderer
) : NotesDataRenderer {

    override fun render(blog: BlogVM, note: Note2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(note.content.value))

        val vm = createNoteVM(note, content)

        val renderedPage = Templates.note(blog, vm)
        return WebPage(
            path = pathResolver.resolve(note),
            content = renderedPage
        )
    }

    override fun renderNotesIndex(blog: BlogVM, notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesIndex(blog, notes.map {
            val markdown = it.previewContent ?: it.content
            val content = htmlRenderer.render(markdownParser.parse(markdown.value))

            createNoteVM(it, content, isTrimmed = it.previewContent != null)
        })
        return WebPage(
            path = pathResolver.resolveNotesIndex(),
            content = renderedPage
        )
    }

    override fun renderNotesArchive(blog: BlogVM, notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesArchive(blog, notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        })
        return WebPage(
            path = pathResolver.resolveNotesArchive(),
            content = HtmlWebPageContent(renderedPage)
        )
    }

    override fun renderRss(blog: BlogVM, notes: List<Note2>): RssFeed {
        return RssFeed(
            filePath = "/rss/notes.xml",
            content = buildRss(blog, notes).generate()
        )
    }

    private fun createNoteVM(note: Note2, content: String, isTrimmed: Boolean = false) = run {
        val htmlContent = HtmlContent(content)

        Note2VM(
            title = note.title,
            createdAt = note.createdAt,
            publishedAt = note.publishedAt,
            publishedAtValue = formatPublishDate(note.publishedAt),
            path = pathResolver.resolve(note, buildUrlPath = true),
            content = htmlContent,
            // FIXME should be stripped before rendering
            previewContent = if (content.length <= MAX_SYMBOLS_IN_CONTENT_PREVIEW)
                htmlContent
            else HtmlContent(content.substring(0 until MAX_SYMBOLS_IN_CONTENT_PREVIEW)),
            isTrimmed = isTrimmed
        )
    }
}

private const val MAX_SYMBOLS_IN_CONTENT_PREVIEW = 500

