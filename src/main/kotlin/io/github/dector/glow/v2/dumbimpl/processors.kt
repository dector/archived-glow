package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import io.github.dector.glow.v2.DataProcessor
import io.github.dector.glow.v2.ProcessedData
import io.github.dector.glow.v2.models.Post
import io.github.dector.glow.v2.models.ProcessedPage


val dumbDataRenderer: DataProcessor = { data ->
    processPosts(
            draftsFilter(data.posts))
}

private fun processPosts(posts: List<Post>): ProcessedData {
    val preparedPosts = convertMarkdown(posts)

    return ProcessedData(
            indexPages = renderIndexPages(preparedPosts),
            pages = renderPages(preparedPosts),
            tagPages = renderTagPages(preparedPosts))
}

private fun draftsFilter(posts: List<Post>): List<Post> = posts.filterNot { it.isDraft }

private fun convertMarkdown(posts: List<Post>): List<Post> {
    val mdParser = Parser.builder().build()
    val htmlRenderer = HtmlRenderer.builder().build()

    return posts.map {
        it.copy(content = htmlRenderer.render(mdParser.parse(it.content)))
    }
}

private fun renderPages(posts: List<Post>) = posts.map {
    ProcessedPage(path = "posts/${it.title}", content = renderPage(it.title, it.content))
}

private fun renderJoined(pageNumber: Int, totalPages: Int, posts: List<Post>): String = html(title = "$pageNumber / $totalPages") {
    posts.joinToString(separator = "\n<hr/>\n") {
        "<h2>${it.title}</h1><br/>${it.content}"
    }
}

private fun renderIndexPages(posts: List<Post>): List<ProcessedPage> {
    val chunks = posts.chunked(2)
    val totalPages = chunks.size

    return chunks.mapIndexed { index, it ->
        val pageNumber = index + 1
        val path = if (pageNumber == 1) "index" else "page$pageNumber"
        ProcessedPage(path, content = renderJoined(pageNumber, totalPages, it))
    }
}

private fun renderJoinedByTag(tag: String, pageNumber: Int, totalPages: Int, posts: List<Post>): String = html(title = "$[tag] :: $pageNumber / $totalPages") {
    posts.joinToString(separator = "\n<hr/>\n", prefix = "<h1>$tag</h1>") {
        "<h2>${it.title}</h1><br/>${it.content}"
    }
}

private fun renderTagPages(posts: List<Post>): List<ProcessedPage> {
    val tags = posts.flatMap { it.tags }.distinct()

    val postsByTag = tags.map { tag -> tag to posts.filter { it.tags.contains(tag) } }

    return postsByTag.flatMap { (tag, posts) ->
        val chunks = posts.chunked(2)
        val totalPages = chunks.size

        chunks.mapIndexed { index, it ->
            val pageNumber = index + 1
            val pathPart = if (pageNumber == 1) "index" else "page$pageNumber"
            ProcessedPage("tags/$tag/$pathPart", content = renderJoinedByTag(tag, pageNumber, totalPages, it))
        }

    }
}

private fun renderPage(title: String, content: String) = html(title) { content }