package io.github.dector.glow

import java.io.File
import java.time.LocalDate

data class PageModel(
        val global: GlobalData,
        val title: String,
        val tags: List<String> = emptyList(),
        val pubdate: LocalDate?,
        val content: String)

data class ParsedPost(
        val meta: PostMeta,
        val content: String)

data class PostMeta(
        val title: String,
        val tags: List<String>,
        val pubdate: LocalDate?,
        val url: String,
        val draft: Boolean,
        val file: File)

data class GlobalData(
        val blogName: String,
        val posts: List<PostMeta>)