package io.github.dector.glow

import io.github.dector.glow.cli.cliCommands
import io.github.dector.glow.core.logger.RootLogger
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.di.DI
import io.github.dector.glow.utils.StopWatch.Companion.DefaultSecondsFormatter
import io.github.dector.glow.utils.measureTimeMillis
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    initApp()

    measureAndPrintExecution {
        executeApp(args)
    }
}

private fun initApp() {
    DI.init()
}

private fun measureAndPrintExecution(operation: () -> Result<Unit>) {
    val result = measureTimeMillis(operation)
    val timeToDisplay = DefaultSecondsFormatter(result.second)

    when {
        result.first.isSuccess ->
            UILogger.info("\nFinished in $timeToDisplay.")
        result.first.isFailure -> {
            UILogger.info("\nFailed after $timeToDisplay.")
            exitProcess(1)
        }
    }
}

private fun executeApp(args: Array<String>): Result<Unit> = try {
    cliCommands().main(args)
    Result.success(Unit)
} catch (e: Throwable) {
    RootLogger.error(e.message, e)
    Result.failure(e)
}