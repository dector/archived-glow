package io.github.dector.glow.v2.implementation

import io.github.dector.glow.core.logger.UiLogger
import io.github.dector.glow.v2.core.GlowExecutionResult
import io.github.dector.glow.v2.core.components.DataProvider
import io.github.dector.glow.v2.core.components.DataPublisher
import io.github.dector.glow.v2.core.components.DataRenderer
import io.github.dector.glow.v2.core.components.GlowEngine
import java.io.File

class DefaultGlowEngine(
        private val dataProvider: DataProvider,
        private val dataRenderer: DataRenderer,
        private val dataPublisher: DataPublisher,
        private val config: ProjectConfig
) : GlowEngine {

    private val log = UiLogger//logger()

    override fun execute(): GlowExecutionResult {
        "Loading data...".logn()

        handlePages()
        handleNotes()

        handleStatic()

        return GlowExecutionResult.Success
    }

    private fun handlePages() {
        val pages = dataProvider.fetchPages()

        "Found pages: ${pages.size}".log()

        pages.forEach { page ->
            "Processing '${page.title}'".log()

            val webPage = dataRenderer.render(page)

            "Publishing '${page.title}'".log()
            dataPublisher.publish(webPage)
        }
        "".log()
    }

    private fun handleNotes() {
        val notes = dataProvider.fetchNotes()
                .filter { !it.isDraft }

        "Found non-draft notes: ${notes.size}".log()

        notes.forEach { note ->
            " * ${note.sourceFile.nameWithoutExtension}".log()
            "Processing...".log()

            val webPage = dataRenderer.render(note)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }
        "".log()

        run {
            "Notes index".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesIndex(notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)

            "".log()
        }

        run {
            "Notes archive".log()
            "Processing...".log()
            val webPage = dataRenderer.renderNotesArchive(notes)

            "Publishing...".log()
            dataPublisher.publish(webPage)
        }

        "".log()
    }

    private fun handleStatic() {
        "Building styles...".log()
        buildStyles(config.input.staticFolder, config.output.staticFolder)

        "Copying static...".log()
        copyStatic(config.input.staticFolder, config.output.staticFolder)

        "Done".logn()
    }

    private fun buildStyles(inputFolder: File, outputFolder: File) {
        val inputFile = File(inputFolder, "../source-dev/includes/less/style.less")
        val outputFile = File(outputFolder, "includes/css/style.css")

        fun createCompilerProcess(vararg arguments: String) = ProcessBuilder("/home/linuxbrew/.linuxbrew/bin/lessc", *arguments)

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

    private fun String.log() {
        log.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }
}