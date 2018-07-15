package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.Post


fun indexPagePathResolver(pageNumber: Int) = "/" + (if (pageNumber == 1) "index" else "page$pageNumber") + ".html"

fun postPagePathResolver(post: Post) = "/posts/${post.title.toLowerCase().cleanup()}.html"

fun tagPagePathResolver(tag: String, pageNumber: Int) = "/tags/$tag/" + (if (pageNumber == 1) "index" else "page$pageNumber") + ".html"

private fun String.cleanup(): String = replace(" ", "_")
        .replace("#", "")
