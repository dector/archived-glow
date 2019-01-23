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
    fun render(note: Note, asPage: Boolean = true): RenderedNote
    fun renderNotesIndex(notes: List<Note>): RenderedPage
}

interface DataPublisher {

    fun publishPage(page: RenderedPage)
    fun publishNote(note: RenderedNote)

    fun publish(data: ProcessedData): PublishResult
    fun publishNotesIndex(htmlContent: String)
}

