package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.tools.nextOrNull
import io.github.dector.glow.tools.prevOrNull
import io.github.dector.glow.v2.*


val dumbDataRenderer: DataRenderer = { data ->
    val filteredPages = data.pages.filterNot { it.isDraft }

    fun renderPages(pages: List<Page>): List<RenderedPage> {
        val result = pages.map { it ->
            RenderedPage(title = it.title, content = it.content, tags = it.tags)
        }

        return result.map {
            it.copy(prevPage = result.prevOrNull(it), nextPage = result.nextOrNull(it))
        }
    }

    fun renderArticle(page: Page): Article = Article(title = page.title, content = page.content, tags = page.tags)

    fun renderIndexPages(pages: List<Page>, articlesOnPage: Int = 2): List<RenderedIndexPages> {
        val chunks = pages.chunked(articlesOnPage)

        val result = chunks.mapIndexed { index, chunk ->
            RenderedIndexPages(articles = chunk.map(::renderArticle), pageNumber = index + 1, totalPages = chunks.size)
        }

        return result.map {
            it.copy(prevPage = result.prevOrNull(it),
                    nextPage = result.nextOrNull(it))
        }
    }

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

    RenderedData(
            indexPages = renderIndexPages(filteredPages),
            pages = renderPages(filteredPages),
            tagPages = renderTagPages(filteredPages))
}