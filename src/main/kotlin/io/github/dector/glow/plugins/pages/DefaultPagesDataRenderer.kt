package io.github.dector.glow.plugins.pages

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.detectNavItem
import io.github.dector.glow.templates.Templates

class DefaultPagesDataRenderer(
        private val pathResolver: PagesPathResolver,
        private val markdownParser: MarkdownParser<Node>,
        private val htmlRenderer: HtmlRenderer
) : PagesDataRenderer {

    override fun render(blog: BlogVM, page: Page2): WebPage {
        val content = htmlRenderer.render(markdownParser.parse(page.content.value))

        val vm = createPageVM(page, content)

        val pagePath = pathResolver.resolve(page)
        val navItem = blog.detectNavItem(pagePath)

        val renderedPage = Templates.page(blog, vm, navItem)
        return WebPage(
                path = pagePath,
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
