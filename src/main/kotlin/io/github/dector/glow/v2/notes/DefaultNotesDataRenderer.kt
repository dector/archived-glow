package io.github.dector.glow.v2.notes

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.implementation.ProjectConfig
import io.github.dector.glow.v2.implementation.parser.MarkdownParser
import io.github.dector.glow.v2.templates.Templates

class DefaultNotesDataRenderer(
        private val pathResolver: NotesPathResolver,
        private val markdownParser: MarkdownParser<Node>,
        private val projectConfig: ProjectConfig,
        private val htmlRenderer: HtmlRenderer
) : NotesDataRenderer {

    override fun render(note: Note2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(note.content.value))

        val vm = createNoteVM(note, content)

        val renderedPage = Templates.note(vm, projectConfig.navigation)
        return WebPage(
                path = pathResolver.resolve(note),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    override fun renderNotesIndex(notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesIndex(notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        }, projectConfig.navigation)
        return WebPage(
                path = pathResolver.resolveNotesIndex(),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    override fun renderNotesArchive(notes: List<Note2>): WebPage {
        val renderedPage = Templates.notesArchive(notes.map {
            val content = htmlRenderer.render(markdownParser.parse(it.content.value))

            createNoteVM(it, content)
        }, projectConfig.navigation)
        return WebPage(
                path = pathResolver.resolveNotesArchive(),
                content = HtmlWebPageContent(renderedPage)
        )
    }

    private fun createNoteVM(note: Note2, content: String) = run {
        Note2VM(
                title = note.title,
                createdAt = note.createdAt,
                publishedAt = note.publishedAt,
                path = pathResolver.resolve(note),
                content = HtmlContent(content),
                previewContent = HtmlContent(content.substring(0..500))
        )
    }
}

