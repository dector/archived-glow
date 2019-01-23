package io.github.dector.glow.v2.core


interface GlowEngine {

    fun execute(): GlowExecutionResult
}

interface DataProvider {

    fun fetchPages(): List<Page2>
    fun fetchNotes(): List<Note2>
}

interface DataRenderer {

    fun render(page: Page2): WebPage
    fun render(note: Note2): WebPage
}

interface DataPublisher {

    fun publish(webPage: WebPage)
}

