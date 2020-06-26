package io.github.dector.glow.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.SubstituteLoggerFactory

val EmptyLogger: Logger = SubstituteLoggerFactory().getLogger("")

val RootLogger: Logger = LoggerFactory.getLogger("")

fun Any.logger(): Logger = LoggerFactory.getLogger(javaClass)
