package io.github.dector.glow.plugins.pages

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.pipeline.GlowPipeline
import org.slf4j.Logger


class PagesPlugin(
        private val dataProvider: PagesDataProvider,
        private val dataRenderer: PagesDataRenderer,
        private val dataPublisher: DataPublisher,
        private val logger: Logger
) : GlowPipeline {

    override fun execute() {
        "Loading pages...".logn()

        val pages = dataProvider.fetchPages()

        "Found pages: ${pages.size}".log()

        pages.forEach { page ->
            "Processing '${page.title}'".log()

            val webPage = dataRenderer.render(page)

            "Publishing '${page.title}'".log()
            dataPublisher.publish(webPage)
        }
        "".log()
    }

    private fun String.log() {
        logger.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }
}

interface PagesDataProvider {
    fun fetchPages(): List<Page2>
}

interface PagesDataRenderer {
    fun render(page: Page2): WebPage
}