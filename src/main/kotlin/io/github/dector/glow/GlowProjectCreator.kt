package io.github.dector.glow

import java.io.File

class GlowProjectCreator(private val opts: GlowCommandInitOptions) {

    private val logger = logger()

    fun process() {
        val targetFolderPath = opts.targetFolder[0]
        val targetFolder = File(targetFolderPath)

        UiLogger.info("Initializing project in $targetFolderPath")
        targetFolder.mkdirs()
    }
}