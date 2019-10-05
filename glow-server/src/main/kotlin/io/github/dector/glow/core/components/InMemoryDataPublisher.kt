package io.github.dector.glow.core.components

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.isLost

class InMemoryDataPublisher(
    private val storage: MutableSet<WebPage>
) : DataPublisher {

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages
        if (webPage.path.isLost) return

        storage += webPage
    }
}
