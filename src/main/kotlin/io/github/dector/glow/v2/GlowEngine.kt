package io.github.dector.glow.v2

import io.github.dector.glow.v2.models.Post
import io.github.dector.glow.v2.models.ProcessedPage


fun execute(dataProvider: DataProvider,
            dataProcessor: DataProcessor,
            dataPublisher: DataPublisher): Result {
    // Get Data (md)
    val data = dataProvider()

    // Process posts
    val processedData = dataProcessor(data)

    // Publish result
    val publishResult = dataPublisher(processedData)

    return Result(publishResult)
}

// Functions

typealias DataProvider = () -> BlogData

typealias DataProcessor = (BlogData) -> ProcessedData

typealias DataPublisher = (ProcessedData) -> PublishResult

// Data flow models

data class BlogData(val posts: List<Post>)

data class ProcessedData(val indexPages: List<ProcessedPage>,
                         val pages: List<ProcessedPage>,
                         val tagPages: List<ProcessedPage>)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)
