package io.github.dector.glow.v2.implementation

import java.io.File
import java.time.Instant

class MarkdownParserWrapper(
        private val parser: MarkdownParser<*>) {

    fun parseMetaInfo(markdownFile: File): Map<String, String> {
        return parseYFM(markdownFile)
    }

    fun parseYFM(markdownFile: File) =
            parser.parseInsecureYFM(markdownFile)

    fun parseYFM(content: String) =
            parser.parseInsecureYFM(content)
}

internal fun String.parseInstant() = try {
    Instant.parse(this)
} catch (e: Throwable) {
    null
}

internal fun parseInstant(str: String?, fallback: () -> Instant): Instant {
    str ?: fallback()

    return try {
        Instant.parse(str)
    } catch (e: Throwable) {
        fallback()
    }
}

internal fun markdownFileId(file: File) = file.nameWithoutExtension
        .toLowerCase()
        .replace(" ", "-")