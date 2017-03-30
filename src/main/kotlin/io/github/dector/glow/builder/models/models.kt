package io.github.dector.glow.builder.models

import java.io.File
import java.time.LocalDate

data class PageModel(
        val blog: BlogData,
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
        val isDraft: Boolean,
        val file: File)

data class BlogData(
        val title: String,
        val posts: List<PostMeta>)