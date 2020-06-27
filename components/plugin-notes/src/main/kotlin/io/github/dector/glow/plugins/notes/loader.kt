package io.github.dector.glow.plugins.notes

import io.github.dector.ktx.applyIf


internal class NotesIndex {
    lateinit var allNotes: List<Note>
        private set

    lateinit var notesToPublish: List<Note>
        private set

    fun populateFrom(provider: NotesDataProvider,
                     includeDrafts: Boolean = false,
                     includeEmpty: Boolean = false
    ): LoadingStats {
        allNotes = provider.fetchNotes()

        notesToPublish = allNotes
            .applyIf(!includeDrafts) { filterNot { it.isDraft } }
            .applyIf(!includeEmpty) { filter { it.content.value.isNotBlank() } }
            .ensureTitlesArePresent()

        return run {
            val total = allNotes.size
            val used = notesToPublish.size
            val dropped = total - used

            LoadingStats(total, used, dropped)
        }
    }
}

internal data class LoadingStats(
    val total: Int,
    val used: Int,
    val dropped: Int
)

private fun List<Note>.ensureTitlesArePresent(): List<Note> = map {
    if (it.title.isNotBlank()) it
    else it.copy(title = "Untitled note ${it.hashCode()}")
}
