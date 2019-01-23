package io.github.dector.glow.v2.core

import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.v2.mockimpl.ProjectConfig
import java.io.File

class DefaultGlowEngine(
        private val dataProvider: DataProvider,
        private val dataRenderer: DataRenderer,
        private val dataPublisher: DataPublisher,
        private val config: ProjectConfig
) : GlowEngine {

    private val log = UiLogger//logger()

    override fun execute(): GlowExecutionResult {
        log.info("Loading data...")
        log.info("")

        handlePages()
        handleNotes()

        handleStatic()

        return GlowExecutionResult()
    }

    private fun handlePages() {
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

    private fun handleNotes() {
        val notes = dataProvider.fetchNotes()
                .filter { !it.isDraft }

        log.info("Found non-draft notes: ${notes.size}")

        notes.forEach { note ->
            log.info(" * ${note.sourceFile.nameWithoutExtension}")
            log.info("Processing...")

            val webPage = dataRenderer.render(note)

            log.info("Publishing...")
            dataPublisher.publish(webPage)
        }
        log.info("")

        run {
            log.info("Notes index")
            log.info("Processing...")
            val webPage = dataRenderer.renderNotesIndex(notes)
            log.info("Publishing...")
            dataPublisher.publish(webPage)
            log.info("")
        }

        run {
            log.info("Notes archive")
            log.info("Processing...")
            val webPage = dataRenderer.renderNotesArchive(notes)
            log.info("Publishing...")
            dataPublisher.publish(webPage)
        }

        log.info("")
    }

    private fun handleStatic() {
        log.info("Copying static...")
        copyStatic(config.input.staticFolder, config.output.staticFolder)
        log.info("Done")
        log.info("")
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            log.warn("File '${file.absolutePath}' exists. Skipping.")
            OnErrorAction.SKIP
        })
    }
}