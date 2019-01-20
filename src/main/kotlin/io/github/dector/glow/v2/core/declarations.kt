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

    @Deprecated("Use render()")
    fun processBlogData(blog: BlogData): ProcessedData
}

interface DataPublisher {

    fun publishPage(page: RenderedPage)

    fun publish(data: ProcessedData): PublishResult
}

class DefaultGlowEngine : GlowEngine {

    override fun execute(dataProvider: DataProvider, dataProcessor: DataProcessor, dataPublisher: DataPublisher): GlowExecutionResult {
        println("Loading data...")

        val metaInfo = dataProvider.fetchMetaInfo()

        println("Found pages: ${metaInfo.pages.size}")
        println()

        println("Processing data...")
        metaInfo.pages.forEach { pageInfo ->
            println("Processing '${pageInfo.title}'")

            val page = page(pageInfo)

            val renderedPage = dataProcessor.render(page)

            println("Publishing '${pageInfo.title}'")
            dataPublisher.publishPage(renderedPage)
        }
        println()

        return GlowExecutionResult()
    }

    private fun page(info: PageInfo) = Page(info = info)
}