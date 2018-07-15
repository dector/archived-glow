package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import io.github.dector.glow.v2.DataProcessor
import io.github.dector.glow.v2.Post
import io.github.dector.glow.v2.ProcessedData
import io.github.dector.glow.v2.ProcessedPost


val dumbDataRenderer: DataProcessor = { data ->
    val filteredPages = draftsFilter(data.posts)

    processPages(filteredPages)
}

private fun draftsFilter(posts: List<Post>): List<Post> = posts.filterNot { it.isDraft }

private fun processPages(posts: List<Post>): ProcessedData {
    // Convert md to html
    val mdParser = Parser.builder().build()
    val htmlRenderer = HtmlRenderer.builder().build()

    val convertedPosts = posts.map { it.copy(content = htmlRenderer.render(mdParser.parse(it.content))) }

    // Render html pages

    val renderedPages = convertedPosts.map {
        ProcessedPost(path = it.title, content = renderPage(it.title, it.content))
    }

    return ProcessedData(
            indexPages = emptyList(),
            pages = renderedPages,
            tagPages = emptyList())
}

private fun renderPage(title: String, content: String) = html(title) { content }

/*private fun renderPages(posts: List<Post>): List<ProcessedPost> {
    return posts.map {
        ProcessedPost(path = it.title.toLowerCase().replace(" ", "_"),
                content = html(it.title) {
                    """
                    |<h1>${it.title}</h1>
                    |
                    |${it.content}
                    |
                    |${it.tags.joinToString(", ", prefix = "[", postfix = "]") {
                        "<a href='${resolvePath(PageType.TAG)}'>$it</a>"

                    }}
                    """.trimMargin()
                })
    }
}*/

// --- ---

private fun html(title: String, body: () -> String): String {
    return """
        |<html>
        |<head><title>$title</title></head>
        |<body>
        |${body()}
        |</body>
        |</html>
        """.trimMargin()
}
