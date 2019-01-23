package io.github.dector.glow.v2.core

import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.v2.mockimpl.ProjectConfig
import java.io.File

class DefaultGlowEngine(
        private val config: ProjectConfig
) : GlowEngine {

    private val log = UiLogger//logger()

    override fun execute(dataProvider: DataProvider, dataRenderer: DataRenderer, dataPublisher: DataPublisher): GlowExecutionResult {
        log.info("Loading data...")
        log.info("")

        handlePages(dataProvider, dataRenderer, dataPublisher)

        // Deprecated
        run {
            val metaInfo = dataProvider.fetchMetaInfo()

            executeForNotes(metaInfo, dataProvider, dataRenderer, dataPublisher)
        }

        log.info("Copying static...")
        copyStatic(config.input.staticFolder, config.output.staticFolder)
        log.info("Done")
        log.info("")

        return GlowExecutionResult()
    }

    private fun handlePages(dataProvider: DataProvider, dataRenderer: DataRenderer, dataPublisher: DataPublisher) {
        val pages = dataProvider.fetchPages()

        log.info("Found pages: ${pages.size}")

        pages.forEach { page ->
            log.info("Processing '${page.title}'")

            val webPage = dataRenderer.render(page)

            log.info("Publishing '${page.title}'")
            dataPublisher.publish(webPage)
        }
        log.info("")
    }

    private fun executeForNotes(metaInfo: MetaInfo, dataProvider: DataProvider, dataRenderer: DataRenderer, dataPublisher: DataPublisher) {
        val nonDraftNotes = metaInfo.notes.filter { !it.isDraft }

        log.info("Found non-draft notes: ${nonDraftNotes.size}")

        nonDraftNotes.forEach { noteInfo ->
            log.info("Processing '${noteInfo.title}'")

            val note = dataProvider.fetchNote(noteInfo)

            val renderedNote = dataRenderer.render(note)

            log.info("Publishing '${noteInfo.title}'")
            dataPublisher.publishNote(renderedNote)
        }

        run {
            val notes = nonDraftNotes.map {
                dataProvider.fetchNote(it)
//                NoteItem(
//                        note = dataProvider.fetchNote(it),
//                        path = pathResolver.resolveForNote(it)
//                )
            }
            log.info("Building index page")
            dataPublisher.publishNotesIndex(dataRenderer.renderNotesIndex(notes).content)
        }

        log.info("")
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            log.error("File '${file.absolutePath}' exists. Skipping.")
            OnErrorAction.SKIP
        })
    }
}