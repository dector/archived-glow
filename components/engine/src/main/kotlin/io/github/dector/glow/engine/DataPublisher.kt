package io.github.dector.glow.engine

interface DataPublisher {

    fun publish(webPage: WebPage)
    fun publish(rss: RssFeed)
}
