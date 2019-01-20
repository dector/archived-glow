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

        val metaInfo = dataProvider.fetchMetaInfo()

        log.info("Found pages: ${metaInfo.pages.size}")
        log.info("\n")

        log.info("Processing data...")
        metaInfo.pages.forEach { pageInfo ->
            log.info("Processing '${pageInfo.title}'")

            val page = dataProvider.fetchPage(pageInfo)

            val renderedPage = dataRenderer.render(page)

            log.info("Publishing '${pageInfo.title}'")
            dataPublisher.publishPage(renderedPage)
        }

        log.info("Copying static...")
        copyStatic(config.input.staticFolder, config.output.staticFolder)
        log.info("Done\n")

        return GlowExecutionResult()
    }

    private fun copyStatic(inputFolder: File, outputFolder: File) {
        inputFolder.copyRecursively(outputFolder, onError = { file, err ->
            log.error("File '${file.absolutePath}' exists. Skipping.")
            OnErrorAction.SKIP
        })
    }
}