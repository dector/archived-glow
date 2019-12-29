package io.github.dector.glow

import arrow.core.Either
import io.github.dector.glow.cli.runCli
import io.github.dector.glow.core.logger.RootLogger
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.utils.StopWatch.Companion.DefaultSecondsFormatter
import io.github.dector.glow.utils.measureOperationTimeMillis
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // FIXME hack to parse config
    require(args.isNotEmpty()) { "Should be run with '--project path/to/website/project' argument"}

    val projectPath = args.indexOfFirst { it.startsWith("--project") }
        .let { args[it + 1] }
    initApp(projectPath)

    measureAndPrintExecution {
        executeApp(args)
    }
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

private fun measureAndPrintExecution(operation: () -> Unit) {
    val result = measureOperationTimeMillis {
        operation()
    }
    val timeToDisplay = DefaultSecondsFormatter(result.time)

    if (result.result is Either.Right) {
        UILogger.info("\nFinished in $timeToDisplay.")
    } else {
        UILogger.info("\nFailed after $timeToDisplay.")
        exitProcess(1)
    }
}

private fun executeApp(args: Array<String>) {
    try {
        runCli(args)
    } catch (e: Throwable) {
        RootLogger.error(e.message, e)
        throw e
    }
}
