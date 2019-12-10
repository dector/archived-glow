package io.github.dector.glow.core.components

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.core.isLost
import io.github.dector.glow.utils.prettyPrint
import io.github.dector.glow.utils.transformIf

class PrettyPrintDataPublisher(
    private val nextPublisher: DataPublisher
) : DataPublisher {

    //private val log = logger()

    override fun publish(webPage: WebPage) {
        // Do not publish "lost" pages (that don't have path)
        if (webPage.path.isLost) {
            //log.warn("Skipping 'lost' page: ${webPage.content}")
            return
        }

        val transformedWebPage = webPage
            .transformIf(PRETTY_PRINT_HTML) {
                it.copy(content = it.content.prettyPrint())
            }

        nextPublisher.publish(transformedWebPage)
    }

    companion object {
        const val PRETTY_PRINT_HTML = true
    }
}
