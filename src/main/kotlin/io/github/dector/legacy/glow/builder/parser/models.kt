package io.github.dector.legacy.glow.builder.parser

import io.github.dector.legacy.glow.builder.models.PostMeta

data class ParsedPost(
        val meta: PostMeta,
        val content: String)