package space.dector.glow.plugins.notes

fun parseMarkdownPartsFrom(text: String): ParsedResult {
    val lines = text
        .trimStart()
        .lines()

    val hasHeader = lines.first().isYmlHeaderDivider()
    if (!hasHeader) return ParsedResult(content = text.trim())

    // Cut header
    val contentLines = lines.drop(1)
        .dropWhile { !it.isYmlHeaderDivider() }
        .drop(1)
        // Trim empty lines
        .dropWhile { it.isBlank() }
        .dropLastWhile { it.isBlank() }

    return ParsedResult(
        header = lines.drop(1)
            .takeWhile { !it.isYmlHeaderDivider() }
            .joinToString("\n"),
        content = contentLines.joinToString("\n")
    )
}

data class ParsedResult(
    val header: String? = null,
    val content: String
)

private fun String.isYmlHeaderDivider() = trim().let { str ->
    str.isNotBlank() && str.all { it == '-' }
}
