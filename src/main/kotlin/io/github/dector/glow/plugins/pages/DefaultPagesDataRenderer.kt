package io.github.dector.glow.plugins.pages

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.core.*
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.templates.Templates

class DefaultPagesDataRenderer(
        private val pathResolver: PagesPathResolver,
        private val markdownParser: MarkdownParser<Node>,
        private val projectConfig: ProjectConfig,
        private val htmlRenderer: HtmlRenderer
) : PagesDataRenderer {

    override fun render(page: Page2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(page.content.value))

        val vm = createPageVM(page, content)

        val renderedPage = Templates.page(vm, projectConfig.navigation)
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
}

