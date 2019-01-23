package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.dector.glow.CliHeader
import io.github.dector.glow.di.DI
import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.logger.disableUiLogger
import io.github.dector.glow.v2.core.GlowEngine

class GlowCommand : CliktCommand(name = "glow") {
    override fun run() {}
}

class BuildCommand : CliktCommand(name = "build") {
    val quiet by option("-q", "--quiet",
            help = "Don't print anything").flag()

    override fun run() {
        if (quiet) disableUiLogger()

        UiLogger.info(CliHeader)

        DI.get<GlowEngine>().execute()
    }
}

fun cliCommands() = GlowCommand()
        .subcommands(BuildCommand())