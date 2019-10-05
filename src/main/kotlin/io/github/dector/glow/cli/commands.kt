package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.logger.disableUiLogger
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.server.Server
import io.github.dector.glow.ui.UiConsole

class GlowCommand : CliktCommand(name = "glow") {

    override fun run() {}
}

class BuildCommand : CliktCommand(name = "build") {

    private val quiet by option("-q", "--quiet",
        help = "Don't print anything").flag()

    override fun run() {
        if (quiet) disableUiLogger()

        UILogger.info(CLI_HEADER)

        DI.get<GlowEngine>().execute()
    }
}

class ServeCommand : CliktCommand(name = "serve") {

    override fun run() {
        Server().run()
    }
}

fun cliCommands() = run {
    val ui = DI.get<UiConsole>()

    GlowCommand()
        .subcommands(BuildCommand(), ServeCommand(), FishCommand(ui))
}
