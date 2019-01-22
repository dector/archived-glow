package io.github.dector.glow.v2.core


interface GlowEngine {

    fun execute(dataProvider: DataProvider,
                dataRenderer: DataRenderer,
                dataPublisher: DataPublisher): GlowExecutionResult
}

interface DataProvider {

    fun fetchMetaInfo(): MetaInfo
    fun fetchPage(pageInfo: PageInfo): Page
    fun fetchNote(noteInfo: NoteInfo): Note
}

interface DataRenderer {

    fun render(page: Page): RenderedPage
    fun render(note: Note): RenderedNote
}

interface DataPublisher {

    fun publishPage(page: RenderedPage)
    fun publishNote(note: RenderedNote)

    fun publish(data: ProcessedData): PublishResult
}

