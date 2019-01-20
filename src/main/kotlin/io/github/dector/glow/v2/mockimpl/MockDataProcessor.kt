package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.DataProcessor
import io.github.dector.glow.v2.core.Page
import io.github.dector.glow.v2.core.RenderedPage

class MockDataProcessor : DataProcessor {

    override fun render(page: Page): RenderedPage {
        return RenderedPage(
                path = PagePath("${page.info.id}-${page.info.title}"),
                content = "Page with title '${page.info.title}'"
        )
    }
}