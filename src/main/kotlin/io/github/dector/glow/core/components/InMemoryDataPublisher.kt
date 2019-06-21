package io.github.dector.glow.core.components

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.isLost
import io.github.dector.glow.core.logger.logger

class InMemoryDataPublisher(
    private val storage: MutableSet<WebPage>
) : DataPublisher {

    private val log = logger()

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages
        if (webPage.path.isLost) return

        storage += webPage
    }
}
