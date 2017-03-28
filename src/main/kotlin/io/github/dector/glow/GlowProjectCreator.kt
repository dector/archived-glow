package io.github.dector.glow

import org.slf4j.LoggerFactory
import java.io.File

class GlowProjectCreator(private val opts: GlowCommandInitOptions) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun process() {
        val targetFolderPath = opts.targetFolder[0]
        val targetFolder = File(targetFolderPath)

        logger.info("Initializing project in $targetFolderPath")
        targetFolder.mkdirs()
    }
}