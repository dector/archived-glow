package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig


internal typealias NotesLoadingResult = Pair<List<Note>, LoadingStats>

internal fun loadNotes(
    provider: NotesDataProvider,
    config: RuntimeConfig
): NotesLoadingResult {
    val allNotes = provider.fetchNotes()

    val filteredNotes = allNotes
        .dropDraftsIfNeeded(config.glow.includeDrafts)
        .dropEmptyNotes()
        .ensureTitlesArePresent()

    val stats = run {
        val total = allNotes.size
        val used = filteredNotes.size
        val dropped = total - used

        LoadingStats(total, used, dropped)
    }
    return filteredNotes to stats
}

internal data class LoadingStats(
    val total: Int,
    val used: Int,
    val dropped: Int
)

private fun List<Note>.dropDraftsIfNeeded(include: Boolean) = when {
    !include -> filterNot { it.isDraft }
    else -> this
}

private fun List<Note>.dropEmptyNotes() =
    filter { it.content.value.isNotBlank() }

private fun List<Note>.ensureTitlesArePresent(): List<Note> = map {
    if (it.title.isNotBlank()) it
    else it.copy(title = "Untitled note ${it.hashCode()}")
}
