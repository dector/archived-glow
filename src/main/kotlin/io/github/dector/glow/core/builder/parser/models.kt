package io.github.dector.glow.core.builder.parser

import io.github.dector.glow.core.builder.models.PostMeta

data class ParsedPost(
        val meta: PostMeta,
        val content: String)