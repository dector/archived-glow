package io.github.dector.glow.v2.core

import io.github.dector.glow.core.logger.UiLogger
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
        log.info("Building styles...")
        buildStyles(config.input.staticFolder, config.output.staticFolder)
        log.info("Copying static...")
        copyStatic(config.input.staticFolder, config.output.staticFolder)
        log.info("Done")
        log.info("")
    }

    private fun buildStyles(inputFolder: File, outputFolder: File) {
        val inputFile = File(inputFolder, "../source-dev/includes/less/style.less")
        val outputFile = File(outputFolder, "includes/css/style.css")

        fun createCompilerProcess(vararg arguments: String) = ProcessBuilder("lessc", *arguments)

        fun checkIfLessCompilerExists() = createCompilerProcess("--version")
                .start()
                .run {
                    waitFor()
                    exitValue()
                } == 0

        if (!checkIfLessCompilerExists()) {
            log.error("Less compiler not found in system. Consider installing it: `npm install -g less`.")
            log.warn("Website style will be broken or out-of-dated.")
            return
        }

        val exitCode = createCompilerProcess(inputFile.absolutePath, outputFile.absolutePath)
                .start()
                .run {
                    waitFor()
                    exitValue()
                }

        if (exitCode != 0) {
            log.error("Less compiler failed to execute request.")
        }
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            log.warn(err.message)

            if (err is FileAlreadyExistsException)
                log.warn("File '${file.absolutePath}' exists. Overwriting.")

            OnErrorAction.SKIP
        }, overwrite = true)
    }
}