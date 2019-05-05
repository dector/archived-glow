package io.github.dector.glow

import io.github.dector.glow.core.cli.cliCommands
import io.github.dector.glow.core.di.DI
import io.github.dector.glow.core.logger.RootLogger
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.utils.StopWatch.Companion.DefaultSecondsFormatter
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val executionResult = executeApp(args)
    val timeToDisplay = DefaultSecondsFormatter(executionResult.timeMs)

    when (executionResult) {
        is Success ->
            UILogger.info("\nFinished in $timeToDisplay.")
        is Failed -> {
            UILogger.info("\nFailed after $timeToDisplay.")
            exitProcess(1)
        }
    }
}

private fun executeApp(args: Array<String>): ExecutionResult {
    var error: Throwable? = null

    val executionTime = measureTimeMillis {
        DI.init()

        try {
            cliCommands()
                    .main(args)
        } catch (e: Throwable) {
            RootLogger.error(e.message, e)
            error = e
        }
    }

    return error.let { err ->
        if (err == null) Success(executionTime)
        else Failed(executionTime, err)
    }
}

private sealed class ExecutionResult(val timeMs: Long)
private class Success(timeMs: Long) : ExecutionResult(timeMs)
private class Failed(timeMs: Long, val error: Throwable) : ExecutionResult(timeMs)