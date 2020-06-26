package io.github.dector.glow

import io.github.dector.glow.cli.runCli
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.logger.RootLogger
import io.github.dector.glow.logger.UILogger
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // FIXME hack to parse config
    require(args.isNotEmpty()) { "Should be run with '--project path/to/website/project' argument" }

    val projectPath = args.indexOfFirst { it.startsWith("--project") }
        .let { args[it + 1] }
    initApp(projectPath)

    executeApp(args)
}

private fun initApp(projectPath: String) {
    val projectDir = File(projectPath)
    require(projectDir.exists()) { "Can't find project in `${projectDir.absolutePath}`" }

    //DI.init()
    DI.resetAction = {
        DI.init()
        DI.modify {
            it.modules(appModule(projectDir))
        }
    }
    DI.reset()  // Will call init()
}

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
