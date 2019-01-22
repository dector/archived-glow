package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.mockimpl.templates.Templates

class MockDataRenderer(
        private val markdownParser: MarkdownParser<Node>
) : DataRenderer {

    private val htmlRenderer = buildRenderer()

    override fun render(page: Page): RenderedPage {
        val content = htmlRenderer.render(markdownParser.parse(page.markdownContent))

        val htmlContent = Templates.page(page, content)
        return RenderedPage(
                path = PagePath(page.info.id),
                content = htmlContent
        )
    }

    override fun renderNotesIndex(notes: List<NoteInfo>) = run {
        Templates.notesIndex(notes)
    }

    override fun render(note: Note): RenderedNote {
        val content = htmlRenderer.render(markdownParser.parse(note.markdownContent))

        val htmlContent = Templates.note(note, content)
        return RenderedNote(
                path = NotePath(note.info.id),
                content = htmlContent
        )
    }

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
}

