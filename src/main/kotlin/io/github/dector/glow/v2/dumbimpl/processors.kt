package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import io.github.dector.glow.tools.nextOrNull
import io.github.dector.glow.tools.prevOrNull
import io.github.dector.glow.v2.core.DataProcessor
import io.github.dector.glow.v2.core.Post
import io.github.dector.glow.v2.core.ProcessedData
import io.github.dector.glow.v2.core.ProcessedPage


typealias DataFilter = (List<Post>) -> List<Post>

val dumbDataRenderer: DataProcessor = { data ->
    val filter = ::nonDraftsFilter

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

private fun processIndexPages(posts: List<Post>): List<ProcessedPage> {
    val chunks = posts.chunked(2)
    val totalPages = chunks.size

    return chunks.mapIndexed { index, it ->
        val pageNumber = index + 1
        fun path(page: Int) = "/" + (if (page == 1) "index" else "page$page") + ".html"

        val prevPagePath: String = chunks.prevOrNull(it)?.let { path(chunks.indexOf(it) + 1) } ?: ""
        val nextPagePath: String = chunks.nextOrNull(it)?.let { path(chunks.indexOf(it) + 1) } ?: ""

        ProcessedPage(path(pageNumber), content = renderJoined(pageNumber, totalPages, nextPagePath, prevPagePath, it))
    }
}

private fun processPostPages(posts: List<Post>) = posts.map {
    ProcessedPage(path = buildPostPath(it), content = renderPage(it))
}

private fun processTagPages(posts: List<Post>): List<ProcessedPage> {
    val tags = posts.flatMap { it.tags }.distinct()

    // Tag -> [Post]
    val postsByTag = tags.map { tag -> tag to posts.filter { it.tags.contains(tag) } }

    return postsByTag.flatMap { (tag, posts) ->
        val chunks = posts.chunked(2)
        val totalPages = chunks.size

        // Paged posts
        chunks.mapIndexed { index, it ->
            val pageNumber = index + 1

            fun path(page: Int) = "/tags/$tag/" + (if (page == 1) "index" else "page$page") + ".html"

            val prevPagePath: String = chunks.prevOrNull(it)?.let { path(chunks.indexOf(it) + 1) } ?: ""
            val nextPagePath: String = chunks.nextOrNull(it)?.let { path(chunks.indexOf(it) + 1) } ?: ""

            ProcessedPage(path(pageNumber), content = renderJoinedByTag(tag, pageNumber, totalPages, prevPagePath, nextPagePath, it))
        }

    }
}

// --- --- --- --- ---

private fun buildPostPath(post: Post) = "/posts/${post.title.toLowerCase().cleanup()}.html"

private fun String.cleanup(): String = replace(" ", "_")
        .replace("#", "")

private fun renderPostPart(post: Post): String {
    return "<h2><a href='${buildPostPath(post)}'>${post.title}</a></h2><br/>${post.content}" +
            """
                | [${post.tags.joinToString { "<a href='/tags/$it/'>$it</a>" }}]
                """.trimMargin()
}

private fun renderJoined(pageNumber: Int, totalPages: Int, nextPagePath: String, prevPagePath: String, posts: List<Post>): String = html(title = "$pageNumber / $totalPages") {
    posts.joinToString(separator = "\n<hr/>\n") {
        renderPostPart(it)
    } + (
            (if (prevPagePath.isNotEmpty()) "<br><a href='$prevPagePath'><< Previous</a>" else "") +
                    (if (nextPagePath.isNotEmpty()) "<br><a href='$nextPagePath'>Next >></a>" else "")
            )
}

private fun renderJoinedByTag(tag: String, pageNumber: Int, totalPages: Int, prevPagePath: String, nextPagePath: String, posts: List<Post>): String = html(title = "$[tag] :: $pageNumber / $totalPages") {
    posts.joinToString(separator = "\n<hr/>\n", prefix = "<h1>$tag</h1>") {
        "<h2>${it.title}</h1><br/>${it.content}"
    } + (
            (if (prevPagePath.isNotEmpty()) "<br><a href='$prevPagePath'><< Previous</a>" else "") +
                    (if (nextPagePath.isNotEmpty()) "<br><a href='$nextPagePath'>Next >></a>" else "")
            )
}

private fun renderPage(post: Post) = html(post.title) { renderPostPart(post) }