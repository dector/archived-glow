package space.dector.glow.engine.defaults

import space.dector.glow.engine.DataPublisher
import space.dector.glow.engine.RenderedWebPage
import space.dector.glow.utils.prettyPrint

class PreprocessedDataPublisher(
    private val nextPublisher: DataPublisher
) : DataPublisher {

    override fun publish(webPage: RenderedWebPage) {
        val prettyContent = webPage.content.prettyPrint()
        val prettyWebPage = webPage.copy(content = prettyContent)

        nextPublisher.publish(prettyWebPage)
    }

    companion object {
        @Deprecated("Use decorator properly")
        const val PRETTY_PRINT_HTML = true
    }
}
