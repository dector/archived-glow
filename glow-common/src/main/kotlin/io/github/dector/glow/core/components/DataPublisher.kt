package io.github.dector.glow.core.components

import io.github.dector.glow.core.RssFeed
import io.github.dector.glow.core.WebPage

interface DataPublisher {

    fun publish(webPage: WebPage)
    fun publish(rss: RssFeed)
}
