package io.github.dector.glow.core.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.NOPLogger
import org.slf4j.helpers.SubstituteLogger
import org.slf4j.helpers.SubstituteLoggerFactory

private val _UiLogger: SubstituteLogger = (SubstituteLoggerFactory().getLogger("") as SubstituteLogger)
        .apply { setDelegate(TransparentLogger()) }
val UiLogger: Logger = _UiLogger

val EmptyLogger: Logger = SubstituteLoggerFactory().getLogger("")

val RootLogger: Logger = LoggerFactory.getLogger("")


fun disableUiLogger() {
    _UiLogger.setDelegate(NOPLogger.NOP_LOGGER)
}

fun Any.logger(): Logger = LoggerFactory.getLogger(javaClass)

@Deprecated("Use `RootLogger` instead", ReplaceWith("RootLogger"))
fun rootLogger(): Logger = RootLogger