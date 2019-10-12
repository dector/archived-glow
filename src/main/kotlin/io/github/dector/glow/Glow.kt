package io.github.dector.glow

import arrow.core.Either
import io.github.dector.glow.cli.runCli
import io.github.dector.glow.core.logger.RootLogger
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.utils.StopWatch.Companion.DefaultSecondsFormatter
import io.github.dector.glow.utils.measureOperationTimeMillis
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    initApp()

    measureAndPrintExecution {
        executeApp(args)
    }
}

private fun initApp() {
    //DI.init()
    DI.resetAction = {
        DI.init()
        DI.modify {
            it.modules(appModule)
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
