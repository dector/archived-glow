package space.dector.glow.server.components

import space.dector.glow.engine.DataPublisher
import space.dector.glow.engine.RenderedWebPage

class InMemoryDataPublisher(
    private val storage: MutableSet<RenderedWebPage>
) : DataPublisher {

    override fun publish(webPage: RenderedWebPage) {
        storage += webPage
    }
}
