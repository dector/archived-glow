package io.github.dector.glow.v2.core


interface GlowEngine {

    fun execute(dataProvider: DataProvider,
                dataProcessor: DataProcessor,
                dataPublisher: DataPublisher): GlowExecutionResult
}

interface DataProvider {

    fun fetchMetaInfo(): MetaInfo
}

interface DataProcessor {

    fun render(page: Page): RenderedPage
}

interface DataPublisher {

    fun publishPage(page: RenderedPage)

    fun publish(data: ProcessedData): PublishResult
}

