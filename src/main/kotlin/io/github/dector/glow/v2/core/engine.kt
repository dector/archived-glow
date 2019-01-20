package io.github.dector.glow.v2.core


fun execute(dataProvider: DataProvider,
            dataProcessor: DataProcessor,
            dataPublisher: DataPublisher): GlowExecutionResult {
    val data = dataProvider.fetchBlogData()
    val processedData = dataProcessor.processBlogData(data)
    val publishResult = dataPublisher.publish(processedData)

    return GlowExecutionResult()
}