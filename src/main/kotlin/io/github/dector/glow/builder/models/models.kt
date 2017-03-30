package io.github.dector.glow.builder.models

import java.io.File
import java.time.LocalDate

data class PostMeta(
        val title: String,
        val tags: List<String>,
        val pubDate: LocalDate?,
        val isDraft: Boolean,
        val url: String,
        val file: File)

data class BlogData(
        val title: String,
        val posts: List<PostMeta>)

data class PageData(
        val blog: BlogData,
        val title: String = "",
        val tags: List<String> = emptyList(),
        val pubDate: LocalDate? = null,
        val content: String = "")