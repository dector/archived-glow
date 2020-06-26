package io.github.dector.glow.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.NOPLogger
import org.slf4j.helpers.SubstituteLogger
import org.slf4j.helpers.SubstituteLoggerFactory

private val _UiLogger: SubstituteLogger =
    (SubstituteLoggerFactory().getLogger("") as SubstituteLogger)
        .apply { setDelegate(TransparentLogger()) }
val UILogger: Logger = _UiLogger

fun setUiLoggerEnabled(enabled: Boolean) {
    _UiLogger.setDelegate(if (enabled) TransparentLogger() else NOPLogger.NOP_LOGGER)
}

val EmptyLogger: Logger = SubstituteLoggerFactory().getLogger("")

val RootLogger: Logger = LoggerFactory.getLogger("")

fun Any.logger(): Logger = LoggerFactory.getLogger(javaClass)
