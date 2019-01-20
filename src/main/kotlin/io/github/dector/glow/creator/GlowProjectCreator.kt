package io.github.dector.glow.creator

import io.github.dector.glow.cli.legacy.GlowCommandInitOptions
import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.logger.logger
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class GlowProjectCreator(private val opts: GlowCommandInitOptions) {

    private val InitialPostPath = "posts/YYYY-MM-DD-Initial.md"

    private val logger = logger()

    fun process(): Boolean {
        val targetFolderPath = opts.targetFolder[0]
        val targetFolder = File(targetFolderPath)

        UiLogger.info("[Building] Initializing project in `$targetFolderPath`...")
        targetFolder.mkdirs()

        // TODO read as stream (for jar packaging)
        val templateUri = javaClass.classLoader?.getResource("template")?.toURI()
        if (templateUri == null) {
            logger.error("New project template not found in classpath.")
            return false
        }

        UiLogger.info("[Building] Deploying template...")
        File(templateUri).copyRecursively(
                target = targetFolder,
                onError = { file, e -> logger.error("Failed to deploy template file ${file.name}", e); OnErrorAction.TERMINATE})

        UiLogger.info("[Building] Modifying template...")
        val initPostFile = File(targetFolder, InitialPostPath)
        if (!initPostFile.exists()) {
            logger.error("Failed to modify template post. Check it manually :).")
            return false
        }

        initPostFile.renameTo(File(initPostFile.parentFile, newInitialPostName()))

        return true
    }

    private fun newInitialPostName() = "%s-Initial.md"
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(LocalDate.now()))
}