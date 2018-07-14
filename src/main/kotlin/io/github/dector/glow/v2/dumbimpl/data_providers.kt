package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.BlogData
import io.github.dector.glow.v2.DataProvider
import io.github.dector.glow.v2.Page


val dumbDataProvider: DataProvider = {
    BlogData(pages = listOf(
            """
            |---
            |title: Page #1
            |tags: blog, published, first
            |---
            |
            |This is **FIRST** page.
            """.trimMargin(),
            """
            |---
            |title: Page #2
            |tags: blog, published, second
            |---
            |
            |This is **SECOND** page.
            """.trimMargin(),
            """
            |---
            |title: Page #3
            |tags: blog, published, third
            |---
            |
            |This is **THIRD** page.
            """.trimMargin(),
            """
            |---
            |title: Page #4
            |tags: blog, draft
            |draft = true
            |---
            |
            |This is **DRAFT** page. Should not be displayed.
            """.trimMargin()
    ))
}