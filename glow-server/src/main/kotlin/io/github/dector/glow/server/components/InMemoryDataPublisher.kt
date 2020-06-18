package io.github.dector.glow.server.components

import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.isLost

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
