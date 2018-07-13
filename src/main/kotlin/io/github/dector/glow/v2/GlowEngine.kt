package io.github.dector.glow.v2


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

// Data flow models

data class BlogData(val pages: List<Page>)

data class ConvertedBlogData(val pages: List<Page>)

data class PreparedBlogData(val indexPages: List<RenderedIndexPages>,
                            val pages: List<RenderedPage>,
                            val tagPages: List<RenderedTagPage>)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)

// Models

data class Page(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val isDraft: Boolean = false)

data class Article(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList())

data class RenderedPage(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val prevPage: RenderedPage? = null,
        val nextPage: RenderedPage? = null)

data class RenderedIndexPages(
        val articles: List<Article>,
        val pageNumber: Int,
        val totalPages: Int,
        val prevPage: RenderedIndexPages? = null,
        val nextPage: RenderedIndexPages? = null)

data class RenderedTagPage(
        val tag: String,
        val articles: List<Article>)