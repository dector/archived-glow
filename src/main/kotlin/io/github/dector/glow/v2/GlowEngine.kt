package io.github.dector.glow.v2


fun main(args: Array<String>) {
    println("Prototyping v2")

    val provider: DataProvider = {
        BlogData(pages = listOf(
                Page(title = "Page #1", content = "This is **FIRST** page.",
                        tags = listOf("blog", "published", "first")),
                Page(title = "Page #2", content = "This is **SECOND** page.",
                        tags = listOf("blog", "published", "second")),
                Page(title = "Page #3", content = "This is **THIRD** page.",
                        tags = listOf("blog", "published", "third")),
                Page(title = "Page Draft", content = "This is **DRAFT** page. Should not be displayed",
                        tags = listOf("blog", "draft"), isDraft = true)
        ))
    }
    val converter: DataConverter = { data ->
        ConvertedBlogData(pages = data.pages)
    }
    val renderer: DataRenderer = { data ->
        val filteredPages = data.pages.filterNot { it.isDraft }

        // FIXME add paging
        fun renderPages(pages: List<Page>): List<RenderedPage> = pages.map { it ->
            RenderedPage(title = it.title, content = it.content, tags = it.tags)
        }

        fun renderArticle(page: Page): Article = Article(title = page.title, content = page.content, tags = page.tags)

        // FIXME add paging
        fun renderIndexPages(pages: List<Page>, articlesOnPage: Int = 2): List<RenderedIndexPages> {
            val chunks = pages.chunked(articlesOnPage)

            return chunks.mapIndexed { index, chunk ->
                RenderedIndexPages(articles = chunk.map(::renderArticle), pageNumber = index + 1, totalPages = chunks.size)
            }
        }

        // FIXME add paging
        fun renderTagPages(pages: List<Page>): List<RenderedTagPage> {
            val tags = pages.flatMap { it.tags }.distinct()

            return tags.map { tag ->
                tag to pages.filter { page -> page.tags.contains(tag) }
            }.map { (tag, page) ->
                RenderedTagPage(
                        tag = tag,
                        articles = page.map(::renderArticle)
                )
            }
        }

        PreparedBlogData(
                indexPages = renderIndexPages(filteredPages),
                pages = renderPages(filteredPages),
                tagPages = renderTagPages(filteredPages))
    }
    val publisher: DataPublisher = { data ->
        fun block(message: String, body: () -> Unit) {
            println("=== === === === === === === === === ===")
            println(message)
            println()
            body()
            println("=== === === === === === === === === ===")
            println()
            println()
        }

        fun page(message: String, body: () -> Unit) {
            println(message)
            body()
            println(message)
            println()
        }

        // Index pages
        block("INDEX PAGES total: ${data.indexPages.size}") {
            data.indexPages.forEach {
                page("--- --- PAGE ${it.pageNumber}/${it.totalPages} --- ---") {
                    println(it.articles.joinToString(separator = "\n\n") {
                        """ |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                    """.trimMargin()
                    })
                }
            }
        }

        // Pages
        block("PAGES total: ${data.pages.size}") {
            data.pages.forEach {
                page("--- --- --- --- --- ---") {
                    println("""
                        |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                    """.trimMargin()
                    )
                }
            }
        }

        // Tag pages
        block("TAG PAGES total: ${data.tagPages.size}") {
            data.tagPages.forEach {
                page("--- --- TAG: ${it.tag} --- ---") {
                    println(it.articles.joinToString(separator = "\n\n") {
                        """ |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                    """.trimMargin()
                    })
                }
            }
        }

        PublishResult.Success()
    }

    println("Running dumb flow\n")

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
        val articles: List<Article>,
        val prevPage: RenderedTagPage? = null,
        val nextPage: RenderedTagPage? = null)