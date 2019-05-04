package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.WebPage


interface DataPublisher {

    fun publish(webPage: WebPage)
}

