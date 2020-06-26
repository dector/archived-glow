package io.github.dector.glow

import io.github.dector.glow.cli.runCli
import io.github.dector.glow.logger.RootLogger
import io.github.dector.glow.logger.UILogger
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    executeApp(args)
}

/*
private fun detectProjectDir(rawArgs: Array<String>): File {
    // If runs in current directory
    val defaultFile = File(ConfigFileName)
    if (defaultFile.exists()) return defaultFile.parentFile

    // Else should be run with `--project` option
    val args = LaunchOptions().also { it.parse(rawArgs) }

    val projectDir = args.project
    require(projectDir != null) {
        "Should be run with '--project path/to/website/project' argument."
    }
    require(projectDir.exists()) {
        "Dir not found: '${projectDir.absolutePath}'."
    }
    require(projectDir.isDirectory) {
        "File found: '${projectDir.absolutePath}'. But expected it to be directory."
    }

    return projectDir
}
*/

private fun executeApp(args: Array<String>) {
    val action = { runCli(args) }

    runCatching(action)
        .onFailure { e ->
            RootLogger.error(e.message, e)

            UILogger.info("\nFailed with exception: '${e.message}'.")
            exitProcess(1)
        }.onSuccess {
            UILogger.info("\nFinished successfully.")
        }.getOrNull()
}

/*private const val ConfigFileName = "website.glow"*/
