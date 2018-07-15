package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import io.github.dector.glow.tools.nextOrNull
import io.github.dector.glow.tools.prevOrNull
import io.github.dector.glow.v2.PaginationIndexPostsCount
import io.github.dector.glow.v2.PaginationTagPostsCount
import io.github.dector.glow.v2.core.DataProcessor
import io.github.dector.glow.v2.core.Post
import io.github.dector.glow.v2.core.ProcessedData
import io.github.dector.glow.v2.core.ProcessedPage


typealias DataFilter = (List<Post>) -> List<Post>

val dumbDataRenderer: DataProcessor = { data ->
    val filter: DataFilter = ::nonDraftsFilter

    processPosts(filter(data.posts))
}

private fun nonDraftsFilter(posts: List<Post>): List<Post> = posts.filterNot { it.isDraft }

private fun processPosts(posts: List<Post>): ProcessedData {
    val preparedPosts = convertMarkdown(posts)

    return ProcessedData(
            indexPages = processIndexPages(preparedPosts),
            postPages = processPostPages(preparedPosts),
            tagPages = processTagPages(preparedPosts))
}

private fun convertMarkdown(posts: List<Post>): List<Post> {
    val mdParser = Parser.builder().build()
    val htmlRenderer = HtmlRenderer.builder().build()

    return posts.map {
        it.copy(content = htmlRenderer.render(mdParser.parse(it.content)))
    }
}

data class PaginatedPage(
        val pageNumber: Int,
        val totalPages: Int,
        val prevPagePath: String,
        val nextPagePath: String,
        val posts: List<Post>)

private fun processIndexPages(posts: List<Post>): List<ProcessedPage> {
    val chunks = posts.chunked(PaginationIndexPostsCount)

    val renderer: IndexPagesRenderer = indexPagesRenderer

    return chunks.mapIndexed { index, it ->
        val info = PaginatedPage(
                pageNumber = index + 1,
                totalPages = chunks.size,
                prevPagePath = chunks.prevOrNull(it)?.let { indexPagePathResolver(chunks.indexOf(it) + 1) } ?: "",
                nextPagePath = chunks.nextOrNull(it)?.let { indexPagePathResolver(chunks.indexOf(it) + 1) } ?: "",
                posts = it
        )

        ProcessedPage(indexPagePathResolver(info.pageNumber), content = renderer(info))
    }
}

private fun processPostPages(posts: List<Post>) = posts.map {
    ProcessedPage(
            path = postPagePathResolver(it),
            content = postPageRenderer(it))
}

private fun processTagPages(posts: List<Post>): List<ProcessedPage> {
    val tags = posts.flatMap { it.tags }.distinct()

    // Tag -> [Post]
    val postsByTag = tags.map { tag -> tag to posts.filter { it.tags.contains(tag) } }

    return postsByTag.flatMap { (tag, posts) ->
        val chunks = posts.chunked(PaginationTagPostsCount)

        // Paged posts
        chunks.mapIndexed { index, it ->
            val info = PaginatedPage(
                    pageNumber = index + 1,
                    totalPages = chunks.size,
                    prevPagePath = chunks.prevOrNull(it)?.let { tagPagePathResolver(tag, chunks.indexOf(it) + 1) } ?: "",
                    nextPagePath = chunks.nextOrNull(it)?.let { tagPagePathResolver(tag, chunks.indexOf(it) + 1) } ?: "",
                    posts = it
            )

            ProcessedPage(
                    path = tagPagePathResolver(tag, info.pageNumber),
                    content = tagPageRenderer(tag, info))
        }

    }
}