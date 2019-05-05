package io.github.dector.glow.v2.resources

import io.github.dector.glow.v2.implementation.ProjectConfig
import io.github.dector.glow.v2.pipeline.GlowPipeline
import org.slf4j.Logger
import java.io.File


class StaticResourcesPipeline(
        private val config: ProjectConfig,
        private val logger: Logger
) : GlowPipeline {

    override fun execute() {
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
            logger.error("Less compiler not found in system. Consider installing it: `npm install -g less`.")
            logger.warn("Website style will be broken or out-of-dated.")
            return
        }

        val exitCode = createCompilerProcess(inputFile.absolutePath, outputFile.absolutePath)
                .start()
                .run {
                    waitFor()
                    exitValue()
                }

        if (exitCode != 0) {
            logger.error("Less compiler failed to execute request.")
        }
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            logger.warn(err.message)

            if (err is FileAlreadyExistsException)
                logger.warn("File '${file.absolutePath}' exists. Overwriting.")

            OnErrorAction.SKIP
        }, overwrite = true)
    }

    private fun String.log() {
        logger.info(this)
    }

    private fun String.logn() {
        this.log()
        "".log()
    }
}