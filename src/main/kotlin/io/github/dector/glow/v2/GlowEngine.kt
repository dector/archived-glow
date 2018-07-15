package io.github.dector.glow.v2


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

data class ProcessedData(val indexPages: List<ProcessedPost>,
                         val pages: List<ProcessedPost>,
                         val tagPages: List<ProcessedPost>)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)

// Models

data class Post(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val isDraft: Boolean = false)

data class Article(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList())

data class ProcessedPost(
        val path: String,
        val content: String)