package io.github.dector.glow.v2.core


interface GlowEngine {

    fun execute(dataProvider: DataProvider,
                dataProcessor: DataProcessor,
                dataPublisher: DataPublisher): GlowExecutionResult {
        val data = dataProvider.fetchBlogData()
        val processedData = dataProcessor.processBlogData(data)
        val publishResult = dataPublisher.publish(processedData)

        return GlowExecutionResult()
    }
}

interface DataProvider {

    fun fetchMetaInfo(): MetaInfo

    @Deprecated("Use fetchMetaInfo()")
    fun fetchBlogData(): BlogData
}

interface DataProcessor {

    fun processBlogData(blog: BlogData): ProcessedData
}

interface DataPublisher {

    fun publish(data: ProcessedData): PublishResult
}

class DefaultGlowEngine : GlowEngine {

    override fun execute(dataProvider: DataProvider, dataProcessor: DataProcessor, dataPublisher: DataPublisher): GlowExecutionResult {
        println("Loading data...")

        val metaInfo = dataProvider.fetchMetaInfo()

        println("Loaded.")
        println("Found pages: ${metaInfo.pages.size}")

        return GlowExecutionResult()
    }
}