package io.github.dector.glow.v2.core

import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.v2.mockimpl.ProjectConfig
import java.io.File

class DefaultGlowEngine(
        private val config: ProjectConfig
) : GlowEngine {

    private val log = UiLogger//logger()

    override fun execute(dataProvider: DataProvider, dataRenderer: DataRenderer, dataPublisher: DataPublisher): GlowExecutionResult {
        log.info("Loading data...\n")

        val metaInfo = dataProvider.fetchMetaInfo()

        executeForPages(metaInfo, dataProvider, dataRenderer, dataPublisher)
        executeForNotes(metaInfo, dataProvider, dataRenderer, dataPublisher)

        log.info("Copying static...")
        copyStatic(config.input.staticFolder, config.output.staticFolder)
        log.info("Done\n")

        return GlowExecutionResult()
    }

    private fun executeForPages(metaInfo: MetaInfo, dataProvider: DataProvider, dataRenderer: DataRenderer, dataPublisher: DataPublisher) {
        log.info("Found pages: ${metaInfo.pages.size}")

        metaInfo.pages.forEach { pageInfo ->
            log.info("Processing '${pageInfo.title}'")

            val page = dataProvider.fetchPage(pageInfo)

            val renderedPage = dataRenderer.render(page)

            log.info("Publishing '${pageInfo.title}'")
            dataPublisher.publishPage(renderedPage)
        }
        log.info("\n")
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
        log.info("\n")
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            log.error("File '${file.absolutePath}' exists. Skipping.")
            OnErrorAction.SKIP
        })
    }
}