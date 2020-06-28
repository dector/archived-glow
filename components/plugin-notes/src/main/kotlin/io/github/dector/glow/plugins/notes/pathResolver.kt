package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.plugins.notes.providers.cleanupTitleForWebPath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun coordinatesFor(note: Note): Coordinates.Endpoint
    fun coordinatesForNotesPage(pageNum: Int): Coordinates.Endpoint
    fun coordinatesForTagPage(tag: String, pageNum: Int): Coordinates.Endpoint
}

// TODO test
class NotesWebPathResolver(
    config: RuntimeConfig
) : NotesPathResolver {

    private val sectionPath = config.glow.notes
        .destinationPath
        .trim('/')

    private val notePathDateFormatter = DateTimeFormatter
        .ofPattern("uuuu/MM/dd")
        .withZone(ZoneOffset.UTC)

    override fun coordinatesFor(note: Note): Coordinates.Endpoint {
        val dir = if (note.publishedAt != null)
            notePathDateFormatter.format(note.publishedAt)
        else DraftsDirName
        val instance = note.title.cleanupTitleForWebPath()

        return Coordinates.Endpoint(
            section = sectionPath,
            inner = dir,
            name = instance
        )
    }

    override fun coordinatesForNotesPage(pageNum: Int): Coordinates.Endpoint {
        return Coordinates.Endpoint(
            section = sectionPath,
            name = when (pageNum) {
                in 2..Int.MAX_VALUE -> "page$pageNum"
                else -> ""
            }
        )
    }

    override fun coordinatesForTagPage(tag: String, pageNum: Int): Coordinates.Endpoint {
        return Coordinates.Endpoint(
            section = sectionPath,
            inner = "tag/$tag",
            name = when (pageNum) {
                in 2..Int.MAX_VALUE -> "page$pageNum"
                else -> ""
            }
        )
    }

    private companion object {
        const val DraftsDirName = "drafts"
    }
}
