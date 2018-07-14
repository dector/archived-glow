package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.ConvertedBlogData
import io.github.dector.glow.v2.DataConverter


val dumbMdToHtmlConverter: DataConverter = { data ->
    ConvertedBlogData(pages = data.pages)
}