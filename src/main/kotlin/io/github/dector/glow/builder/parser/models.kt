package io.github.dector.glow.builder.parser

import io.github.dector.glow.builder.models.PostMeta

data class ParsedPost(
        val meta: PostMeta,
        val content: String)