package io.github.dector.glow.server.components

import io.github.dector.glow.engine.DataPublisher
import io.github.dector.glow.engine.RenderedWebPage

class InMemoryDataPublisher(
    private val storage: MutableSet<RenderedWebPage>
) : DataPublisher {

    override fun publish(webPage: RenderedWebPage) {
        storage += webPage
    }
}
