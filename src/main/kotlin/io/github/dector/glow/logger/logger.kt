package io.github.dector.glow.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.MarkerIgnoringBase
import org.slf4j.helpers.NOPLogger
import org.slf4j.helpers.SubstituteLogger
import org.slf4j.helpers.SubstituteLoggerFactory

private val _UiLogger: SubstituteLogger
        = (SubstituteLoggerFactory().getLogger("") as SubstituteLogger)
        .apply { setDelegate(TransparentLogger()) }
val UiLogger = _UiLogger

fun disableUiLogger() {
    _UiLogger.setDelegate(NOPLogger.NOP_LOGGER)
}

fun Any.logger(): Logger = LoggerFactory.getLogger(javaClass)

fun rootLogger(): Logger = LoggerFactory.getLogger("")

open class TransparentLogger : MarkerIgnoringBase() {

    protected fun log(msg: String?) { println(msg) }
    protected fun log(format: String?, arg: Any?) { println(format?.format(arg)) }
    protected fun log(format: String?, vararg arguments: Any?) { println(format?.format(arguments)) }
    protected fun log(msg: String?, t: Throwable?) { println(msg); t?.printStackTrace(System.out) }

    override fun isWarnEnabled() = true

    override fun warn(msg: String?) { log(msg) }
    override fun warn(format: String?, arg: Any?) { log(format, arg) }
    override fun warn(format: String?, vararg arguments: Any?) { log(format, arguments) }
    override fun warn(format: String?, arg1: Any?, arg2: Any?) { log(format, arg1, arg2) }
    override fun warn(msg: String?, t: Throwable?) { log(msg, t) }

    override fun isInfoEnabled() = true

    override fun info(msg: String?) { log(msg) }
    override fun info(format: String?, arg: Any?) { log(format, arg) }
    override fun info(format: String?, arg1: Any?, arg2: Any?) { log(format, arg1, arg2) }
    override fun info(format: String?, vararg arguments: Any?) { log(format, arguments) }
    override fun info(msg: String?, t: Throwable?) { log(msg, t) }

    override fun isErrorEnabled() = true

    override fun error(msg: String?) { log(msg) }
    override fun error(format: String?, arg: Any?) { log(format, arg) }
    override fun error(format: String?, arg1: Any?, arg2: Any?) { log(format, arg1, arg2) }
    override fun error(format: String?, vararg arguments: Any?) { log(format, arguments) }
    override fun error(msg: String?, t: Throwable?) { log(msg, t) }

    override fun isDebugEnabled() = true

    override fun debug(msg: String?) { log(msg) }
    override fun debug(format: String?, arg: Any?) { log(format, arg) }
    override fun debug(format: String?, arg1: Any?, arg2: Any?) { log(format, arg1, arg2) }
    override fun debug(format: String?, vararg arguments: Any?) { log(format, arguments) }
    override fun debug(msg: String?, t: Throwable?) { log(msg, t) }

    override fun isTraceEnabled() = true

    override fun trace(msg: String?) { log(msg) }
    override fun trace(format: String?, arg: Any?) { log(format, arg) }
    override fun trace(format: String?, arg1: Any?, arg2: Any?) { log(format, arg1, arg2) }
    override fun trace(format: String?, vararg arguments: Any?) { log(format, arguments) }
    override fun trace(msg: String?, t: Throwable?) { log(msg, t) }
}