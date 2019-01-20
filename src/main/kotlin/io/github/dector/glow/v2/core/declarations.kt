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
        println()

        println("Processing data...")
        metaInfo.pages.forEach { page ->
            println("Processing ${page.title}")
        }
        println()

        return GlowExecutionResult()
    }
}