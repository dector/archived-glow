package io.github.dector.glow.v2

import io.github.dector.glow.v2.PublishResult.Fail
import io.github.dector.glow.v2.PublishResult.Success


fun main(args: Array<String>) {
    println("Prototyping v2")

    val provider: DataProvider = { BlogData() }
    val converter: DataConverter = { _ -> ConvertedBlogData() }
    val renderer: DataRenderer = { _ -> PreparedBlogData() }
    val publisher: DataPublisher = { _ -> PublishResult.Success() }

    println("Running dumb flow")

    val result = execute(provider, converter, renderer, publisher)
    println("Glow finished with publishing result: ${result.publishResult}")
}

fun execute(dataProvider: DataProvider,
            dataConverter: DataConverter,
            dataRenderer: DataRenderer,
            dataPublisher: DataPublisher): Result {
    // Get Data (md)
    val data = dataProvider()

    // Convert data (md -> html)
    // Render pages
    val resultData = dataRenderer(dataConverter(data))

    // Publish result
    val publishResult = dataPublisher(resultData)

    return Result(publishResult)
}

// Functions

typealias DataProvider = () -> BlogData

typealias DataConverter = (BlogData) -> ConvertedBlogData

typealias DataRenderer = (ConvertedBlogData) -> PreparedBlogData

typealias DataPublisher = (PreparedBlogData) -> PublishResult

// Data

data class BlogData(val nothing: Unit = Unit)

data class ConvertedBlogData(val nothing: Unit = Unit)

data class PreparedBlogData(val nothing: Unit = Unit)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)