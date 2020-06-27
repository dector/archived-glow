package io.github.dector.glow.engine.defaults

import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.engine.RssFeed
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.engine.isLost
import io.github.dector.glow.utils.prettyPrint
import io.github.dector.ktx.applyIf

class PreprocessedDataPublisher(
    private val nextPublisher: DataPublisher
) : DataPublisher {

    override fun publish(webPage: RenderedWebPage) {
        val prettyContent = webPage.content.prettyPrint()
        val prettyWebPage = webPage.copy(content = prettyContent)

        nextPublisher.publish(prettyWebPage)
    }

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages (that don't have path)
        if (webPage.path.isLost) {
            //log.warn("Skipping 'lost' page: ${webPage.content}")
            return
        }

        val transformedWebPage = webPage
            .applyIf(PRETTY_PRINT_HTML) {
                copy(content = content.prettyPrint())
            }

        nextPublisher.publish(transformedWebPage)
    }

    override fun publish(rss: RssFeed) = nextPublisher.publish(rss)

    companion object {
        @Deprecated("Use decorator properly")
        const val PRETTY_PRINT_HTML = true
    }
}
