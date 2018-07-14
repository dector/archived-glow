package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.BlogData
import io.github.dector.glow.v2.DataProvider
import io.github.dector.glow.v2.Page


val dumbDataProvider: DataProvider = {
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