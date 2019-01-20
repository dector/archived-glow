package io.github.dector.glow.v2.core


interface GlowEngine {

    fun execute(dataProvider: DataProvider,
                dataRenderer: DataRenderer,
                dataPublisher: DataPublisher): GlowExecutionResult
}

interface DataProvider {

    fun fetchMetaInfo(): MetaInfo
    fun fetchPage(pageInfo: PageInfo): Page
}

interface DataRenderer {

    fun render(page: Page): RenderedPage
}

interface DataPublisher {

    fun publishPage(page: RenderedPage)

    fun publish(data: ProcessedData): PublishResult
}

