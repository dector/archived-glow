package io.github.dector.glow.engine

interface DataPublisher {

    @Deprecated("")
    fun publish(webPage: WebPage)

    @Deprecated("")
    fun publish(rss: RssFeed)

    fun publish(webPage: RenderedWebPage): Unit = TODO()
}
