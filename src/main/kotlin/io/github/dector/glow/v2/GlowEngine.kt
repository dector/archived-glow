package io.github.dector.glow.v2


fun execute(dataProvider: DataProvider,
            dataRenderer: DataRenderer,
            dataPublisher: DataPublisher): Result {
    // Get Data (md)
    val data = dataProvider()

    // Render pages
    val resultData = dataRenderer(data)

    // Publish result
    val publishResult = dataPublisher(resultData)

    return Result(publishResult)
}

// Functions

typealias DataProvider = () -> BlogData

typealias DataRenderer = (BlogData) -> RenderedData

typealias DataPublisher = (RenderedData) -> PublishResult

// Data flow models

data class BlogData(val pages: List<Page>)

data class ConvertedBlogData(val pages: List<Page>)

data class RenderedData(val indexPages: List<RenderedIndexPages>,
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