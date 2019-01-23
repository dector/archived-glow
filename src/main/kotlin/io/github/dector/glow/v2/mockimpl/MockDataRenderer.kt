package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.templates.Templates

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

    override fun render(note: Note2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(note.content.value))

        val vm = createNoteVM(note, content)

        val renderedPage = Templates.note(vm)
        return WebPage(
                path = pathResolver.resolve(note),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    override fun renderNotesIndex(notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesIndex(notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        })
        return WebPage(
                path = pathResolver.resolveNotesIndex(),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    override fun renderNotesArchive(notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesArchive(notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        })
        return WebPage(
                path = pathResolver.resolveNotesArchive(),
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

    private fun createNoteVM(note: Note2, content: String) = run {
        Note2VM(
                title = note.title,
                createdAt = note.createdAt,
                publishedAt = note.publishedAt,
                path = pathResolver.resolve(note),
                content = HtmlContent(content)
        )
    }

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
}

