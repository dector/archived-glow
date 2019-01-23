package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.mockimpl.templates.Templates

class MockDataRenderer(
        private val pathResolver: PathResolver,
        private val markdownParser: MarkdownParser<Node>
) : DataRenderer {

    private val htmlRenderer = buildRenderer()

    override fun render(page: Page2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(page.content.value))

        val vm = createPageVM(page, content)

        val renderedPage = Templates.page(vm)
        return WebPage(
                path = pathResolver.resolve(page),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    private fun createPageVM(page: Page2, content: String) = run {
        Page2VM(
                title = page.title,
                createdAt = page.createdAt,
                path = pathResolver.resolve(page),
                content = HtmlContent(content)
        )
    }

    override fun renderNotesIndex(notes: List<Note>): RenderedPage {
        val processedNotes = notes.map { note ->
            render(note, asPage = false)
        }

        val content = Templates.notesIndex(processedNotes)

        return RenderedPage(
                path = pathResolver.notesIndex(),
                content = content
        )
    }

    override fun render(note: Note, asPage: Boolean): RenderedNote {
        val content = renderNoteContent(note)

        val htmlContent = if (asPage) Templates.note(note, content) else content
        return RenderedNote(
                path = NotePath(note.info.id),
                info = note.info,
                content = htmlContent
        )
    }

    private fun renderNoteContent(note: Note) =
            htmlRenderer.render(markdownParser.parse(note.markdownContent))

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
}

