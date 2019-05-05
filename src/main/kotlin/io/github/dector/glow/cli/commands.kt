package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.dector.glow.CliHeader
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.logger.disableUiLogger
import io.github.dector.glow.di.DI

class GlowCommand : CliktCommand(name = "glow") {
    override fun run() {}
}

class BuildCommand : CliktCommand(name = "build") {
    val quiet by option("-q", "--quiet",
            help = "Don't print anything").flag()

    override fun run() {
        if (quiet) disableUiLogger()

        UILogger.info(CliHeader)

        DI.get<GlowEngine>().execute()
    }
}

class FishCommand : CliktCommand(name = "fish") {

    private val fish = """
          /`·.¸
         /¸...¸`:·
     ¸.·´  ¸   `·.¸.·´)
    : © ):´;      ¸  {
     `·.¸ `·  ¸.·´\`·¸)
         `\\´´\¸.·´     Hi, there!"""

    override fun run() {
        UILogger.info(fish)
    }
}

fun cliCommands() = GlowCommand()
        .subcommands(BuildCommand(), FishCommand())