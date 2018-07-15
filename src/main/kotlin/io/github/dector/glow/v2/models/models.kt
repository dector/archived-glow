package io.github.dector.glow.v2.models

data class Post(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val isDraft: Boolean = false)

data class ProcessedPage(
        val path: String,
        val content: String)