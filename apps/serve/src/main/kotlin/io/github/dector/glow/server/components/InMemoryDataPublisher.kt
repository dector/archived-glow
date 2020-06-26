package io.github.dector.glow.server.components

import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.HtmlWebPageContent
import io.github.dector.glow.engine.RssFeed
import io.github.dector.glow.engine.WebPage
import io.github.dector.glow.engine.WebPagePath
import io.github.dector.glow.engine.isLost

class InMemoryDataPublisher(
    private val storage: MutableSet<WebPage>
) : DataPublisher {

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages
        if (webPage.path.isLost) return

        storage += webPage
    }

    override fun publish(rss: RssFeed) {
        storage += WebPage(
            path = WebPagePath(rss.filePath),
            content = HtmlWebPageContent(rss.content)
        )
    }
}
