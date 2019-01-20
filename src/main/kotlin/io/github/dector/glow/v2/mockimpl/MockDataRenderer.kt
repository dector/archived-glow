package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.DataRenderer
import io.github.dector.glow.v2.core.Page
import io.github.dector.glow.v2.core.RenderedPage
import io.github.dector.glow.v2.dumbimpl.utils.htmlPage
import kotlinx.html.unsafe

class MockDataRenderer(
        private val markdownParser: MarkdownParser<Node>
) : DataRenderer {

    private val htmlRenderer = buildRenderer()

    override fun render(page: Page): RenderedPage {
        val content = htmlRenderer.render(markdownParser.parse(page.markdownContent))

        val htmlContent = htmlPage(page.info.title) {
            unsafe {
                +content
            }
        }

        return RenderedPage(
                path = PagePath(page.info.id),
                content = htmlContent
        )
    }

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
}